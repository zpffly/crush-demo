package com.zpffly.crush.rabbitmq;

import com.rabbitmq.client.Channel;
import com.zpffly.crush.config.RabbitmqConfig;
import com.zpffly.crush.entity.CrushOrder;
import com.zpffly.crush.entity.User;
import com.zpffly.crush.service.CrushService;
import com.zpffly.crush.service.GoodsService;
import com.zpffly.crush.service.OrderService;
import com.zpffly.crush.vo.GoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RabbitListener(queues = RabbitmqConfig.queueName)
public class RabbitMQReceiver {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CrushService crushService;

    @RabbitHandler
    public void handleMessage(@Payload RabbitmqMessage message, @Headers Map<String, Object> headers, Channel channel){
        try{
            Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
            // 业务处理：下单
            User user = message.getUser();
            long goodId = message.getGoodId();
            // 判断库存
            GoodsVO good = goodsService.getGoodVOByGoodId(goodId);
            // 还有库存
            if (good.getStockCount() > 0){
                CrushOrder order = orderService.getOrderByUserIdAndGoodId(user.getId(), goodId);
                // 用户没有下过这个订单，可以下单
                if (order == null)
                    crushService.doCrush(user, good);
            }
            //　回复
            channel.basicAck(deliveryTag, false);
        }catch (IOException e){
            log.info("消费者 error：{}", e.getMessage());
        }
    }
}
