package com.divum.hiring_platform.repository.service.impl;

import com.divum.hiring_platform.repository.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    public static  final String HASH_KEY = "GOOGLEOAUTHTOKENS";

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate template;

    public void addAccessToken(String accessToken){
        template.opsForHash().put(HASH_KEY,"accessToken",accessToken);
    }

    public void addRefreshToken(String refreshToken){
        template.opsForHash().put(HASH_KEY,"refreshToken",refreshToken);
    }

    public String getAccessToken(){
        return (String) template.opsForHash().get(HASH_KEY,"accessToken");
    }

    public String getRefreshToken(){
        return (String) template.opsForHash().get(HASH_KEY,"refreshToken");
    }
}
