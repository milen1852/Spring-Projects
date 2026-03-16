package com.example.controller;

import com.example.service.UserLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserLogController {

    private final UserLogService userLogService;

    @PostMapping("/logs")
    public void save(@RequestBody Map<String, Object> request){

        userLogService.saveLog(request);
    }
}
