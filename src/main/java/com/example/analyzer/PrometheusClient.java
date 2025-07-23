package com.example.analyzer;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
            if (result.isArray() && !result.isEmpty()) {
                return result.get(0).path("value").get(1).asDouble();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
