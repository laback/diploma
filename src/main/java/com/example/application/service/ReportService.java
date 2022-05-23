package com.example.application.service;

import com.example.application.excel.ExcelSaver;
import com.example.application.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ReportService {

    private final OrderRepository orderRepository;

    public ReportService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void savePeriodReport(LocalDateTime beginDate, LocalDateTime endDate) throws IOException {
        ExcelSaver.savePeriodOrderReport(orderRepository.getAllByCreatedBetween(beginDate, endDate));
    }
}
