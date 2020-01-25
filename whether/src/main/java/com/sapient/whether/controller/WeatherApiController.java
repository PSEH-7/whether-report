package com.sapient.whether.controller;

import com.sapient.whether.model.*;
import com.sapient.whether.service.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/weather")
public class WeatherApiController {

    private final WeatherService weatherService;

    public WeatherApiController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @RequestMapping("/now/{country}/{city}")
    public Weather getWeather(@PathVariable String country,
                              @PathVariable String city) {
        return this.weatherService.getWeather(country, city);
    }

    @RequestMapping("/threeday/{country}/{city}")
    public Object getWeatherForecast(@PathVariable String country,
                                     @PathVariable String city) {
        WeatherForecast forecast = this.weatherService.getWeatherForecast(country, city);
        WhetherResponse response = new WhetherResponse();
        Temperature temperature = new Temperature();
        List<WeatherEntry> entries = forecast.getEntries();
        List<Double> tempList = new ArrayList<>();

        Double celsiusTempConversion = 273.0;

        boolean isSunDay = false;
        boolean isRainDay = false;
        for (WeatherEntry weatherEntry : entries) {
            tempList.add(weatherEntry.getTemperature());
            if (weatherEntry.getTemperature() - celsiusTempConversion > 40) {
                isSunDay = true;
            }

            if (weatherEntry.getRain() != null) {
                isRainDay = true;
            }
        }
        tempList.sort(Double::compareTo);
        temperature.setLowTemperature(tempList.get(0));
        temperature.setMaxTemperature(tempList.get(tempList.size() - 1));

        // check for rain season
        if (isRainDay) {
            response.setPrediction("Rain is predicted");
            response.setMessage("Carry umbrella'.");
        } else if (isSunDay) {
            response.setPrediction("Temperatures is  above 40 degree celsius.");
            response.setMessage("Use sunscreen lotion");
        } else {
            response.setPrediction("Sunny day.");
            response.setMessage("'Enjoy the today's whether.");
        }
        response.setTemperature(temperature);
        return response;
    }

}
