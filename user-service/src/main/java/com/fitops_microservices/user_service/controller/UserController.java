package com.fitops_microservices.user_service.controller;


import com.fitops_microservices.user_service.dto.UserRequest;
import com.fitops_microservices.user_service.dto.UserResponse;
import com.fitops_microservices.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable String userId) {
        UserResponse user = userService.getUserProfile(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserRequest userRequest)  {
        UserResponse user = userService.register(userRequest);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    // inter-service communication endpoint
    @GetMapping("/{userId}/validate")
    public ResponseEntity<Boolean> validateUser(@PathVariable String userId) {
        boolean isValid = userService.validateUser(userId);
        return ResponseEntity.ok(isValid);
    }
}
