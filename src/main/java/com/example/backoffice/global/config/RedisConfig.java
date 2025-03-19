package com.example.backoffice.global.config;

import com.example.backoffice.domain.mainPage.dto.MainPageResponseDto;
import com.example.backoffice.domain.vacation.dto.VacationsResponseDto;
import com.example.backoffice.global.date.DateTimeUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@EnableCaching
@Configuration
public class RedisConfig {

    /**
     * database 0 : refresh token repository
     * database 1 : caching data repository
     * database 2 : view count repository
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
    public JedisConnectionFactory redisConnectionFactoryForCacheData() {
        RedisStandaloneConfiguration config
                = new RedisStandaloneConfiguration(host, port);
        config.setDatabase(1);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactoryForViewCount() {
        RedisStandaloneConfiguration config
                = new RedisStandaloneConfiguration(host, port);
        config.setDatabase(2);
        return new JedisConnectionFactory(config);
    }

    @Bean
    @Primary
    public RedisCacheManager cacheManagerForMainPage(
            @Qualifier("redisConnectionFactoryForCacheData")
            RedisConnectionFactory redisConnectionFactoryForCachedData) {

        Jackson2JsonRedisSerializer<MainPageResponseDto.ReadOneDto> serializer =
                new Jackson2JsonRedisSerializer<>(MainPageResponseDto.ReadOneDto.class);

        RedisCacheConfiguration cacheConfiguration
                = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(DateTimeUtils.getAtEndOfDay())
                .serializeValuesWith(
                        RedisSerializationContext
                                .SerializationPair
                                .fromSerializer(serializer));

        return RedisCacheManager.builder(redisConnectionFactoryForCachedData)
                .cacheDefaults(cacheConfiguration)
                .build();
    }

    @Bean
    public RedisCacheManager cacheManagerForVacationPeriod(
            @Qualifier("redisConnectionFactoryForCacheData")
            RedisConnectionFactory redisConnectionFactoryForCachedData,
            ObjectMapper objectMapper) {

        Jackson2JsonRedisSerializer<VacationsResponseDto.ReadPeriodDto> serializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, VacationsResponseDto.ReadPeriodDto.class);

        RedisCacheConfiguration cacheConfiguration
                = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(DateTimeUtils.getEndDayOfMonth())
                .serializeValuesWith(
                        RedisSerializationContext
                                .SerializationPair
                                .fromSerializer(serializer));

        return RedisCacheManager.builder(redisConnectionFactoryForCachedData)
                .cacheDefaults(cacheConfiguration)
                .build();
    }

    // 추가적으로 Object의 저장 가능성이 존재하기에
    // @Cacheable과 같은 Spring boot 환경에서는
    // GenericJackson2JsonRedisSerializer 방식을 유지
    @Bean
    public RedisCacheManager cacheManagerForViewCount(
            @Qualifier("redisConnectionFactoryForViewCount")
            RedisConnectionFactory redisConnectionFactoryForViewCount) {

        RedisCacheConfiguration cacheConfiguration
                = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.builder(redisConnectionFactoryForViewCount)
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
    public RedisTemplate<String, Object> redisTemplateForCacheData(
            @Qualifier("redisConnectionFactoryForCacheData")
            JedisConnectionFactory redisConnectionFactoryForCacheData,
            ObjectMapper objectMapper) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactoryForCacheData);

        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        // 디폴트 시리얼라이저 적용
        template.setDefaultSerializer(serializer);
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        return template;
    }

    @Bean
    public RedisTemplate<String, VacationsResponseDto.ReadPeriodDto> redisTemplateForVacation(
            @Qualifier("redisConnectionFactoryForCacheData")
            RedisConnectionFactory redisConnectionFactoryForCachedData) {

        RedisTemplate<String, VacationsResponseDto.ReadPeriodDto> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactoryForCachedData);

        Jackson2JsonRedisSerializer<VacationsResponseDto.ReadPeriodDto> serializer =
                new Jackson2JsonRedisSerializer<>(VacationsResponseDto.ReadPeriodDto.class);

        template.setValueSerializer(serializer);
        template.setKeySerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public RedisTemplate<String, String> redisTemplateForViewCount(
            @Qualifier("redisConnectionFactoryForViewCount")
            JedisConnectionFactory redisConnectionFactoryForViewCount) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactoryForViewCount);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}
