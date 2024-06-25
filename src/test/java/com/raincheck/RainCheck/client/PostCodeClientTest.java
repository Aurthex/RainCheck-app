package com.raincheck.RainCheck.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PostCodeClientTest {

    private PostCodeClient postCodeClient;

    @Mock
    private HttpClient mockClient;

    @Mock
    private HttpResponse<String> mockResponse;


    @Test
    public void testFetchJson_Success() throws Exception {
        MockitoAnnotations.openMocks(this);
        postCodeClient = new PostCodeClient("NE403BX");
        // Mock response
        String jsonResponse = "{ \"latitude\": 54.96628, \"longitude\": -1.75399, \"admin_district\": \"Gateshead\" }";
        when(mockResponse.body()).thenReturn(jsonResponse);
        when(mockClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(CompletableFuture.completedFuture(mockResponse));

        // Trigger fetchJson method
        postCodeClient.fetchJson();

        // Delay to ensure async processing completes (optional in some cases)
        Thread.sleep(1000);

        // Verify results
        assertEquals("54.96628", postCodeClient.getLatitude());
        assertEquals("-1.75399", postCodeClient.getLongitude());
        assertEquals("Gateshead", postCodeClient.getLocation());
    }
}