package com.by.gomel.gstu.service;

import com.by.gomel.gstu.excel.ExcelSaver;
import com.by.gomel.gstu.model.User;
import com.by.gomel.gstu.repository.OrderDetailRepository;
import com.by.gomel.gstu.repository.OrderRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class ReportService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final OrderDetailRepository orderDetailRepository;

    @Autowired
    public ReportService(OrderRepository orderRepository, UserService userService, OrderDetailRepository orderDetailRepository) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.orderDetailRepository = orderDetailRepository;
    }

    public void saveDailyReports() throws IOException {

        var now = LocalDateTime.now();

        savePeriodReport(now.minusHours(8), now);
        saveDetailsSalesByPeriod(now.minusHours(8), now);
    }

    public void savePeriodReport(LocalDateTime beginDate, LocalDateTime endDate) throws IOException {
        ExcelSaver.savePeriodOrderReport(orderRepository.getAllByCreatedBetween(beginDate, endDate), (User) userService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    public void saveDetailsSalesByPeriod(LocalDateTime beginDate, LocalDateTime endDate) throws IOException {
        ExcelSaver.saveDetailsSalesReport(orderDetailRepository.getForSailedDetailsReport(beginDate, endDate),(User) userService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    public void saveEmployeePeriodReport(LocalDateTime beginDate, LocalDateTime endDate, Long userId) throws IOException {
        ExcelSaver.saveEmployeePeriodReport(orderRepository.getAllByCreatedBetweenAndEmployee(
                beginDate,
                endDate,
                userService.getById(userId)
        ),(User) userService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                );
    }
}
