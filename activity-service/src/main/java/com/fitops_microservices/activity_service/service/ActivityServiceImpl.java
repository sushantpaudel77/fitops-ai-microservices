package com.fitops_microservices.activity_service.service;

import com.fitops_microservices.activity_service.dto.ActivityRequest;
import com.fitops_microservices.activity_service.dto.ActivityResponse;
import com.fitops_microservices.activity_service.model.Activity;
import com.fitops_microservices.activity_service.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityServiceImpl {

    @Value("${kafka.topic.name}")
    private String topicName;

    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;
    private final KafkaTemplate<String, Activity> kafkaTemplate;

    public ActivityResponse trackActivity(ActivityRequest activityRequest) {

       boolean isValidUSer = userValidationService.validateUser(activityRequest.getUserId());

        if (!isValidUSer) throw new RuntimeException("Invalid User: " + activityRequest.getUserId());

        Activity activity = Activity.builder()
                .userId(activityRequest.getUserId())
                .type(activityRequest.getType())
                .duration(activityRequest.getDuration())
                .caloriesBurned(activityRequest.getCaloriesBurned())
                .startTime(activityRequest.getStartTime())
                .additionalMetrics(activityRequest.getAdditionalMetrics())
                .build();

       Activity savedActivity = activityRepository.save(activity);

       try {
           kafkaTemplate.send(topicName, savedActivity.getUserId(), savedActivity);
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
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
