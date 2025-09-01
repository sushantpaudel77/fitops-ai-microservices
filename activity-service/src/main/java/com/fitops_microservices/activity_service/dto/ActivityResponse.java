package com.fitops_microservices.activity_service.dto;

import com.fitops_microservices.activity_service.model.ActivityType;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ActivityResponse {
    private String id;
    private String userId;
    private ActivityType type;
    private Integer duration;
    private Integer caloriesBurned;
    private LocalDateTime startTime;
    private Map<String, Object> additionalMetrics;  @CreatedDate
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}
