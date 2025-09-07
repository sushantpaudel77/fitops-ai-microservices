package com.fitops_microservices.user_service.repository;

import com.fitops_microservices.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);
    boolean existsByKeyCloakId(String userId);
    User findByEmail(String email);
}
