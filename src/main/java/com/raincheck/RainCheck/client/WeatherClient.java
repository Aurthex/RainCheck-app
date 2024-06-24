package com.raincheck.RainCheck.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Date;

public class WeatherClient {

    private final String BASE_URL;
    private final HttpClient client;

    public WeatherClient(String latitude, String longitude){
        BASE_URL = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "&current=temperature_2m,weather_code,wind_speed_10m";
        client = HttpClient.newHttpClient();
    }

    public WeatherClient(String latitude, String longitude, Date date){
        BASE_URL = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "&daily=weather_code,temperature_2m_max,wind_speed_10m_max&timezone=Europe%2FLondon&start_date=" + date.toString() + "&end_date=" + date.toString();
        client = HttpClient.newHttpClient();
    }

    public int checkStatusCode() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode();
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

    public String findDaily() throws IOException, InterruptedException {
        String raw = findAll();
        int start = raw.indexOf("\"daily\":") + 8;
        int end = raw.indexOf("}}") + 1;
        return raw.substring(start, end);
    }
}
