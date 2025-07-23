package com.example.analyzer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.analyzer.ReportGenerator.MetricReport;
import com.example.analyzer.config.ThresholdConfig;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MetricEvaluator {

    private final PrometheusClient prometheusClient;
    private final ThresholdConfig thresholdConfig;
    private final ReportGenerator reportGenerator;

    @Scheduled(cron = "0 0 2 * * *")
    public void runDailyAnalysis() {
        List<MetricReport> reports = new ArrayList<>();

        double errorLogs = prometheusClient.queryScalar("sum(increase(logback_events_total{level=\"ERROR\"}[1d]))");
        reports.add(new ReportGenerator.MetricReport("Error Logs", errorLogs,
                thresholdConfig.getMaxErrorLogsPerDay(),
                errorLogs > thresholdConfig.getMaxErrorLogsPerDay() ? "FAIL" : "OK"));

        double usedMemory = prometheusClient.queryScalar("avg(jvm_memory_used_bytes)");
        double maxMemory = prometheusClient.queryScalar("avg(jvm_memory_max_bytes)");
        double memUsage = (usedMemory / maxMemory) * 100;
        reports.add(new ReportGenerator.MetricReport("Memory Usage (%)", memUsage,
                thresholdConfig.getMemoryUsagePercent(),
                memUsage > thresholdConfig.getMemoryUsagePercent() ? "FAIL" : "OK"));

        double cpuUsage = prometheusClient.queryScalar("avg(rate(process_cpu_usage[1d])) * 100");
        reports.add(new ReportGenerator.MetricReport("CPU Usage (%)", cpuUsage,
                thresholdConfig.getCpuUsagePercent(), cpuUsage > thresholdConfig.getCpuUsagePercent() ? "FAIL" : "OK"));

        reportGenerator.generateCsvReport(reports);
    }
}
