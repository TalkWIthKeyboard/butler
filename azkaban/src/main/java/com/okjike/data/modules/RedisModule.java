package com.okjike.data.modules;

import com.okjike.data.models.AzkabanConfig;
import org.mapstruct.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.Duration;

@Configuration
public class RedisModule {

    @Bean
    Duration providesRedisTimeout(AzkabanConfig azkabanConfig) {
        return Duration.ofMillis(azkabanConfig.isDebug ? 2000 : 300);
    }

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Discount {

    }

    @Discount
    @Bean
    JedisPool providesDefaultJedisPool(AzkabanConfig azkabanConfig, Duration duration) {
        return constructJedisPool(azkabanConfig.azkabanRedisHost,
            azkabanConfig.azkabanRedisPort, duration);
    }

    private JedisPool constructJedisPool(String host, int port, Duration timeout) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(256);
        return new JedisPool(jedisPoolConfig, host, port, (int) timeout.toMillis());
    }
}
