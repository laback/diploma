package com.by.gomel.gstu.repository;

import com.by.gomel.gstu.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> getAllByPlannedIssueDate(LocalDate plannedIssueDate);

    List<Order> getAllByCreatedBetween(LocalDateTime beginDate, LocalDateTime endDate);
}
