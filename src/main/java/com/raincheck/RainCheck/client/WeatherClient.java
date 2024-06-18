package com.raincheck.RainCheck.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherClient {

    private final String BASE_URL;
    private final HttpClient client;

    public WeatherClient(String latitude, String longitude){
        BASE_URL = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "&current=temperature_2m,weather_code,wind_speed_10m";
        client = HttpClient.newHttpClient();
    }

    public String findAll() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String findCurrent() throws IOException, InterruptedException {
        String raw = findAll();
        int start = raw.indexOf("\"current\":") + 10;
        int end = raw.indexOf("}}") + 1;
        return raw.substring(start, end);
    }
}
