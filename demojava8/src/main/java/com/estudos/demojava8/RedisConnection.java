package com.estudos.demojava8;

import com.google.gson.Gson;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Optional;

@Configuration
public class RedisConnection {
    private Settings settings;
    JedisPool pool = new JedisPool(new JedisPoolConfig(),settings.getRedisHost());

    public RedisConnection(Settings settings) {
        this.settings = settings;
    }

    public <T> Optional<T> checkRedis(String key, Class<T> model) {
        String value = null;
        try (Jedis jedis = pool.getResource()) {
            jedis.auth(settings.getRedisAuth());
            value = jedis.get(key);
            System.out.println("############### VALUE: " + value);
        }
        pool.close();
        return Optional.ofNullable(new Gson().fromJson(value, model));
    }

    public void saveRedis(String key, String value) {
        try (Jedis jedis = pool.getResource()) {
            jedis.auth(settings.getRedisAuth());
            jedis.set(key, value);
        }
        pool.close();
    }

    public void saveListRedis(String listKey, String value){
        try (Jedis jedis = pool.getResource()) {
            jedis.auth(settings.getRedisAuth());
            Transaction t = jedis.multi();
            t.append(listKey, value);
        }
        pool.close();

    }
}
