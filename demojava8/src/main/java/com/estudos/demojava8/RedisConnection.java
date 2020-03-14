package com.estudos.demojava8;

import com.google.gson.Gson;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

import java.util.Optional;

@Configuration
public class RedisConnection {
    private Settings settings;

    public RedisConnection(Settings settings) {
        this.settings = settings;
    }

    public <T> Optional<T> checkRedis(String key, Class<T> model) {
        Jedis jedis = new Jedis(settings.getRedisHost(), settings.getRedisPort());
        jedis.auth(settings.getRedisAuth());

        String value = jedis.get(key);
        jedis.close();
        System.out.println("############### VALUE: " + value);

        return Optional.ofNullable(new Gson().fromJson(value, model));
    }

    public void saveRedis(String key, String value) {
        Jedis jedis = new Jedis(settings.getRedisHost(), settings.getRedisPort());
        jedis.auth(settings.getRedisAuth());
        //TODO: Precisa usar o pool
        jedis.set(key, value);
        jedis.close();
    }
}
