package com.zpffly.crush.rabbitmq;

import com.zpffly.crush.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RabbitMQReceiverTest {
    @Autowired
    RabbitMQSender sender;
    @Autowired
    RabbitMQReceiver receiver;

    @Test
    void test1(){
        RabbitmqMessage message = new RabbitmqMessage();
        User user = new User();
        user.setNickname("11111");
        user.setId(Long.parseLong("18200744309"));
        message.setUser(user);
        message.setGoodId(1);
        sender.send(message);
    }
}