package com.delivery.service;

import com.delivery.dto.Point;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class GeocodingService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Cacheable(value = "geocoding", key = "#address")
    public Point getCoordinates(String address) {
        try {
            String encoded = URLEncoder.encode(address, StandardCharsets.UTF_8);
            String url = "https://nominatim.openstreetmap.org/search?q=" + encoded + "&format=json&limit=1";

            String response = restTemplate.getForObject(url, String.class);
            JsonNode node = objectMapper.readTree(response);

            if (node.isArray() && node.size() > 0) {
                double lat = node.get(0).get("lat").asDouble();
                double lon = node.get(0).get("lon").asDouble();
                return new Point(lat, lon);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to get coordinates for address: " + address);
        }

        throw new RuntimeException("Address not found: " + address);
    }
}
