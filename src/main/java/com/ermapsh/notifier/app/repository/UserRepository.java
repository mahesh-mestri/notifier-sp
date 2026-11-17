package com.ermapsh.notifier.app.repository;

import com.ermapsh.notifier.app.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(@Email(message = "Email should be valid") @NotBlank(message="Email is required") String email);
}