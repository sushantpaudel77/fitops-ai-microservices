package com.fitops_microservices.activity_service;

import com.fitops_microservices.activity_service.model.Activity;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends MongoRepository<Activity, String> {
}
