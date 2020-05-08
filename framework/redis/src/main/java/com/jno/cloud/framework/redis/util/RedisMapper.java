package com.jno.cloud.framework.redis.util;

import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisMapper extends RedisAbstractMapper {

    @Override
    public void insertOrUpdate(String key, Serializable value) {
        redisTemplate.opsForValue().set(key,value);
    }

    @Override
    public void insertOrUpdateBySeconds(String key, Serializable value, long seconds) {
        redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
    }

    @Override
    public void insertOrUpdateByMinutes(String key, Serializable value, long minutes) {
        redisTemplate.opsForValue().set(key, value, minutes, TimeUnit.MINUTES);
    }

    @Override
    public void insertOrUpdateByHours(String key, Serializable value, long hours) {
        redisTemplate.opsForValue().set(key, value, hours, TimeUnit.HOURS);
    }

    @Override
    public void insertOrUpdateByDays(String key, Serializable value, long days) {
        redisTemplate.opsForValue().set(key, value, days, TimeUnit.DAYS);
    }

    @Override
    public Object select(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Set<String> list(String pattern) {
        return redisTemplate.keys("*"+ pattern +"*");
    }

    @Override
    public boolean remove(String key) {
        return redisTemplate.delete(key);
    }

    @Override
    public int removeAll(String... keys) {
        int count = 0;
        for (String key : keys){
            if (redisTemplate.delete(key))
                count++;
        }
        return count;
    }

    @Override
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key,TimeUnit.SECONDS);
    }

    /**
     * 是否存在该键值
     * @param key
     * @return
     */
    public boolean exist(String key){
        return redisTemplate.hasKey(key);
    }

    public Map<String,Object> getParameter(List<Object> list){
        Map<String,Object> mapList = new HashMap<>();
        Set<String> keys = null;
        String k = null;
        if (list.size() > 0){
            for(int i = 0; i < list.size(); i++){
                keys = redisTemplate.keys("*" + list.get(i).toString() + "*");
                List<Object> listresult = new ArrayList<>();
                for (String key:keys) {
                    Object obj = redisTemplate.opsForValue().get(key);
                    listresult.add(obj);
                }
                mapList.put(list.get(i).toString(),listresult);
            }
        }
        return mapList;
    }

}
