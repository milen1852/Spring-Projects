package com.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserLogService {

    private final RestTemplate restTemplate;

    @Value("${log.service-url}")
    private String logUrl;

    @Async
    public void log(String userId, String loginStatus, String description){

        Map<String, Object> body = new HashMap<>();

        body.put("userId", userId);
        body.put("loginStatus", loginStatus);
        body.put("description", description);

        restTemplate.postForEntity(logUrl + "/api/logs" , body, Void.class);
    }
}
