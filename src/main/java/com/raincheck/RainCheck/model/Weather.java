package com.raincheck.RainCheck.model;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class Weather {

    // Fields to store temperature, weather code, and wind speed
    private Integer temperature;
    private Integer weather_code;
    private Integer wind_speed;

    // Constructor to parse JSON string and initialize fields
    public Weather(String json){
        String[] elements = json.split(",");
        String codeString = elements[1].substring(16,elements[1].length()-1);
        weather_code = (int) Math.round(Double.parseDouble(codeString));
        String tempString = elements[2].substring(22, elements[2].length()-1);
        temperature = (int) Math.round(Double.parseDouble(tempString));
        String windString = elements[3].substring(22, elements[3].length()-2);
        wind_speed = (int) Math.round(Double.parseDouble(windString));
    }

    // Constructor to initialize fields directly
    public Weather(int temperature, int weather_code, int wind_speed){
        this.temperature = temperature;
        this.weather_code = weather_code;
        this.wind_speed = wind_speed;
    }

    // Override toString() method to provide a string representation of the Weather object
    @Override
    public String toString(){
        return "Weather: " + "\n" +
                "Temperature: " + "\n" + temperature +
                "Code: " + "\n" + weather_code +
                "Wind Speed: " + "\n" + wind_speed;
    }

}
