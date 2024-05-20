package com.example.backoffice.global.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplateForNotification;
    private final ChannelTopic topic;

    public void publish(String message){
        redisTemplateForNotification.convertAndSend(
                topic.getTopic(), message
        );
    }
}
