package com.raincheck.RainCheck.client;

import lombok.Getter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PostCodeClient {

    private final String BASE_URL;
    private final HttpClient client;
    @Getter
    private String latitude;
    @Getter
    private String longitude;
    @Getter
    private String location;

    public PostCodeClient(String postcode) {
        BASE_URL= "https://api.postcodes.io/postcodes/" + postcode;
        client = HttpClient.newHttpClient();
        fetchJson();
    }

    private void fetchJson() {
        try {
            // Build the request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL))
                    .GET()
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Extract latitude and longitude from the JSON response
            String json = response.body();
            extractLatLongFromJson(json);

        } catch (Exception e) {
            e.printStackTrace(); // Replace with proper logging
        }
    }

    private void extractLatLongFromJson(String json) {
        try {
            // Extract latitude
            latitude = json.split("\"latitude\":")[1].split(",")[0].trim();
            latitude = latitude.substring(0, latitude.length() - 1); // Remove surrounding quotes

            // Extract longitude
            longitude = json.split("\"longitude\":")[1].split(",")[0].trim();
            longitude = longitude.substring(0, longitude.length() - 1); // Remove surrounding quotes

            // Extract admin district
            location = json.split("\"admin_district\":")[1].split(",")[0].trim();
            location = location.substring(1, location.length() - 1);

        } catch (Exception e) {
            e.printStackTrace(); // Replace with proper logging
        }
    }

}