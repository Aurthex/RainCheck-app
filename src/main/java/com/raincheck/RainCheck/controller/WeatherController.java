package com.raincheck.RainCheck.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raincheck.RainCheck.client.WeatherClient;
import com.raincheck.RainCheck.model.Weather;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class WeatherController {

    @RequestMapping(value = "/")
    public String index(Model model) throws IOException, ParseException, InterruptedException {
        WeatherClient client = new WeatherClient("54.9783", "-1.6178");
        String current = client.findCurrent();

        ObjectMapper objectMapper = new ObjectMapper();

        Weather weather = objectMapper.readValue(String.valueOf(current), new TypeReference<Weather>() {
        });

        return weather.toString();
    }
}
