package com.fitops_microservices.activity_service.service;

import com.fitops_microservices.activity_service.dto.ActivityRequest;
import com.fitops_microservices.activity_service.dto.ActivityResponse;
import com.fitops_microservices.activity_service.model.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityServiceImpl {

    private final ActivityRepository activityRepository;

    public ActivityResponse trackActivity(ActivityRequest activityRequest) {
        Activity activity = Activity.builder()
                .userId(activityRequest.getUserId())
                .type(activityRequest.getType())
                .duration(activityRequest.getDuration())
                .caloriesBurned(activityRequest.getCaloriesBurned())
                .startTime(activityRequest.getStartTime())
                .additionalMetrics(activityRequest.getAdditionalMetrics())
                .build();

       Activity savedActivity = activityRepository.save(activity);
       return mapToResponse(savedActivity);
    }

    public ActivityResponse mapToResponse(Activity activity) {
        ActivityResponse activityResponse = new ActivityResponse();
        activityResponse.setId(activity.getId());
        activityResponse.setUserId(activity.getUserId());
        activityResponse.setType(activity.getType());
        activityResponse.setDuration(activity.getDuration());
        activityResponse.setCaloriesBurned(activity.getCaloriesBurned());
        activityResponse.setStartTime(activity.getStartTime());
        activityResponse.setAdditionalMetrics(activity.getAdditionalMetrics());
        activityResponse.setCreatedAt(activity.getCreatedAt());
        activityResponse.setUpdateAt(activity.getUpdateAt());
        return activityResponse;
    }
}
