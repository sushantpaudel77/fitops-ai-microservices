package com.fitops_microservices.activity_service.controller;

import com.fitops_microservices.activity_service.dto.ActivityRequest;
import com.fitops_microservices.activity_service.dto.ActivityResponse;
import com.fitops_microservices.activity_service.service.ActivityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/activities")
@RequiredArgsConstructor
public class ActivityController {

private final ActivityServiceImpl activityService;

    @PostMapping
    public ResponseEntity<ActivityResponse> tractActivity(@RequestBody ActivityRequest activityRequest) {
        return ResponseEntity.ok(activityService.trackActivity(activityRequest));
    }
}
