package com.mawkun.core.base.service;


import cn.pertech.common.utils.JsonUtils;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import com.xiaoleilu.hutool.json.JSONObject;
import com.xiaoleilu.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CacheService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    public CacheService() {
    }

    public <T> void put(String key, T obj) {
        this.redisTemplate.opsForValue().set(key, JsonUtils.toString(obj));
    }

    public <T> void put(String key, T obj, int timeout) {
        this.put(key, obj, timeout, TimeUnit.MINUTES);
    }

    public <T> void put(String key, T obj, int timeout, TimeUnit unit) {
        this.redisTemplate.opsForValue().set(key, JsonUtils.toString(obj), (long)timeout, unit);
    }

    public <T> T get(String key, Class<T> cls) {
        return JsonUtils.toJavaObject(JsonUtils.asJSONObject((String)this.redisTemplate.opsForValue().get(key)), cls);
    }

    public <T> List<T> getList(String key, Class<T> cls) {
        return JsonUtils.toJavaList(JsonUtils.asJSONArray((String)this.redisTemplate.opsForValue().get(key)), cls);
    }

    public <T> T putIfAbsent(String key, Class<T> cls, Supplier<T> supplier) {
        T t = this.get(key, cls);
        if (null == t) {
            t = supplier.get();
            if (null != t) {
                this.put(key, t);
            }
        }

        return t;
    }

    public <T> T putIfAbsent(String key, Class<T> cls, Supplier<T> supplier, int timeout) {
        T t = this.get(key, cls);
        if (null == t) {
            t = supplier.get();
            if (null != t) {
                this.put(key, t, timeout);
            }
        }

        return t;
    }

    public boolean exists(String key) {
        return this.redisTemplate.hasKey(key);
    }

    public void del(String key) {
        this.redisTemplate.delete(key);
    }

    public boolean expire(String key, long timeout, TimeUnit unit) {
        return this.redisTemplate.expire(key, timeout, unit);
    }

    public boolean expire(String key, long timeout) {
        return this.redisTemplate.expire(key, timeout, TimeUnit.MINUTES);
    }

    public void put(String key, String value) {
        this.redisTemplate.opsForValue().set(key, value);
    }

    public void put(String key, String value, int timeout) {
        this.put(key, value, timeout, TimeUnit.MINUTES);
    }

    public void put(String key, String value, int timeout, TimeUnit unit) {
        this.redisTemplate.opsForValue().set(key, value, (long)timeout, unit);
    }

    public String get(String key) {
        return (String)this.redisTemplate.opsForValue().get(key);
    }

    public void putHash(String key, Map<Object, Object> m) {
        this.redisTemplate.opsForHash().putAll(key, m);
    }

    public Map<Object, Object> getHash(String key) {
        try {
            return this.redisTemplate.opsForHash().entries(key);
        } catch (Exception var3) {
            return null;
        }
    }

    public int clearDb() {
        Set<String> keys = this.redisTemplate.keys("*");
        int count = keys != null ? keys.size() : 0;
        if (count == 0) {
            return 0;
        } else {
            this.redisTemplate.delete(keys);
            return keys.size();
        }
    }
}
