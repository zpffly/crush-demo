package com.zpffly.crush.constant;

public interface RedisConstant {
    long EXPIRY = 3600;


//    键前缀

    String GOODSVO = "goodsVo";
    String ORDER_DETAIL = "order_detail";

    // 商品库存
    String GOODS_STOCK  = "good_stock";

    // 商品售完标志
    String GOOD_OVER = "good_over";

    // 秒杀路径
    String CrushPath = "path";
    long pathExpiry = 60;
}
