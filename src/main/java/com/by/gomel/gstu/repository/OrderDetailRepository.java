package com.by.gomel.gstu.repository;

import com.by.gomel.gstu.model.Order;
import com.by.gomel.gstu.model.OrdersDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrdersDetail, Long> {

    List<OrdersDetail> getAllByOrder(Order order);
}
