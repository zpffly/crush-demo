package com.zpffly.crush.myenum;

import lombok.Data;
import lombok.Getter;

/**
 * 项目枚举类
 */

@Getter
public enum  CrushEnum {

    LOGIN_SUCCESS(0, "登录成功"),
    VALIDATOR_ERROR(1, "参数校验错误：%s"),
    USER_IS_NULL(2, "用户为空"),
    USER_NOT_FOUND(3, "用户不存在"),
    PASSWORD_ERROR(4, "密码错误"),


    // 秒杀模块
    STOCK_LOWER_ZERO(5, "商品秒杀已经结束"),
    GOOD_CRUSH_REPEAT(6, "商品只能秒杀一次"),
    CRUSH_SUCCESS(10, "秒杀成功"),
    CRUSH_FAIL(11, "秒杀失败"),
    PATH_ERROR(12, "秒杀地址不存在"),

    // 订单模块
    ORDER_NOT_EXIST(7, "订单不存在"),
    GOOD_STOCK_ZERO(8, "商品库存为0"),

    CONFIRM_ORDER_ING(9, "正在确认订单")
    ;

    private int code;
    private String msg;
    CrushEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
