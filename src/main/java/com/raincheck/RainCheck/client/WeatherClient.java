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


    // WeatherClient class constructor that takes latitude and longitude as arguments
    // Initialize the BASE_URL with the API endpoint URL, appending latitude and longitude as query parameters
    // Create a new HttpClient instance to be used for sending HTTP requests
    public WeatherClient(String latitude, String longitude){
        BASE_URL = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "&current=temperature_2m,weather_code,wind_speed_10m";
        client = HttpClient.newHttpClient();
    }

    // WeatherClient class constructor that takes latitude, longitude, and date as arguments
    // Initialize the BASE_URL with the API endpoint URL
    // Append latitude and longitude as query parameters
    // Specify daily weather data (weather_code, temperature_2m_max, wind_speed_10m_max)
    // Set the timezone to Europe/London
    // Set the start_date and end_date to the provided date
    // Create a new HttpClient instance to be used for sending HTTP requests
    public WeatherClient(String latitude, String longitude, Date date){
        BASE_URL = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "&daily=weather_code,temperature_2m_max,wind_speed_10m_max&timezone=Europe%2FLondon&start_date=" + date.toString() + "&end_date=" + date.toString();
        client = HttpClient.newHttpClient();
    }

    // Method to check the status code of the HTTP response
    public int checkStatusCode() throws IOException, InterruptedException {
        // Build the HTTP GET request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))       // Set the URI to the BASE_URL
                .GET()                           // Specify that this is a GET request
                .build();                        // Build the HttpRequest object
        // Send the request and get the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // Return the HTTP status code from the response
        return response.statusCode();
    }

    // Method to send an HTTP GET request and return the response body as a string
    public String findAll() throws IOException, InterruptedException {
        // Build the HTTP GET request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))      // Set the URI to the BASE_URL
                .GET()                          // Specify that this is a GET request
                .build();                       // Build the HttpRequest object
        // Send the request and get the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // Return the response body
        return response.body();
    }

    // Method to extract and return the current weather information from the JSON response
    // Call the findAll method to get the full JSON response as a string
    // Find the starting index of the "current" section in the JSON response and add 10 to move past the "current": part
    // Find the ending index of the "current" section in the JSON response and add 1 to include the closing brace
    // Extract and return the "current" weather information as a substring
    public String findCurrent() throws IOException, InterruptedException {
        String raw = findAll();
        int start = raw.indexOf("\"current\":") + 10;
        int end = raw.indexOf("}}") + 1;
        return raw.substring(start, end);
    }

    // Method to extract and return the daily weather information from the JSON response
    // Call the findAll method to get the full JSON response as a string
    // Find the starting index of the "daily" section in the JSON response and add 8 to move past the "daily": part
    // Find the ending index of the "daily" section in the JSON response and add 1 to include the closing brace
    // Extract and return the "daily" weather information as a substring
    public String findDaily() throws IOException, InterruptedException {
        String raw = findAll();
        int start = raw.indexOf("\"daily\":") + 8;
        int end = raw.indexOf("}}") + 1;
        return raw.substring(start, end);
    }
}
