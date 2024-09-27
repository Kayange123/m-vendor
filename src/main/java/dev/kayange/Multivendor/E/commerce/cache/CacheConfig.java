package dev.kayange.Multivendor.E.commerce.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean
    public CacheStore<String, Integer> userCache(){
        return new CacheStore<>(900, TimeUnit.SECONDS);
    }
}
