package com.fitops_microservices.user_service.repository;

import com.fitops_microservices.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByKeycloakId(String keycloakId);
    boolean existsByKeycloakId(String keycloakId);

    // Better to return Optional for consistency
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}