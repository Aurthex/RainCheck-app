package com.raincheck.RainCheck.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Getter
@Setter
public class Weather {
    @JsonProperty("temperature_2m")
    private int temperature; //Converting from double to int rounds down in all cases

    @JsonProperty("weather_code")
    private int weather_code;

    @JsonProperty("wind_speed_10m")
    private int wind_speed; //Converting from double to int rounds down in all cases

    @Override
    public String toString(){
        return "Weather: " + "\n" +
                "Temperature: " + "\n" + temperature +
                "Code: " + "\n" + weather_code +
                "Wind Speed: " + "\n" + wind_speed;
    }

}
