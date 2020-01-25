package com.zpffly.crush.service;

import com.zpffly.crush.constant.RedisConstant;
import com.zpffly.crush.dao.OrderDao;
import com.zpffly.crush.entity.CrushOrder;
import com.zpffly.crush.entity.CrushOrderInfo;
import com.zpffly.crush.entity.Goods;
import com.zpffly.crush.entity.User;
import com.zpffly.crush.myenum.OrderInfoEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class OrderService {
    @Autowired
    OrderDao orderDao;
    @Autowired
    RedisTemplate<String, CrushOrder> template;

    public CrushOrder getOrderByUserIdAndGoodId(long userId, long goodId) {
        CrushOrder order = template.opsForValue().get(RedisConstant.ORDER_DETAIL + userId + goodId);
        if (order == null) {
            synchronized (OrderService.class) {
                if (order == null)
                    order = orderDao.getOrderByUserIdAndGoodId(userId, goodId);
            }
        }
        return order;
    }

    public CrushOrderInfo createOrder(User user, Goods goods) {
        CrushOrderInfo orderInfo = new CrushOrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getGoodsPrice());
        orderInfo.setOrderChannel(0);
        orderInfo.setStatus(OrderInfoEnum.NEW_ORDER.getCode());
        orderInfo.setUserId(user.getId());
        // mybatis插入后将id存入这个对象
        orderDao.insertOrderInfo(orderInfo);
        CrushOrder crushOrder = new CrushOrder();
        crushOrder.setGoodsId(goods.getId());
        crushOrder.setOrderId(orderInfo.getId());
        crushOrder.setUserId(user.getId());
        orderDao.insertOrder(crushOrder);
        template.opsForValue().set(RedisConstant.ORDER_DETAIL + user.getId() + goods.getId(), crushOrder, RedisConstant.EXPIRY, TimeUnit.SECONDS);
        return orderInfo;
    }

    public CrushOrderInfo getOrderInfoById(long orderId) {
        return orderDao.getOrderInfoById(orderId);
    }
}
