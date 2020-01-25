package com.zpffly.crush.vo;

import com.zpffly.crush.entity.Goods;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsVO extends Goods {
    private Integer stockCount;//秒杀库存
    private Date startDate;//秒杀开始时间
    private Date endDate;//秒杀结束时间
    private BigDecimal crushPrice;

    @Override
    public String toString() {
        return "GoodsVO{" + "Good=" + super.toString()+
                "stockCount=" + stockCount +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", crushPrice=" + crushPrice +
                '}';
    }
}
