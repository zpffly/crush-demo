package com.zpffly.crush.service;

import com.zpffly.crush.constant.RedisConstant;
import com.zpffly.crush.dao.GoodsDao;
import com.zpffly.crush.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class GoodsService {

    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    RedisTemplate<String, GoodsVO> redisTemplate;

    public List<GoodsVO> getGoodVOList(){
        return goodsDao.getGoodVOList();
    }

    public GoodsVO getGoodVOByGoodId(long goodId){
        return goodsDao.getGoodVOByGoodId(goodId);
//        GoodsVO good = redisTemplate.opsForValue().get(RedisConstant.GOODSVO + goodId);
//        // 取不到从数据库取
//        if (good == null){
//            synchronized (GoodsService.class){
//                good = redisTemplate.opsForValue().get(RedisConstant.GOODSVO+goodId);
//                if (good == null){
//                    good = goodsDao.getGoodVOByGoodId(goodId);
//                    redisTemplate.opsForValue().set(RedisConstant.GOODSVO+goodId, good, RedisConstant.EXPIRY, TimeUnit.SECONDS);
//                }
//            }
//        }
//
//        return good;
    }

    // 根据商品id减少秒杀商品的库存
    public int reduceCrushGoodStock(long goodId){
        return goodsDao.reduceCrushGoodStock(goodId);
    }
}
