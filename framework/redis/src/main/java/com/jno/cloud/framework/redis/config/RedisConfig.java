package com.jno.cloud.framework.redis.config;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.time.Duration;

/**
 * redis配置类
 * @Date: 2020/5/8 9:30
 * @Author: ojn
 */

@Configuration
@EnableCaching   //开启注解
@AutoConfigureAfter(RedisAutoConfiguration.class) //加载RedisAutoConfiguration类后再加载当前类
public class RedisConfig extends CachingConfigurerSupport {

//    /**
//     * retemplate相关配置
//     */
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory){
//
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        //配置连接工厂
//        template.setConnectionFactory(factory);
//
//        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
//        Jackson2JsonRedisSerializer jacksonSeial = new Jackson2JsonRedisSerializer(Object.class);
//
//        ObjectMapper om = new ObjectMapper();
//        //指定要序列化的域，field，get和set，以及修饰符范围，ANY是都有包括private和public
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        //指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String，Integer等会抛出异常
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jacksonSeial.setObjectMapper(om);
//
//        //使用StringRedisSerializer来序列化何反序列化redis的key值
//        template.setKeySerializer(new StringRedisSerializer());
//        //值采用json序列化
//        template.setValueSerializer(jacksonSeial);
//
//        //hash key采用String的序列化方式
//        template.setHashKeySerializer(new StringRedisSerializer());
//        //hash value值采用json序列化
//        template.setHashValueSerializer(jacksonSeial);
//        template.afterPropertiesSet();
//
//        return template;
//    }

    @Bean(name="byteRedisTemplate")
    public RedisTemplate byteRedisTemplate(LettuceConnectionFactory redisConnectionFactory){
        RedisTemplate template = new RedisTemplate<String, Serializable>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new RedisByteSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());// Hash key序列化
        template.setHashValueSerializer(new RedisByteSerializer());// Hash value序列化
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    @Primary
    public RedisTemplate<String,Serializable> redisCacheTemplate(LettuceConnectionFactory redisConnectionFactory){
        RedisTemplate<String,Serializable> template = new RedisTemplate<String,Serializable>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());// Hash key序列化
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());// Hash value序列化
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }


    /*
    自定义 cache key 的生成方式
    解决spring默认的生成策略 当缓存键值的参数列表的值相同时返回结果一样
    造成获取到错误的缓存数据
     */
    @Bean
    public KeyGenerator myKeyGenerator(){
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                sb.append("&");
                for (Object obj : params) {
                    if (obj != null){
                        sb.append(obj.getClass().getName());
                        sb.append("&");
                        sb.append(JSON.toJSONString(obj));
                        sb.append("&");
                    }
                }
//                log.info("redis cache key str: "+sb.toString());
//                log.info("redis cache key sha256Hex: "+DigestUtils.sha256Hex(sb.toString()));
                return DigestUtils.sha256Hex(sb.toString());
            }
        };
    }

    /**
     * 缓存管理器
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        //初始化一个RedisCacheWriter
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        //设置CacheManager的值序列化方式为json序列化
        RedisSerializer<Object> jsonSerializer = new GenericJackson2JsonRedisSerializer();
        RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair
                .fromSerializer(jsonSerializer);
        RedisCacheConfiguration defaultCacheConfig=RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(pair);
        //设置默认超过期时间是30秒
        defaultCacheConfig.entryTtl(Duration.ofSeconds(30));
        //初始化RedisCacheManager
        return new RedisCacheManager(redisCacheWriter, defaultCacheConfig);
    }


}
