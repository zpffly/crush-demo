package com.zpffly.crush.service;

import com.zpffly.crush.constant.RedisConstant;
import com.zpffly.crush.dao.GoodsDao;
import com.zpffly.crush.vo.GoodsVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest

class GoodsServiceTest {

    @Autowired
    GoodsService goodsService;
    @Autowired
    RedisTemplate<String, GoodsVO> redisTemplate;
    @Autowired
    GoodsDao goodsDao;


    @Test
    void getGoodVOList() {
        List<GoodsVO> goodVOList = goodsService.getGoodVOList();
        System.out.println(goodVOList);
    }

    @Test
    void getGoodsVO(){
//        GoodsVO good = goodsDao.getGoodVOByGoodId(1);
//        System.out.println(good);
//        redisTemplate.opsForValue().set("goodvo", good);
//        long l = 1;
//        System.out.println(redisTemplate.opsForValue().get("goodvo"+1));
        goodsService.getGoodVOByGoodId(1);
    }
}