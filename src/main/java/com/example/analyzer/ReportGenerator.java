package com.example.analyzer;
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
                writer.writeNext(new String[]{r.metric, String.valueOf(r.value), String.valueOf(r.threshold), r.status});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
