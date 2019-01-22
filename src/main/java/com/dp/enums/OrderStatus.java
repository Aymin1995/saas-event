package com.dp.enums;

/**
 * description: 订单状态枚举
 * Author	Date	Changes
 * Aymin 10:01 2019/1/22 Created
 *
 * @author Aymin
 */
public enum OrderStatus {
    // 待支付，待发货，待收货，订单结束
    WAIT_PAYMENT(1), WAIT_DELIVER(2), WAIT_RECEIVE(3), FINISH(4);

    OrderStatus(Integer code){
        code = this.code;
    }
    private Integer code;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public static OrderStatus getOrderStatuByCode(Integer code){
        for (OrderStatus s:OrderStatus.values()) {
            if(s.getCode().intValue()==code.intValue()){
                return s;
            };
        }
        return null;
    }
}
