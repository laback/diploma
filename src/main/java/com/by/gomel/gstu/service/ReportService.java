package com.by.gomel.gstu.service;

import com.by.gomel.gstu.excel.ExcelSaver;
import com.by.gomel.gstu.model.User;
import com.by.gomel.gstu.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class ReportService {

    private final OrderRepository orderRepository;
    private final UserService userService;

    @Autowired
    public ReportService(OrderRepository orderRepository, UserService userService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
    }

    public void savePeriodReport(LocalDateTime beginDate, LocalDateTime endDate) throws IOException {
        ExcelSaver.savePeriodOrderReport(orderRepository.getAllByCreatedBetween(beginDate, endDate), (User) userService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    public void saveDetailsSalesByPeriod(LocalDateTime beginDate, LocalDateTime endDate){

    }
}
