package com.fitops_microservices.activity_service.repository;

import com.fitops_microservices.activity_service.model.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ActivityRepository extends MongoRepository<Activity, String> {
}
