package com.fitops_microservices.activity_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserValidationService {

    private final WebClient userServiceWebClient;

    public boolean validateUser(String userId) {
        try {
            Boolean isValid = userServiceWebClient.get()
                    .uri("/api/users/{userId}/validate", userId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block(); // blocking call, only use in synchronous code

            return Boolean.TRUE.equals(isValid);
        } catch (WebClientException exception) {
            log.error("Error calling user service for userId {}: {}", userId, exception.getMessage());
            throw new RuntimeException("Error in WebClient", exception);
        }
    }
}

