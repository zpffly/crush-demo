package com.zpffly.crush.entity;

import lombok.Data;

@Data
public class CrushOrder {
    private Long id;
    private Long userId;
    private Long  orderId;
    private Long goodsId;
}
