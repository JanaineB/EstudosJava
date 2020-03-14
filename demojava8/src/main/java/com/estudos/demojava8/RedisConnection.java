package com.estudos.demojava8;

import com.google.gson.Gson;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

import java.util.Optional;

@Configuration
public class RedisConnection {
    //TODO: extrair metodos redis para uma classe redis
    public <T> Optional<T> checkRedis(String key, Class<T> model) {
        //TODO: Abstrair auth para env var
        Jedis jedis = new Jedis("localhost", 6379);
        jedis.auth("123");

        String value = jedis.get(key);
        jedis.close();
        System.out.println("############### VALUE: " + value);

        return Optional.ofNullable(new Gson().fromJson(value, model));
    }

    public void saveRedis(String key, String value) {
        Jedis jedis = new Jedis("localhost", 6379);
        jedis.auth("123");
        //TODO: Precisa usar o pool
        jedis.set(key, value);
    }
}
