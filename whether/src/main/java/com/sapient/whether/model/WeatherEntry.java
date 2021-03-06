package com.sapient.whether.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public class WeatherEntry implements Serializable {

    private Instant timestamp;

    private double temperature;

    private Integer weatherId;

    private String weatherIcon;

    private String rain;

    private String cloudPercentage;

    public String getCloudPercentage() {
        return cloudPercentage;
    }

    public void setCloudPercentage(String cloudPercentage) {
        this.cloudPercentage = cloudPercentage;
    }

    public void setRain(String rain) {
        this.rain = rain;
    }

    public String getRain() {
        return rain;
    }

    @JsonProperty("timestamp")
    public Instant getTimestamp() {
        return this.timestamp;
    }

    @JsonSetter("dt")
    public void setTimestamp(long unixTime) {
        this.timestamp = Instant.ofEpochMilli(unixTime * 1000);
    }

    /**
     * Return the temperature in Kelvin (K).
     */
    public double getTemperature() {
        return this.temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    @JsonProperty("clouds")
    public void setCloudsDetails(Map<String, Object> rain) {
        setCloudPercentage(rain.get("all").toString());
    }

    @JsonProperty("rain")
    public void setRainDetails(Map<String, Object> rain) {
        setRain(rain.get("3h").toString());
    }


    @JsonProperty("main")
    public void setMain(Map<String, Object> main) {
        setTemperature(Double.parseDouble(main.get("temp").toString()));
    }

    public Integer getWeatherId() {
        return this.weatherId;
    }

    public void setWeatherId(Integer weatherId) {
        this.weatherId = weatherId;
    }

    public String getWeatherIcon() {
        return this.weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    @JsonProperty("weather")
    public void setWeather(List<Map<String, Object>> weatherEntries) {
        Map<String, Object> weather = weatherEntries.get(0);
        setWeatherId((Integer) weather.get("id"));
        setWeatherIcon((String) weather.get("icon"));
    }

}
