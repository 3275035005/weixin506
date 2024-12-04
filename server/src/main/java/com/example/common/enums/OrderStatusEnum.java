package com.example.common.enums;

/**
 * 订单状态
 */
public enum OrderStatusEnum {

    CANCEL("已取消"),
    NO_PAY("待支付"),
    REFUND("已退款"),
    NO_SEND("待发货"),
    NO_ACCEPT("待收货"),
    NO_EVALUATION("待评价"),
    DONE("已完成"),
    ;
    private String value;

    public String getValue() {
        return value;
    }

    OrderStatusEnum(String value) {
        this.value = value;
    }
}
