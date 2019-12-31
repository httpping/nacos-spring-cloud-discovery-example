package com.alibaba.nacos.example.spring.cloud.config;

import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 *
 * @since 1.0
 */
public class RedisUtil<K extends Serializable, V extends Serializable> {

    private RedisTemplate<Serializable, Serializable> redisTemplate;

    public void setRedisTemplate(RedisTemplate<Serializable, Serializable> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 设置指定 key 的值
     *
     * @param key
     * @param value
     */
    public void set(K key, V value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置key value 并设置过期时间为timeout
     *
     * @param key
     * @param value
     * @param timeout 过期时间 毫秒
     */
    public void setEx(K key, V value, long timeout) {
        setEx(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 设置key value 并设置过期时间为timeout
     *
     * @param key
     * @param value
     * @param timeout 过期时间
     * @param unit    时间单位
     */
    public void setEx(K key, V value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 获取指定 key 的值
     *
     * @param key
     * @return
     */
    public  V get(K key) {
        return (V) redisTemplate.opsForValue().get(key);
    }

    /**
     * 将给定 key 的值设为 value ，并返回 key 的旧值
     *
     * @param key
     * @param value
     * @return
     */
    public V getAndSet(K key, V value) {
        return (V) redisTemplate.opsForValue().getAndSet(key, value);
    }

    /**
     * 删除key
     *
     * @param key
     */
    public void delete(K key) {
        redisTemplate.delete(key);
    }

    /**
     * 是否存在key
     *
     * @param key
     * @return
     */
    public Boolean hasKey(K key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param timeout 毫秒
     * @return
     */
    public Boolean expire(K key, long timeout) {
        return expire(key, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public Boolean expire(K key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param date
     * @return
     */
    public Boolean expireAt(K key, Date date) {
        return redisTemplate.expireAt(key, date);
    }

    public void putMap(String key, String sku, Object value) {
        redisTemplate.opsForHash().put(key, sku, value);
    }
    public void delMap(String key,String sku) {
        redisTemplate.opsForHash().delete(key, sku);
    }

    public Object getMap(String key,String sku) {
        return redisTemplate.opsForHash().get(key,sku);
    }

    public  Map<Object, Object> getMap(String key){
        Map<Object, Object> result = redisTemplate.opsForHash().entries(key);
        return result;
    }

}
