package com.example.blog.messaging.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import com.example.blog.config.RabbitMQConfig;

@Service
@RequiredArgsConstructor
public class CommentNotificationProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendNotification(Long postAuthorId, String info) {
        String payload = "User ID " + postAuthorId + ": " + info;
        rabbitTemplate.convertAndSend(RabbitMQConfig.COMMENT_NOTIFICATION_QUEUE, payload);
    }
}
