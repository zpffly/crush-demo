package com.zpffly.crush.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


@Data
public class CrushGoods {
    //秒杀商品id(自增长）
    private Long id;
    //商品id
    private Long goodsId;
    //秒杀库存
    private Integer stockCount;
    //秒杀开始时间
    private Date startDate;
    //秒杀结束时间
    private Date endDate;
    // 秒杀价格
    private BigDecimal crushPrice;
}
