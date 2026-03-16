package com.example.service;

import com.example.entity.UserLog;
import com.example.repository.UserLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserLogService {

    private final UserLogRepository userLogRepository;

    public void saveLog(Map<String, Object> request) {

        UserLog userLog = convertToEntity(request);

        userLogRepository.save(userLog);
    }

    private UserLog convertToEntity(Map<String, Object> request){
        return UserLog.builder()
                .instructionId(UUID.randomUUID().toString())
                .userId(String.valueOf(request.get("userId").toString()))
                .loginStatus(request.get("loginStatus").toString())
                .description(request.get("description").toString())
                .logTime(LocalDateTime.now())
                .build();
    }
}
