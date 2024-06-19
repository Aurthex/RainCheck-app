package com.raincheck.RainCheck.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Weather {
    private Integer temperature;
    private Integer weather_code;
    private Integer wind_speed;

    public Weather(String json){
        String[] elements = json.split(",");
        String tempString = elements[2].substring(17);
        temperature = (int) Math.round(Double.parseDouble(tempString));
        String codeString = elements[3].substring(15);
        weather_code = (int) Math.round(Double.parseDouble(codeString));
        String windString = elements[4].substring(17, elements[4].length()-1);
        wind_speed = (int) Math.round(Double.parseDouble(windString));
    }

    @Override
    public String toString(){
        return "Weather: " + "\n" +
                "Temperature: " + "\n" + temperature +
                "Code: " + "\n" + weather_code +
                "Wind Speed: " + "\n" + wind_speed;
    }

}
