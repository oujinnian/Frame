package com.jno.cloud.framework.redis.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.Set;

public abstract class RedisAbstractMapper {

    @Autowired
    protected RedisTemplate<String, Serializable> redisTemplate;

    /**
     * 写入缓存
     * @param key
     * @param value
     */
    public abstract void insertOrUpdate(String key, Serializable value);

    /**
     * 写入缓存，设置时间秒
     * @param key
     * @param value
     * @param seconds
     */
    public abstract void insertOrUpdateBySeconds(String key, Serializable value, long seconds);

    /**
     * 写入缓存，设置时间分
     * @param key
     * @param value
     * @param minutes
     */
    public abstract void insertOrUpdateByMinutes(String key, Serializable value, long minutes);

    /**
     * 写入缓存，设置时间小时
     * @param key
     * @param value
     * @param hours
     */
    public abstract void insertOrUpdateByHours(String key, Serializable value, long hours);

    /**
     * 写入缓存，设置时间天
     * @param key
     * @param value
     * @param days
     */
    public abstract void insertOrUpdateByDays(String key, Serializable value, long days);

    /**
     * 查询数据
     * @param key
     * @return
     */
    public abstract Object select(String key);

    /**
     * 获取列表
     * @param pattern
     * @return
     */
    public abstract Set<String> list(String pattern);

    /**
     * 清除数据
     * @param key
     * @return
     */
    public abstract boolean remove(String key);

    /**
     * 清除多个数据
     * @param keys
     * @return
     */
    public abstract int removeAll(String...keys);

    /**
     * 查询数据过期时间
     * @param key
     * @return
     */
    public abstract Long getExpire(String key);


}
