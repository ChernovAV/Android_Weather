package com.chernov.android.android_weather;

import com.chernov.android.android_weather.Item.ItemAllDay;
import com.chernov.android.android_weather.Item.ItemToday;

import java.util.ArrayList;

public class WeatherSingleton {
    private static WeatherSingleton instance;
    private static ItemToday toDay = new ItemToday();
    private static ArrayList<ItemAllDay> gItems = new ArrayList<>();

    private WeatherSingleton (){}

    public static WeatherSingleton getInstance(){
        if (null == instance){
            instance = new WeatherSingleton();
        }
        return instance;
    }

    public static ArrayList<ItemAllDay> getgItems() {
        return gItems;
    }

    public static ItemToday getToDay() {
        return toDay;
    }
}
