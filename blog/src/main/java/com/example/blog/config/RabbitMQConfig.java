package com.example.blog.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.*;

@Configuration
public class RabbitMQConfig {
    public static final String COMMENT_NOTIFICATION_QUEUE = "comment.notifications";

    @Bean
    public Queue queue() {
        return new Queue(COMMENT_NOTIFICATION_QUEUE, false);
    }
}
