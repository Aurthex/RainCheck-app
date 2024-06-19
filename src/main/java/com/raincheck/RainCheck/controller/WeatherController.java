package com.raincheck.RainCheck.controller;

import com.raincheck.RainCheck.client.WeatherClient;
import com.raincheck.RainCheck.model.Weather;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class WeatherController {

    @RequestMapping(value = "/")
    public String index(Model model) throws IOException, InterruptedException {

        WeatherClient client = new WeatherClient("54.9783", "-1.6178");
        String current = client.findCurrent();
        Weather weather = new Weather(current);
        return current + weather.toString();
    }
}
