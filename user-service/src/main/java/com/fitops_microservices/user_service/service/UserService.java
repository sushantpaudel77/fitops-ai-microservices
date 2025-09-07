package com.fitops_microservices.user_service.service;

import com.fitops_microservices.user_service.dto.UserRequest;
import com.fitops_microservices.user_service.dto.UserResponse;

import java.util.Optional;

public interface UserService {

    UserResponse register(UserRequest userRequest);

    UserResponse getUserProfile(String userId);

    boolean validateUser(String keycloakId);

    Optional<UserResponse> findByKeycloakId(String keycloakId);

    Optional<UserResponse> findByEmail(String email);
}