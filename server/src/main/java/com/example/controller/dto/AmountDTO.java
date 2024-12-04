package com.example.controller.dto;

import java.math.BigDecimal;

/**
 * 订单金额包装类
 */
public class AmountDTO {
    // 总价格
    private BigDecimal amount;
    //优惠金额
    private BigDecimal discount;
    // 实付款
    private BigDecimal actual;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getActual() {
        return actual;
    }

    public void setActual(BigDecimal actual) {
        this.actual = actual;
    }
}
