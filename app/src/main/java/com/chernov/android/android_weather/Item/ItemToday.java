package com.chernov.android.android_weather.Item;

public class ItemToday {
    private String weatherRightNow = null;
    private String weatherType = null;
    private String windSpeed = null;
    private String humidity = null;
    private String pressure = null;
    private String city = null;
    private String day = null;

    public void setWeatherRightNow(String weatherRightNow) {
        this.weatherRightNow = weatherRightNow;
    }

    public String getWeatherRightNow() {
        return weatherRightNow;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getPressure() {
        return pressure;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDay() {
        return day;
    }
}