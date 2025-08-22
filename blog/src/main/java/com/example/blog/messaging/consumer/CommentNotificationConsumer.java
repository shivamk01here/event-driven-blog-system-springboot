package com.example.blog.messaging.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class CommentNotificationConsumer {
    @RabbitListener(queues = "comment.notifications")
    public void receiveNotification(String message) {
        System.out.println("[RabbitMQ NOTIFICATION RECEIVED] " + message);
    }
}
