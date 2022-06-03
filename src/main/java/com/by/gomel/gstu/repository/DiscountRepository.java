package com.by.gomel.gstu.repository;

import com.by.gomel.gstu.model.Discount;
import com.by.gomel.gstu.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

    Optional<Discount> findDiscountByUser(User user);
}
