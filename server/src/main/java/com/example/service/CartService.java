package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.controller.dto.AmountDTO;
import com.example.entity.Account;
import com.example.entity.Cart;
import com.example.entity.Goods;
import com.example.mapper.CartMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车业务处理
 **/
@Service
public class CartService {

    @Resource
    private CartMapper cartMapper;

    @Resource
    private GoodsService goodsService;

    /**
     * 新增
     */
    public void add(Cart cart) {
        Account currentUser = TokenUtils.getCurrentUser();
        cart.setUserId(currentUser.getId());
        cartMapper.insert(cart);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        cartMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            cartMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Cart cart) {
        cartMapper.updateById(cart);
    }

    /**
     * 根据ID查询
     */
    public Cart selectById(Integer id) {
        return cartMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Cart> selectAll(Cart cart) {
        List<Cart> carts = cartMapper.selectAll(cart);
        for (Cart c : carts) {
            Goods goods = goodsService.selectById(c.getGoodsId());
            c.setGoods(goods);
        }
        return carts;
    }

    /**
     * 分页查询
     */
    public PageInfo<Cart> selectPage(Cart cart, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Cart> list = selectAll(cart);
        return PageInfo.of(list);
    }

    /**
     * 计算购物车实付总价
     */
    public AmountDTO calcTotal(List<Cart> carts) {
        AmountDTO amountDTO = new AmountDTO();
        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal actual = BigDecimal.ZERO;
        for (Cart cart : carts) {
            Integer num = cart.getNum();
            Integer goodsId = cart.getGoodsId();
            Goods goods = goodsService.selectById(goodsId);
            if (ObjectUtil.isNotNull(goods)) {
                amount = amount.add(goods.getPrice().multiply(BigDecimal.valueOf(num)));
                actual = actual.add(goods.getActualPrice().multiply(BigDecimal.valueOf(num)));
            }
        }
        amountDTO.setAmount(amount);
        amountDTO.setActual(actual);
        amountDTO.setDiscount(amount.subtract(actual));
        return amountDTO;
    }

    public void deleteByBusinessId(Integer businessId) {
        cartMapper.deleteByBusinessId(businessId);
    }
}