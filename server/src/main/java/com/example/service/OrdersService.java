package com.example.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.example.common.enums.OrderStatusEnum;
import com.example.common.enums.RoleEnum;
import com.example.controller.dto.AmountDTO;
import com.example.controller.dto.OrderDTO;
import com.example.entity.Account;
import com.example.entity.Cart;
import com.example.entity.Orders;
import com.example.entity.OrdersItem;
import com.example.mapper.OrdersMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 业务处理
 **/
@Service
public class OrdersService {

    private static final Logger log = LoggerFactory.getLogger(OrdersService.class);

    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private GoodsService goodsService;

    @Resource
    private CartService cartService;

    @Resource
    private OrdersItemService ordersItemService;

    /**
     * 新增
     */
    public void add(Orders orders) {
        ordersMapper.insert(orders);
    }

    /**
     * 删除
     */
    @Transactional
    public void deleteById(Integer id) {
        ordersItemService.deleteByOrderId(id);
        ordersMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            ordersMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Orders orders) {
        if (OrderStatusEnum.NO_SEND.getValue().equals(orders.getStatus())) {
            // 支付
            orders.setPayTime(DateUtil.now());
        }
        ordersMapper.updateById(orders);
    }

    /**
     * 根据ID查询
     */
    public Orders selectById(Integer id) {
        return ordersMapper.selectById(id);
    }

    private Orders setPreviousCondition(Orders orders) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.BUSINESS.name().equals(currentUser.getRole())) {  // 商家只看自己的数据
            orders.setBusinessId(currentUser.getId());
        } else if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            orders.setUserId(currentUser.getId());
        }
        return orders;
    }

    /**
     * 查询所有
     */
    public List<Orders> selectAll(Orders orders) {
        setPreviousCondition(orders);
        return ordersMapper.selectAll(orders);
    }

    /**
     * 分页查询
     */
    public PageInfo<Orders> selectPage(Orders orders, Integer pageNum, Integer pageSize) {
        setPreviousCondition(orders);
        PageHelper.startPage(pageNum, pageSize);
        List<Orders> list = ordersMapper.selectAll(orders);
        return PageInfo.of(list);
    }

    /**
     * 下单
     */
    @Transactional
    public Orders addOrder(OrderDTO orderDTO) {
        Orders orders = null;
        try {
            List<Cart> cartList = orderDTO.getCartList();
            if (CollUtil.isEmpty(cartList)) {
                return null;
            }
            orders= new Orders();
            // 开始组装订单数据
            orders.setComment(orderDTO.getComment());
            orders.setUser(orderDTO.getUser());
            orders.setPhone(orderDTO.getPhone());
            orders.setAddress(orderDTO.getAddress());
            // 计算总价
            AmountDTO amountDTO = cartService.calcTotal(cartList);
            orders.setAmount(amountDTO.getAmount());
            orders.setActual(amountDTO.getActual());
            orders.setDiscount(amountDTO.getDiscount());

            String orderNo = IdUtil.getSnowflakeNextIdStr();  // 生成订单编号
            orders.setOrderNo(orderNo);
            orders.setTime(DateUtil.now());
            orders.setBusinessId(cartList.get(0).getBusinessId());
            orders.setCover(cartList.get(0).getGoods().getImg());
            Integer nums = cartList.stream().map(Cart::getNum).reduce(Integer::sum).orElse(0); // 计算购物车商品总数量
            orders.setName(cartList.get(0).getGoods().getName() + "等" + nums + "份餐品");
            orders.setStatus(OrderStatusEnum.NO_PAY.getValue());
            Account currentUser = TokenUtils.getCurrentUser();
            orders.setUserId(currentUser.getId());
            // 订单插入数据库
            add(orders);

            // 生成订单详情
            OrdersItem ordersItem;
            for (Cart cart : cartList) {
                ordersItem = new OrdersItem();
                ordersItem.setOrderId(orders.getId());
                ordersItem.setGoodsId(cart.getGoodsId());
                ordersItem.setGoodsImg(cart.getGoods().getImg());
                ordersItem.setGoodsName(cart.getGoods().getName());
                ordersItem.setNum(cart.getNum());
                ordersItem.setPrice(cart.getGoods().getActualPrice());
                ordersItemService.add(ordersItem);
            }

            // 清空购物车
            cartService.deleteBatch(cartList.stream().map(Cart::getId).collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("下单失败，请联系管理员", e);
        }
        return orders;
    }

    public void updateByBusinessId(Integer businessId) {
        ordersMapper.updateByBusinessId(businessId);
    }

    public List<Orders> selectByBusinessId(Integer businessId) {
        return ordersMapper.selectByBusinessId(businessId);
    }

    public void updateStatus(String status, Integer orderId) {
        ordersMapper.updateStatus(status, orderId);
    }

    public List<Orders> selectDoneOrders() {
        return ordersMapper.selectDoneOrders();
    }
}