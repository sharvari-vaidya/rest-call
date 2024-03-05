package com.example.main.config;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConf {

	  @Bean
	    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
	        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
	        redisTemplate.setConnectionFactory(connectionFactory);
	        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
	        return redisTemplate;
	    }

}
