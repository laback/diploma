package com.example.application.controller;

import com.example.application.service.ReportService;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.time.LocalDateTime;

@Controller
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("user/report")
    public String getReportPage(){



        return "reports/index";
    }

    @GetMapping("user/report/periodOrderReport")
    public String savePeriodOrderReport(@Param(value = "beginDate") String beginDate, @Param(value = "endDate") String endDate) throws IOException {
        reportService.savePeriodReport(LocalDateTime.parse(beginDate), LocalDateTime.parse(endDate));

        return "redirect:/user/report";
    }
}
