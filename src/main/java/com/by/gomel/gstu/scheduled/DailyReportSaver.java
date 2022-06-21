package com.by.gomel.gstu.scheduled;

import com.by.gomel.gstu.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DailyReportSaver {

    private final ReportService reportService;

    @Autowired
    public DailyReportSaver(ReportService reportService) {
        this.reportService = reportService;
    }

    @Scheduled(cron = "0 0 18 * * *")
    public void saveDailyReport() throws IOException {
        reportService.saveDailyReports();
    }
}
