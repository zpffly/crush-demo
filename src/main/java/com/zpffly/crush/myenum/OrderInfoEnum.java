package com.zpffly.crush.myenum;

import lombok.Getter;

/**
 * 订单状态枚举类
 * 订单状态，0新建未支付，1已支付，2已发货，3已收货，4已退款，5已完成
 */
@Getter
public enum OrderInfoEnum {
    NEW_ORDER(0, "未支付"),
    PAY_ORDER(1, "已支付"),
    SEND_ORDER(2, "已发货"),
    RECEIVED_ORDER(3, "已收货"),
    REFUND_ORDER(4, "已退款"),
    FINISH_ORDER(5, "已完成")
    ;
    private int code;
    private String msg;
    OrderInfoEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
