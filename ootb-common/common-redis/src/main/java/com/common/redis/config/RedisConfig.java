package com.common.redis.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName RedisConfig
 * @Description redis 配置
 **/

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
}
