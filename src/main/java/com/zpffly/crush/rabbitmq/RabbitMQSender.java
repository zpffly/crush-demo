package com.zpffly.crush.rabbitmq;

import com.zpffly.crush.config.RabbitmqConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Log4j2
public class RabbitMQSender implements RabbitTemplate.ConfirmCallback , RabbitTemplate.ReturnCallback {

    @Autowired
    private RabbitTemplate template;

    @PostConstruct
    public void init(){
        template.setReturnCallback(this);
        template.setConfirmCallback(this);
    }

    @Override
    // 消息是否成功到达exchange
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack){
            // todo 更新消息状态
        }else{
            log.error("消息投递失败：　消息Id={}", correlationData.getId());
            // todo 记录失败次数，更新消息状态
        }
    }

    @Override
    // Exchange发送到Queue失败触发回调函数
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        Long correlationId = message.getMessageProperties().getHeader(AmqpHeaders.CORRELATION_ID);
        log.error("消息无法发送到消息队列, correlationId={},replyCode={},replyText={},exchange={},routingKey={}",
                correlationId,replyCode, replyText, exchange, routingKey);
        // todo　更新数据库消息
    }


    public void send(RabbitmqMessage message){
        CorrelationData correlationData = new CorrelationData();
        // userId + goodId
        correlationData.setId("" + message.getUser().getId() + "," + message.getGoodId());
        template.convertAndSend(RabbitmqConfig.exchangeName, RabbitmqConfig.routingKey, message, correlationData);
    }

}
