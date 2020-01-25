package com.zpffly.crush;

import com.zpffly.crush.entity.CrushOrderInfo;
import com.zpffly.crush.entity.User;
import com.zpffly.crush.service.GoodsService;
import com.zpffly.crush.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.net.UnknownServiceException;

@SpringBootTest
class CrushApplicationTests {

    @Autowired
    RedisTemplate<String, Integer> redisTemplate;
    @Autowired
    OrderService orderService;
    @Autowired
    GoodsService goodsService;

//    @Test
//    void test1() {
//        User user = new User();
//        user.setId((long) 111);
//        user.setNickname("aaaaaaa");
//        redisTemplate.opsForValue().set("k1", user);
//        User user1 = redisTemplate.opsForValue().get("k1");
//        System.out.println(user1);
//    }

    @Test
    void test2(){
        System.out.println(orderService.getOrderByUserIdAndGoodId(1, 1));
    }

    @Test
    void test3(){
        System.out.println(goodsService.reduceCrushGoodStock(2));
    }

    @Test
    void Test4(){
        redisTemplate.opsForValue().set("k1", 11);
        System.out.println(redisTemplate.opsForValue().get("k1"));
    }

    @Test
    void Test5(){
        CrushOrderInfo orderInfo = orderService.getOrderInfoById(5);
        System.out.println(orderInfo);
    }

}
