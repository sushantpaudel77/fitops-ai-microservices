package com.fitops_microservices.user_service.service;

import com.fitops_microservices.user_service.dto.UserRequest;
import com.fitops_microservices.user_service.dto.UserResponse;
import com.fitops_microservices.user_service.exception.UserNotFoundException;
import com.fitops_microservices.user_service.model.User;
import com.fitops_microservices.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse register(UserRequest userRequest) {
        log.info("Attempting to register user with email: {}", userRequest.getEmail());

        Optional<User> existingUser = findUserByEmail(userRequest.getEmail());
        if (existingUser.isPresent()) {
            log.info("User already exists with email: {}, returning existing user", userRequest.getEmail());
            return mapToUserResponse(existingUser.get());
        }

        User newUser = createUserFromRequest(userRequest);
        User savedUser = userRepository.save(newUser);

        log.info("Successfully registered new user with ID: {}", savedUser.getId());
        return mapToUserResponse(savedUser);
    }

    @Override
    public UserResponse getUserProfile(String userId) {
        log.info("Retrieving user profile for ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new UserNotFoundException("User not found with ID: " + userId);
                });

        return mapToUserResponse(user);
    }

    @Override
    public boolean validateUser(String keycloakId) {
        log.debug("Validating user with Keycloak ID: {}", keycloakId);

        boolean exists = userRepository.existsByKeycloakId(keycloakId);

        log.debug("User validation result for Keycloak ID {}: {}", keycloakId, exists);
        return exists;
    }

    @Override
    public Optional<UserResponse> findByKeycloakId(String keycloakId) {
        log.debug("Finding user by Keycloak ID: {}", keycloakId);

        return userRepository.findByKeycloakId(keycloakId)
                .map(this::mapToUserResponse);
    }

    @Override
    public Optional<UserResponse> findByEmail(String email) {
        log.debug("Finding user by email: {}", email);

        return findUserByEmail(email)
                .map(this::mapToUserResponse);
    }

    // Private helper methods

    private Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private User createUserFromRequest(UserRequest userRequest) {
        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setKeycloakId(userRequest.getKeycloakId());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setPassword(userRequest.getPassword());
        return user;
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setKeycloakId(user.getKeycloakId());
        response.setPassword(user.getPassword());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}