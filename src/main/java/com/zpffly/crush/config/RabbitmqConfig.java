package com.zpffly.crush.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    public static final String queueName = "crush_queue";
    public static final String exchangeName = "crush_exchange";
    public static final String routingKey = "crush.routingKey";

    @Bean
    public Queue queue(){
        return new Queue(queueName, true, false, false);
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(exchangeName, false, false);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(directExchange()).with(routingKey);
    }


}
