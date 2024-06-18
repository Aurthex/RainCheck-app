package com.raincheck.RainCheck.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Weather {
    @JsonProperty("temperature_2m")
    private Integer temperature; //Converting from double to int rounds down in all cases

    @JsonProperty("weather_code")
    private Integer weather_code;

    @JsonProperty("wind_speed_10m")
    private Integer wind_speed; //Converting from double to int rounds down in all cases

    @Override
    public String toString(){
        return "Weather: " + "\n" +
                "Temperature: " + "\n" + temperature +
                "Code: " + "\n" + weather_code +
                "Wind Speed: " + "\n" + wind_speed;
    }

}
