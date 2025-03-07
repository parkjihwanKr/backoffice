package com.example.backoffice.global.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@EnableCaching
@Configuration
public class RedisConfig {


    /**
     * database 0 : refresh token repository
     * database 1 : board view count repository
     * database 2 : upcoming member attendance repository
     * database 3 : monthly update vacation period repository
     * database 4 : often found Member entity field repository
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
    public JedisConnectionFactory redisConnectionFactoryForUpcomingAttendance() {
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
    public JedisConnectionFactory redisConnectionFactoryForCachedMember() {
        RedisStandaloneConfiguration config
                = new RedisStandaloneConfiguration(host, port);
        config.setDatabase(4);
        return new JedisConnectionFactory(config);
    }

    // updateUpcomingVacationPeriod, Member(특정 필드)에 대한 캐싱 데이터 설정
    @Bean
    public RedisCacheManager cacheManagerForVacationPeriod(
            @Qualifier("redisConnectionFactoryForVacationPeriod")
            RedisConnectionFactory redisConnectionFactoryForVacationPeriod) {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.builder(redisConnectionFactoryForVacationPeriod)
                .cacheDefaults(cacheConfiguration)
                .build();
    }

    @Bean
    @Primary
    public RedisCacheManager cacheManagerForCachedMember(
            @Qualifier("redisConnectionFactoryForCachedMember")
            RedisConnectionFactory redisConnectionFactoryForCachedMember) {

        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10)) // 캐싱 유지 시간 설정
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.builder(redisConnectionFactoryForCachedMember)
                .cacheDefaults(cacheConfiguration)
                .build();
    }

    // 모든 데이터 베이스의 읽기/쓰기 저장소
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
    public RedisTemplate<String, Object> redisTemplateForUpcomingAttendance(
            @Qualifier("redisConnectionFactoryForUpcomingAttendance")
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

    @Bean
    public RedisTemplate<String, Object> redisTemplateFormCachedMember(
            @Qualifier("redisConnectionFactoryForCachedMember")
            JedisConnectionFactory redisConnectionFactoryForCachedMember) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactoryForCachedMember);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}
