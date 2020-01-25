package com.sapient.whether.controller;

import com.sapient.whether.model.*;
import com.sapient.whether.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/weather")
public class WeatherApiController {

    static final Logger LOGGER = LoggerFactory.getLogger(WeatherApiController.class.getName());

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
        WhetherResponse response = new WhetherResponse();
        try {
            WeatherForecast forecast = this.weatherService.getWeatherForecast(country, city);

            Temperature temperature = new Temperature();
            List<WeatherEntry> entries = forecast.getEntries();
            List<Double> tempList = new ArrayList<>();

            Double celsiusTempConversion = 273.0;

            Date currentDate = new Date();
            // convert date to calendar
            Calendar c = Calendar.getInstance();
            c.setTime(currentDate);
            // manipulate date
            c.add(Calendar.DATE, 3); //same with c.add(Calendar.DAY_OF_MONTH, 1);
            // convert calendar to date
            Long threeDayTime = c.getTime().getTime();

            Instant time = Instant.ofEpochMilli(threeDayTime * 1000);

            Map<String, Object> map = new HashMap<>();
            boolean isSunDay = false;
            boolean isRainDay = false;
            for (WeatherEntry weatherEntry : entries) {
                tempList.add(weatherEntry.getTemperature());
                if (weatherEntry.getTemperature() - celsiusTempConversion > 40) {
                    isSunDay = true;
                    map.put("isSunDay", weatherEntry.getTimestamp());
                }

                if (weatherEntry.getRain() != null) {
                    isRainDay = true;
                    map.put("isRainDay", weatherEntry.getTimestamp());
                }
            }
            if (tempList != null && !tempList.isEmpty()) {
                tempList.sort(Double::compareTo);
                temperature.setLowTemperature(tempList.get(0));
                temperature.setMaxTemperature(tempList.get(tempList.size() - 1));
            }

            // check for rain season
            if (isRainDay) {
                response.setDay(map.get("isRainDay").toString());
                response.setPrediction("Rain is predicted");
                response.setMessage("Carry umbrella'.");
            } else if (isSunDay) {
                response.setDay(map.get("isRainDay").toString());
                response.setPrediction("Temperatures is  above 40 degree celsius.");
                response.setMessage("Use sunscreen lotion");
            }
            response.setTemperature(temperature);
        } catch (Exception ex) {
            LOGGER.error("Error occurred while checking the whether prediction.");
            response.setMessage("Error occurred while checking the whether prediction.");
        }
        return response;
    }

    private static void nextThreeDate() {
        Date currentDate = new Date();
        // convert date to calendar
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        // manipulate date
        c.add(Calendar.DATE, 3); //same with c.add(Calendar.DAY_OF_MONTH, 1);
        // convert calendar to date
        Long threeDayTime = c.getTime().getTime();
        System.out.println(c.getTime().getTime());
    }
}