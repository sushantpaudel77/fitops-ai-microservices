package com.fitops_microservices.user_service.service;

import com.fitops_microservices.user_service.dto.UserRequest;
import com.fitops_microservices.user_service.dto.UserResponse;
import com.fitops_microservices.user_service.model.User;
import com.fitops_microservices.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl {

    private final UserRepository userRepository;

    public UserResponse register(UserRequest userRequest)  {

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            User existingUser = userRepository.findByEmail(userRequest.getEmail());
            UserResponse response = new UserResponse();

            response.setId(existingUser.getId());
            response.setPassword(existingUser.getPassword());
            response.setEmail(existingUser.getEmail());
            response.setFirstName(existingUser.getFirstName());
            response.setLastName(existingUser.getLastName());
            response.setCreatedAt(existingUser.getCreatedAt());
            response.setUpdatedAt(existingUser.getUpdatedAt());

            return response;
        }

        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setPassword(userRequest.getPassword());

       User savedUser = userRepository.save(user);
       UserResponse response = new UserResponse();

       response.setId(savedUser.getId());
       response.setPassword(savedUser.getPassword());
       response.setEmail(savedUser.getEmail());
       response.setFirstName(savedUser.getFirstName());
       response.setLastName(savedUser.getLastName());
       response.setCreatedAt(savedUser.getCreatedAt());
       response.setUpdatedAt(savedUser.getUpdatedAt());

       return response;
    }

    public UserResponse getUserProfile(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with the ID: " + userId));

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setPassword(user.getPassword());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());

        return  response;
    }

    public boolean validateUser(String userId) {
        return userRepository.existsByKeyCloakId(userId);
    }
}
