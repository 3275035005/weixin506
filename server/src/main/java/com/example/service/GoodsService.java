package com.example.service;

import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Goods;
import com.example.entity.Orders;
import com.example.entity.OrdersItem;
import com.example.mapper.GoodsMapper;
import com.example.mapper.OrdersItemMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 商品信息业务处理
 **/
@Service
public class GoodsService {

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private BusinessService businessService;

    @Resource
    private OrdersItemService ordersItemService;

    @Resource
    private OrdersService ordersService;

    /**
     * 新增
     */
    public void add(Goods goods) {
        businessService.checkBusiness();
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.BUSINESS.name().equals(currentUser.getRole())) {
            goods.setBusinessId(currentUser.getId());
        }
        goodsMapper.insert(goods);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        goodsMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            goodsMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Goods goods) {
        goodsMapper.updateById(goods);
    }

    /**
     * 根据ID查询
     */
    public Goods selectById(Integer id) {
        Goods goods = goodsMapper.selectById(id);
        wrapGoods(goods);
        return goods;
    }

    /**
     * 查询所有
     */
    public List<Goods> selectAll(Goods goods) {
        List<Goods> list = goodsMapper.selectAll(goods);
        for (Goods g : list) {
            wrapGoods(g);
        }
        return list;
    }

    /**
     * 分页查询
     */
    public PageInfo<Goods> selectPage(Goods goods, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.BUSINESS.name().equals(currentUser.getRole())) {  // 商家只看自己的数据
            goods.setBusinessId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> list = goodsMapper.selectAll(goods);
        return PageInfo.of(list);
    }

    /**
     * 获取实际价格
     */
    public Goods wrapGoods(Goods goods) {
        if (goods == null) {
            return null;
        }
        BigDecimal actualPrice = goods.getPrice().multiply(BigDecimal.valueOf(goods.getDiscount())).setScale(2, RoundingMode.FLOOR);
        goods.setActualPrice(actualPrice);

        // 查询商品销量
        int saleCount = 0;
        List<Orders> ordersList = ordersService.selectDoneOrders();
        for (Orders o : ordersList) {
            OrdersItem ordersItem = new OrdersItem();
            ordersItem.setOrderId(o.getId());
            ordersItem.setGoodsId(goods.getId());
            List<OrdersItem> ordersItems = ordersItemService.selectAll(ordersItem);
            saleCount += ordersItems.stream().map(OrdersItem::getNum).reduce(Integer::sum).orElse(0);
        }
        goods.setSaleCount(saleCount);
        return goods;
    }

}