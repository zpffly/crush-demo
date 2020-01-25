package com.zpffly.crush.service;

import com.zpffly.crush.constant.RedisConstant;
import com.zpffly.crush.entity.*;
import com.zpffly.crush.exception.CrushException;
import com.zpffly.crush.myenum.CrushEnum;
import com.zpffly.crush.util.MD5Util;
import com.zpffly.crush.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
public class CrushService {
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    RedisTemplate redisTemplate;


    /**
     * 根据用户和商品执行秒杀操作
     * @param user
     * @param goods
     * @return
     */
    @Transactional
    public CrushOrderInfo doCrush(User user, Goods goods){
        // 减少秒杀商品的库存
        if (goodsService.reduceCrushGoodStock(goods.getId()) == 0){
            // 设置商品卖完
            setGoodOver(goods.getId());
            throw new CrushException(CrushEnum.GOOD_STOCK_ZERO.getMsg());
        }
        // 生成订单
        return orderService.createOrder(user, goods);
    }


    public Result getCrushResult(Long userId, long goodId) {
        CrushOrder order = orderService.getOrderByUserIdAndGoodId(userId, goodId);
        // 秒杀成功
        if (order != null){
//            System.err.println(order.getId());
            return Result.success(CrushEnum.CRUSH_SUCCESS,order.getId());
        }else{ // 判断秒杀失败或者还在等待消费
            if (isGoodOver(goodId)){
                return Result.error(CrushEnum.STOCK_LOWER_ZERO);
            }else {
                return Result.success(CrushEnum.CONFIRM_ORDER_ING);
            }
        }
    }

    private void setGoodOver(Long id) {
        redisTemplate.opsForValue().set(RedisConstant.GOOD_OVER + id, true);
    }
    private boolean isGoodOver(long goodId){
        return redisTemplate.opsForValue().get(RedisConstant.GOOD_OVER + goodId) != null;
    }

    public boolean checkPath(User user, long goodId, String path) {
        if (user == null || path == null)
            return false;
        String existPath = (String) redisTemplate.opsForValue().get(RedisConstant.CrushPath + user.getId() + goodId);
        return path.equals(existPath);
    }

    public String createCrushPath(User user, long goodId) {
        String path = MD5Util.md5(UUIDUtil.generate());
        redisTemplate.opsForValue().set(RedisConstant.CrushPath+user.getId()+goodId, path, RedisConstant.pathExpiry, TimeUnit.SECONDS);
        return path;
    }
}
