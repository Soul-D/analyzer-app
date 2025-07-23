package com.example.analyzer;
@Component
public class PrometheusClient {
    @Value("${prometheus.url}")
    private String prometheusUrl;
    private final ObjectMapper mapper = new ObjectMapper();
    public double queryScalar(String query) {
        try {
            String url = prometheusUrl + "/api/v1/query?query=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
            String response = HttpClient.newHttpClient()
                .send(HttpRequest.newBuilder(URI.create(url)).GET().build(), HttpResponse.BodyHandlers.ofString())
                .body();
            JsonNode root = mapper.readTree(response);
            JsonNode result = root.path("data").path("result");
            if (result.isArray() && result.size() > 0) {
                return result.get(0).path("value").get(1).asDouble();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
