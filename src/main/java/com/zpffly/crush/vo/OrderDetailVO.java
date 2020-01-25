package com.zpffly.crush.vo;

import com.zpffly.crush.entity.CrushOrderInfo;
import lombok.Data;

@Data
public class OrderDetailVO {
    private GoodsVO goodsVO;
    private CrushOrderInfo order;
}
