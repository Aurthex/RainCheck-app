package com.raincheck.RainCheck.client;

import lombok.Getter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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


    // The PostCodeClient class constructor that takes a postcode as an argument
    // Set the BASE_URL to the URL of the API endpoint, appending the given postcode
    // Create a new HttpClient instance to be used for sending HTTP requests
    // Call the fetchJson method to perform the actual fetching of the JSON data from the API
    public PostCodeClient(String postcode) throws URISyntaxException, IOException, InterruptedException {
        BASE_URL= "https://api.postcodes.io/postcodes/" + postcode;
        client = HttpClient.newHttpClient();
        fetchJson();
    }

    // handle the process of sending an HTTP request to the API and fetching the JSON response
    void fetchJson() throws URISyntaxException, IOException, InterruptedException {

        // Create a new HttpRequest builder
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL))                         // Set the URI to the BASE_URL
                .GET()                                          // Specify that this is a GET request
                .build();                                       // Build the HttpRequest object

        // Send the request and store the response as a string
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Get the response body as a string
        // Call a method to extract latitude and longitude from the JSON response
        String json = response.body();
        extractLatLongFromJson(json);


    }
    // Extract the Status, Longitude and latitude from the Json string
    private void extractLatLongFromJson(String json) {

        // Status Check
        // Split the JSON string to get the part after "status" and extract the value
        String status = json.split("\"status\":")[1].split(",")[0].trim();
        if (!status.equals("200")) return;

        // Extract latitude
        // Split the JSON string to get the part after "latitude" and extract the value
        latitude = json.split("\"latitude\":")[1].split(",")[0].trim();
        latitude = latitude.substring(0, latitude.length() - 1); // Remove surrounding quotes

        // Extract longitude
        // Split the JSON string to get the part after "longitude" and extract the value
        longitude = json.split("\"longitude\":")[1].split(",")[0].trim();
        longitude = longitude.substring(0, longitude.length() - 1); // Remove surrounding quotes

        // Extract admin district
        // Split the JSON string to get the part after "admin_district" and extract the value
        location = json.split("\"admin_district\":")[1].split(",")[0].trim();
        location = location.substring(1, location.length() - 1); // Remove surrounding quotes

    }

}