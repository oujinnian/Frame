package com.jno.cloud.framework.redis.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 用byte序列化value
 * @param <T>
 */

@Repository
public class RedisByteMapper<T> extends RedisMapper {

    @Resource(name="byteRedisTemplate")
    protected RedisTemplate<String,T> redisTemplate;

    public void insertOrUpdate(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void insertOrUpdateBySeconds(String key, T value, long seconds) {
        redisTemplate.opsForValue().set(key, value, seconds);
    }

    public void insertOrUpdateByMinutes(String key, T value, long minutes) {
        redisTemplate.opsForValue().set(key, value, minutes);
    }

    public void insertOrUpdateByHours(String key, T value, long hours) {
        redisTemplate.opsForValue().set(key, value, hours);
    }

    public void insertOrUpdateByDays(String key, T value, long days) {
        redisTemplate.opsForValue().set(key, value, days);
    }

    public T select(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Set<String> list(String pattern) {
        return redisTemplate.keys(pattern);
    }

    public boolean remove(String key) {
        return redisTemplate.delete(key);
    }

    public int removeAll(String... keys) {
        int count = 0;
        for (String key : keys) {
            if(redisTemplate.delete(key)) {
                count++;
            }
        }
        return count;
    }

    public boolean exist(String key) {
        return redisTemplate.hasKey(key);
    }

    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }


    public Boolean expire(String key,long timeout ,TimeUnit unit) {
        return redisTemplate.expire(key,timeout,unit);
    }


}
