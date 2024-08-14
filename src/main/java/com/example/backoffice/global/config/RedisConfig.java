package com.example.backoffice.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.port}")
    private Integer port;

    @Value("${spring.data.redis.host}")
    private String host;

    @Bean
    public JedisConnectionFactory redisConnectionFactoryForToken() {
        RedisStandaloneConfiguration config
                = new RedisStandaloneConfiguration(host, port);
        // refreshToken 데이터베이스
        config.setDatabase(0);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactoryForViewCount() {
        RedisStandaloneConfiguration config
                = new RedisStandaloneConfiguration(host, port);
        // viewCount 데이터베이스
        config.setDatabase(1);
        return new JedisConnectionFactory(config);
    }


    @Bean
    public RedisTemplate<String, Object> redisTemplateForToken(
            JedisConnectionFactory redisConnectionFactoryForToken) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactoryForToken);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplateForViewCount(
            JedisConnectionFactory redisConnectionFactoryForViewCount) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactoryForViewCount);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}
