package org.example.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService {

    @Autowired
    JedisPool jedisPool;



    //获取当个对象
    public <T> T get(KeyPrefix keyPrefix, String key, Class<T> clazz){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            //真正的key
            String realKey = keyPrefix.getPrefix()+key;
            String  str = jedis.get(realKey);
            T t = stringToBean(str,clazz);
            return t;
        }finally {
            returnToPool(jedis);
        }
    }

    //设置对象
    public <T> boolean set(KeyPrefix keyPrefix, String key, T value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String str = beanToString(value);
            if(str == null || str.length() <= 0){
                return false;
            }
            String realKey = keyPrefix.getPrefix()+key;
            int seconds = keyPrefix.expireSeconds();
            if(seconds <= 0){
                jedis.set(realKey,str);
            }else{
                jedis.setex(realKey,seconds,str);
            }
            return true;
        }finally {
            returnToPool(jedis);
        }
    }


    //判断key是否存在
    public <T> boolean exists(KeyPrefix keyPrefix, String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix()+key;
            return jedis.exists(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    //删除
    public  boolean delete(KeyPrefix keyPrefix, String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix()+key;
            long ret =  jedis.del(key);
            return ret > 0;
        }finally {
            returnToPool(jedis);
        }
    }


    //增加
    public <T> Long incr(KeyPrefix keyPrefix, String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix()+key;
            return jedis.incr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    //减少
    public <T> Long decr(KeyPrefix keyPrefix, String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix()+key;
            return jedis.decr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }


    public static  <T> String beanToString(T value) {
        if(value == null){
            return null;
        }
        Class<?> clazz = value.getClass();
        if(clazz == int.class ||clazz == Integer.class){
            return ""+value;
        }else if(clazz == String.class){
            return (String)value;
        }else if(clazz == long.class || clazz == Long.class){
            return ""+value;
        }else{
            return JSON.toJSONString(value);
        }
    }

    public static  <T> T stringToBean(String str,Class<T> clazz) {
        if(str == null || str.length() <= 0){
            return null;
        }
        if(clazz == int.class ||clazz == Integer.class){
            return (T) Integer.valueOf(str);
        }else if(clazz == String.class){
            return (T) str;
        }else if(clazz == long.class || clazz == Long.class){
            return (T) Long.valueOf(str);
        }else{
            return JSON.toJavaObject(JSON.parseObject(str),clazz);
        }
    }

    private void returnToPool(Jedis jedis) {
        if(jedis!=null){
            jedis.close();
        }
    }
}
