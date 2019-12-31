package com.alibaba.nacos.example.spring.cloud.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;

/**
 * redis配置
 * @since 1.0
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<Serializable, Serializable> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<Serializable, Serializable> template = new RedisTemplate<Serializable, Serializable>();
        template.setConnectionFactory(connectionFactory);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer(om);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(jsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisUtil redisUtil(RedisTemplate redisTemplate) {
        RedisUtil<Serializable, Serializable> redisUtil = new RedisUtil<Serializable, Serializable>();
        redisUtil.setRedisTemplate(redisTemplate);
        return redisUtil;
    }
}


