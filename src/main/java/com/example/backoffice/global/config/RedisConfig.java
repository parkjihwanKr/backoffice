package com.example.backoffice.global.config;

import org.springframework.beans.factory.annotation.Qualifier;
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


    /**
     * database 0 : refresh token repository
     * database 1 : board view count
     * database 2 : upcoming member attendance
     * database 3 : monthly update vacation period
     */

    @Value("${spring.data.redis.port}")
    private Integer port;

    @Value("${spring.data.redis.host}")
    private String host;

    @Bean
    public JedisConnectionFactory redisConnectionFactoryForToken() {
        RedisStandaloneConfiguration config
                = new RedisStandaloneConfiguration(host, port);
        config.setDatabase(0);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactoryForViewCount() {
        RedisStandaloneConfiguration config
                = new RedisStandaloneConfiguration(host, port);
        config.setDatabase(1);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactoryForCachedMemberAttendance() {
        RedisStandaloneConfiguration config
                = new RedisStandaloneConfiguration(host, port);
        config.setDatabase(2);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactoryForVacationPeriod() {
        RedisStandaloneConfiguration config
                = new RedisStandaloneConfiguration(host, port);
        config.setDatabase(3);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplateForToken(
            @Qualifier("redisConnectionFactoryForToken")
            JedisConnectionFactory redisConnectionFactoryForToken) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactoryForToken);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplateForViewCount(
            @Qualifier("redisConnectionFactoryForViewCount")
            JedisConnectionFactory redisConnectionFactoryForViewCount) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactoryForViewCount);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplateForCachedMemberAttendance(
            @Qualifier("redisConnectionFactoryForCachedMemberAttendance")
            JedisConnectionFactory redisConnectionFactoryForCachedMemberAttendance) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactoryForCachedMemberAttendance);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplateForVacationPeriod(
            @Qualifier("redisConnectionFactoryForVacationPeriod")
            JedisConnectionFactory redisConnectionFactoryForVacationPeriod) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactoryForVacationPeriod);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

}
