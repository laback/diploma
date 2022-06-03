package com.by.gomel.gstu.controller;

import com.by.gomel.gstu.service.ReportService;
import com.by.gomel.gstu.service.RoleService;
import com.by.gomel.gstu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Controller
public class ReportController {

    private final ReportService reportService;
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public ReportController(ReportService reportService, UserService userService, RoleService roleService) {
        this.reportService = reportService;
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("user/report")
    public String getReportPage(Model model){

        var employeeId = roleService.getEmployeeRoleId();
        var adminId = roleService.getAdminRoleId();

        model.addAttribute("users", userService.getAll().stream().filter(user -> user.getRole().getId() == employeeId || user.getRole().getId() == adminId).collect(Collectors.toList()));


        return "reports/index";
    }

    @GetMapping("user/report/periodOrderReport")
    public String savePeriodOrderReport(@Param(value = "beginDate") String beginDate, @Param(value = "endDate") String endDate) throws IOException {
        reportService.savePeriodReport(LocalDateTime.parse(beginDate), LocalDateTime.parse(endDate));

        return "redirect:/user/report";
    }

    @GetMapping("user/report/periodDetailReport")
    public String savePeriodDetailReport(@Param(value = "beginDate") String beginDate, @Param(value = "endDate") String endDate) throws IOException {
        reportService.saveDetailsSalesByPeriod(LocalDateTime.parse(beginDate), LocalDateTime.parse(endDate));

        return "redirect:/user/report";
    }


    @GetMapping("user/report/employeeSalesReport")
    public String saveEmployeeSalesReport(@Param(value = "beginDate") String beginDate, @Param(value = "endDate") String endDate, @Param(value = "user") String user) throws IOException {
        reportService.saveEmployeePeriodReport(LocalDateTime.parse(beginDate), LocalDateTime.parse(endDate), Long.valueOf(user));

        return "redirect:/user/report";
    }
}
