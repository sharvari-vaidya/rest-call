package com.example.main.serviceImpl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.main.service.RedisService;


@Service
public class RedisServiceImpl implements RedisService{

	@Value("${auth_token_expiry}")
	private int authTokenExpiry;
	
	
	private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }
    
    @Override
    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

	@Override
	public void setExpiry(String key) {
    	redisTemplate.expire(key, authTokenExpiry,TimeUnit.MINUTES);
	}

	@Override
	public void removeUser(String key) {
		redisTemplate.delete(key);
		
	}


}
