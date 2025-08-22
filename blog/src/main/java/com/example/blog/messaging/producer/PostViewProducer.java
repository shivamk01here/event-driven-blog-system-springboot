package com.example.blog.messaging.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostViewProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendViewEvent(Long postId) {
        kafkaTemplate.send("blog.analytics", "Post viewed: " + postId);
    }
}
