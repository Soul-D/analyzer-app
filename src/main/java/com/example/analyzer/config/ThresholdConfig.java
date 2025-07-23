package com.example.analyzer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "thresholds")
@Data
public class ThresholdConfig {

    private double maxErrorLogsPerDay;
    private double memoryUsagePercent;
    private double cpuUsagePercent;
}
