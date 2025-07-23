package com.example.analyzer.config;
@Component
@ConfigurationProperties(prefix = "thresholds")
@Data
public class ThresholdConfig {
    private double maxErrorLogsPerDay;
    private double memoryUsagePercent;
    private double cpuUsagePercent;
}
