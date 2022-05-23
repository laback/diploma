package com.example.application.repository;

import com.example.application.model.Detail;
import com.example.application.model.Order;
import com.example.application.model.OrdersDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrdersDetail, Long> {

    List<OrdersDetail> getAllByOrder(Order order);
}
