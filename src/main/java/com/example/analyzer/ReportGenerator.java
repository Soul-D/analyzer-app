package com.example.analyzer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

import com.opencsv.CSVWriter;

@Component
public class ReportGenerator {

    public static class MetricReport {

        public String metric;
        public double value;
        public double threshold;
        public String status;

        public MetricReport(String metric, double value, double threshold, String status) {
            this.metric = metric;
            this.value = value;
            this.threshold = threshold;
            this.status = status;
        }
    }

    public void generateCsvReport(List<MetricReport> reports) {
        try (CSVWriter writer = new CSVWriter(new FileWriter("daily_report.csv"))) {
            writer.writeNext(new String[]{"Metric", "Value", "Threshold", "Status"});
            for (MetricReport r : reports) {
                writer.writeNext(
                        new String[]{r.metric, String.valueOf(r.value), String.valueOf(r.threshold), r.status});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
