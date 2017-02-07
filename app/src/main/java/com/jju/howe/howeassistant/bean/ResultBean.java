package com.jju.howe.howeassistant.bean;

/**
 * Created by Howe on 2016/10/23.
 */

public class ResultBean {
    private String airQuality;
    private String sourceName;
    private String date;
    private String lastUpdateTime;
    private int dateLong;
    private String pm25;
    private String city;
    private String humidity;
    private int windLevel;
    private String weather;
    private String tempRange;
    private String wind;

    public String getAirQuality() {
        return airQuality;
    }

    public void setAirQuality(String airQuality) {
        this.airQuality = airQuality;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public int getDateLong() {
        return dateLong;
    }

    public void setDateLong(int dateLong) {
        this.dateLong = dateLong;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public int getWindLevel() {
        return windLevel;
    }

    public void setWindLevel(int windLevel) {
        this.windLevel = windLevel;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTempRange() {
        return tempRange;
    }

    public void setTempRange(String tempRange) {
        this.tempRange = tempRange;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }
}