package com.zpffly.crush.entity;

import lombok.Data;

import java.util.Date;

@Data
public class CrushOrderInfo {
    private Long id; //order id
    private Long userId;
    private Long goodsId;
    private Long  deliveryAddrId;
    private String goodsName;
    private Integer goodsCount;
    private Double goodsPrice;
    private Integer orderChannel;
    private Integer status;
    private Date createDate;
    private Date payDate;
}
