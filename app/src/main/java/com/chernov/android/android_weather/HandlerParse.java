package com.chernov.android.android_weather;

import android.util.Log;

import com.chernov.android.android_weather.Item.ItemAllDay;
import com.chernov.android.android_weather.Item.ItemToday;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class HandlerParse extends DefaultHandler {

    private final String TAG = "myLogs";
    String element;

    private ItemAllDay ItemAllDay;
    private ItemToday items = WeatherSingleton.getToDay();;
    private ArrayList<ItemAllDay> gItems = WeatherSingleton.getgItems();
    private boolean rightNow = true;
    private boolean day = false;
    private int day_part = -1;

    @Override
    // начинаем парcить документ, который передали
    // по параметру saxparser.parse(new File("myXMLConfig"), );
    public void startDocument() throws SAXException {
        Log.d(TAG, "start parsing...");
        gItems.clear();
    }

    @Override
    // срабатывает когда документ закончился
    public void endDocument() throws SAXException {
        Log.d(TAG, "end parsing...");
    }

    @Override
    // срабатывает, когда найден нужный элемент (пространство имен, локальное имя, составное
    // имя разделенное двоиточием, возвращает сведения о неких отребутах данного тега)
    public void startElement(String namespace, String localName, String qName, Attributes attr) {
        // имена тегов
        element = qName;
        // ищем элемент именно продукта
        if(element.equals("day")) {
            day = true;
            Log.d(TAG, ">>>>>>parsing... " + element);
            Log.d(TAG, ">>>>>>parsing... " + attr.getValue(0).toString());
            ItemAllDay = new ItemAllDay();
            ItemAllDay.setDate(attr.getValue(0).toString());
            day_part = 0;
        }
    }

    @Override
    // срабатывает, когда есть закрывающаяся например </amount>
    public void endElement(String namespace, String localName, String qName) throws SAXException {
            element = "";
    }

    @Override
    // принимает значение между тегов
    // набор символов, старт и энд
    public void characters(char[]ch, int start, int end) {

        if(element.equals("ipad_image")) {
            String[] isbnParts = new String(ch, start, end).split("-");
            items.setDay(isbnParts[1]);
        }

        // текущая температура, один раз
        if(element.equals("temperature") && rightNow) {
            // заполняем Item
            items.setWeatherRightNow(new String(ch, start, end));
            Log.d(TAG, ">>>>>>right now temperature     >>>>>> " + new String(ch, start, end));
        }

        if(element.equals("sunrise")) {
            Log.d(TAG, ">>>>>>sunrise                   >>>>>> " + new String(ch, start, end));
            ItemAllDay.setSunrise(new String(ch, start, end));
        }

        if(element.equals("sunset")) {
            Log.d(TAG, ">>>>>>sunset                    >>>>>> " + new String(ch, start, end));
            ItemAllDay.setSunset(new String(ch, start, end));
        }

        if(element.equals("temperature_from")) {
            Log.d(TAG, ">>>>>>morning temperature_from  >>>>>> " + new String(ch, start, end));
            switch (day_part) {
                case 0:
                    ItemAllDay.setMorningTemperatureFrom(getTempterature(new String(ch, start, end)));
                    break;
                case 1:
                    ItemAllDay.setDayTemperatureFrom(getTempterature(new String(ch, start, end)));
                    break;
                case 2:
                    ItemAllDay.setEveningTemperatureFrom(getTempterature(new String(ch, start, end)));
                    break;
                case 3:
                    ItemAllDay.setNightTemperatureFrom(getTempterature(new String(ch, start, end)));
                    break;
            }
        }

        if(element.equals("temperature_to")) {
            Log.d(TAG, ">>>>>>morning temperature_to    >>>>>> " + new String(ch, start, end));
            switch (day_part) {
                case 0:
                    ItemAllDay.setMorningTemperatureTo(getTempterature(new String(ch, start, end)));
                    break;
                case 1:
                    ItemAllDay.setDayTemperatureTo(getTempterature(new String(ch, start, end)));
                    break;
                case 2:
                    ItemAllDay.setEveningTemperatureTo(getTempterature(new String(ch, start, end)));
                    break;
                case 3:
                    ItemAllDay.setNightTemperatureTo(getTempterature(new String(ch, start, end)));
                    break;
            }
        }

        if(element.equals("weather_type")) {
            // информация на данный момент
            if(rightNow) {
                items.setWeatherType(new String(ch, start, end));
                Log.d(TAG, ">>>>>>weather_type              >>>>>> " + new String(ch, start, end));
            } else {
                if(day && day_part<4) {
                    Log.d(TAG, ">>>>>>weather_type              >>>>>> " + new String(ch, start, end));
                    switch (day_part) {
                        case 0:
                            ItemAllDay.setMorningWeatherType(new String(ch, start, end));
                            break;
                        case 1:
                            ItemAllDay.setDayWeatherType(new String(ch, start, end));
                            break;
                        case 2:
                            ItemAllDay.setEveningWeatherType(new String(ch, start, end));
                            break;
                        case 3:
                            ItemAllDay.setNightWeatherType(new String(ch, start, end));
                            break;
                    }
                }
            }
        }

        if(element.equals("wind_speed")) {
            // информация на данный момент
            if(rightNow) {
                items.setWindSpeed(new String(ch, start, end));
                Log.d(TAG, ">>>>>> скорость ветра wind_speed>>>>>> " + new String(ch, start, end));
            } else {
                if(day && day_part<4) {
                    Log.d(TAG, ">>>>>> скорость ветра wind_speed>>>>>> " + new String(ch, start, end));

                    switch (day_part) {
                        case 0:
                            ItemAllDay.setMorningWindSpeed(new String(ch, start, end));
                            break;
                        case 1:
                            ItemAllDay.setDayWindSpeed(new String(ch, start, end));
                            break;
                        case 2:
                            ItemAllDay.setEveningWindSpeed(new String(ch, start, end));
                            break;
                        case 3:
                            ItemAllDay.setNightWindSpeed(new String(ch, start, end));
                            break;
                    }
                }
            }
        }

        if(element.equals("humidity")) {
            // информация на данный момент
            if(rightNow) {
                items.setHumidity(new String(ch, start, end));
                Log.d(TAG, ">>>>>>влажность humidity        >>>>>> " + new String(ch, start, end));
            } else {
                if(day && day_part<4) {
                    Log.d(TAG, ">>>>>>влажность humidity        >>>>>> " + new String(ch, start, end));

                    switch (day_part) {
                        case 0:
                            ItemAllDay.setMorningHumidity(new String(ch, start, end));
                            break;
                        case 1:
                            ItemAllDay.setDayHumidity(new String(ch, start, end));
                            break;
                        case 2:
                            ItemAllDay.setEveningHumidity(new String(ch, start, end));
                            break;
                        case 3:
                            ItemAllDay.setNightHumidity(new String(ch, start, end));
                            break;
                    }
                }
            }
        }

        if(element.equals("pressure")) {
            // информация на данный момент
            if(rightNow) {
                items.setPressure(new String(ch, start, end));
                Log.d(TAG, ">>>>>>давление pressure >>>>>> " + new String(ch, start, end));
                rightNow = false;
            } else {
                if(day) {
                    if(day && day_part<4) {
                        Log.d(TAG, ">>>>>>давление pressure >>>>>> " + new String(ch, start, end));
                        switch (day_part) {
                            case 0:
                                ItemAllDay.setMorningPressure(new String(ch, start, end));
                                break;
                            case 1:
                                ItemAllDay.setDayPressure(new String(ch, start, end));
                                break;
                            case 2:
                                ItemAllDay.setEveningPressure(new String(ch, start, end));
                                break;
                            case 3:
                                ItemAllDay.setNightPressure(new String(ch, start, end));
                                break;
                        }
                    }

                    day_part++;

                    if(day_part == 4) {
                        Log.d(TAG, "записываем данные");
                        gItems.add(ItemAllDay);
                    }

                    if(day_part == 6) {
                        day_part = 0;
                        Log.d(TAG, "день закончен");
                    }
                }
            }
        }


    }

    public String getTempterature(String temperature) {
        int temp = new Integer(temperature);
        String result;
        if (temp > 0) {
            result = "+" + temp;
        } else {
           result = "" + temp;
        }
        return result;
    }
}
