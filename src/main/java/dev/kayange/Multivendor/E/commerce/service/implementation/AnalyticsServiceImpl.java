package dev.kayange.Multivendor.E.commerce.service.implementation;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsServiceImpl {
    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    public void aggregateDailyData() {
        // Aggregate and save daily analytics data...
    }

    @Scheduled(cron = "0 0 0 * * MON") // Runs every Monday at midnight
    public void aggregateWeeklyData() {
        // Aggregate and save weekly analytics data...
    }
}
