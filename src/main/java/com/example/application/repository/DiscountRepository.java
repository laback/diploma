package com.example.application.repository;

import com.example.application.model.Discount;
import com.example.application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

    Optional<Discount> findDiscountByUser(User user);
}
