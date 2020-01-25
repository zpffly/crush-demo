package com.zpffly.crush.rabbitmq;

import com.zpffly.crush.entity.User;
import lombok.Data;

import java.io.Serializable;

/**
 * 消息要实现Serializable接口
 */
@Data
public class RabbitmqMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private User user;
    private long goodId;
}
