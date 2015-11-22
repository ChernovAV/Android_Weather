package com.chernov.android.android_weather;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chernov.android.android_weather.Item.ItemAllDay;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

@SuppressLint("ValidFragment")
public class TabFragment extends Fragment {

    @Bind(R.id.morning_temperature) TextView mTempterature;
    @Bind(R.id.day_temperature) TextView dTempterature;
    @Bind(R.id.evening_temperature) TextView eTempterature;
    @Bind(R.id.night_temperature) TextView nTempterature;

    @Bind(R.id.m_status) TextView mStatus;
    @Bind(R.id.d_status) TextView dStatus;
    @Bind(R.id.e_status) TextView eStatus;
    @Bind(R.id.n_status) TextView nStatus;

    @Bind(R.id.m_pressure) TextView mPressure;
    @Bind(R.id.d_pressure) TextView dPressure;
    @Bind(R.id.e_pressure) TextView ePressure;
    @Bind(R.id.n_pressure) TextView nPressure;

    @Bind(R.id.m_humidity) TextView mHumidity;
    @Bind(R.id.d_humidity) TextView dHumidity;
    @Bind(R.id.e_humidity) TextView eHumidity;
    @Bind(R.id.n_humidity) TextView nHumidity;

    @Bind(R.id.m_wind) TextView mWind;
    @Bind(R.id.d_wind) TextView dWind;
    @Bind(R.id.e_wind) TextView eWind;
    @Bind(R.id.n_wind) TextView nWind;

    @Bind(R.id.title_sunrise) TextView tSunrise;
    @Bind(R.id.title_sunset) TextView tSunset;

    @Bind(R.id.titleData) TextView data;
    @Bind(R.id.titleWeekday) TextView titleWeekday;
    @Bind(R.id.holiday) TextView holiday;

    int position = 0;

    public TabFragment(int position) {
        this.position = position;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // при смене ориентации экрана этот фрагмент сохраняет свое состояние. onDestroy не вызывается
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_fragment, null);
        // registration butter
        ButterKnife.bind(this, v);
        setInformation();
       return v;
    }

    public void setInformation () {
        ArrayList<ItemAllDay> gItems = WeatherSingleton.getgItems();

        titleWeekday.setText(MainFragment.listDays.get(position));
        data.setText(gItems.get(position).getDate());
        tSunrise.setText("(" + gItems.get(position).getSunrise() + ")");
        tSunset.setText("(" + gItems.get(position).getSunset() + ")");

        holiday.setText(HolidaysSingleton.getHashmap()
                .get("d_" + gItems.get(position).getDate().replace("-", "_")));

        if(!(gItems.get(position).getMorningTemperatureFrom()==null)) {
            mTempterature.setText(gItems.get(position).getMorningTemperatureFrom() + " ..." +
                    gItems.get(position).getMorningTemperatureTo());
        }
        mStatus.setText(gItems.get(position).getMorningWeatherType());
        mPressure.setText(gItems.get(position).getMorningPressure());
        mHumidity.setText(gItems.get(position).getMorningHumidity() + "%");
        mWind.setText(gItems.get(position).getMorningWindSpeed() + " м/с");

        if(!(gItems.get(position).getDayTemperatureFrom() == null)) {
            dTempterature.setText(gItems.get(position).getDayTemperatureFrom() + " ..." +
                    gItems.get(position).getDayTemperatureTo());
        }
        dStatus.setText(gItems.get(position).getDayWeatherType());
        dPressure.setText(gItems.get(position).getDayPressure());
        dHumidity.setText(gItems.get(position).getDayHumidity() + "%");
        dWind.setText(gItems.get(position).getDayWindSpeed() + " м/с");

        if(!(gItems.get(position).getEveningTemperatureFrom() == null)) {
            eTempterature.setText(gItems.get(position).getEveningTemperatureFrom() + " ..." +
                    gItems.get(position).getEveningTemperatureTo());
        }
        eStatus.setText(gItems.get(position).getEveningWeatherType());
        ePressure.setText(gItems.get(position).getEveningPressure());
        eHumidity.setText(gItems.get(position).getEveningHumidity() + "%");
        eWind.setText(gItems.get(position).getEveningWindSpeed() + " м/с");

        if(!(gItems.get(position).getNightTemperatureFrom() == null)) {
            nTempterature.setText(gItems.get(position).getNightTemperatureFrom() + " ..." +
                    gItems.get(position).getNightTemperatureTo());
        }
        nStatus.setText(gItems.get(position).getNightWeatherType());
        nPressure.setText(gItems.get(position).getNightPressure());
        nHumidity.setText(gItems.get(position).getNightHumidity() + "%");
        nWind.setText(gItems.get(position).getNightWindSpeed() + " м/с");
    }
}