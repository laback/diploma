package com.by.gomel.gstu.repository;

import com.by.gomel.gstu.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    Optional<User> findUserByEmail(String email);
}
