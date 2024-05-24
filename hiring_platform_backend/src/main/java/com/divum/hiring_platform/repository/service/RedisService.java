package com.divum.hiring_platform.repository.service;

public interface RedisService {

    void addAccessToken(String accessToken);

    void addRefreshToken(String refreshToken);

    String getAccessToken();

    String getRefreshToken();
}
