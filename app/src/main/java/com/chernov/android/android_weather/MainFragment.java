package com.chernov.android.android_weather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Bind;
import com.chernov.android.android_weather.GoogleImageSearch.GoogleConnect;
import com.chernov.android.android_weather.Item.ItemGallery;
import com.chernov.android.android_weather.Item.ItemToday;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainFragment extends Fragment {

    static final String TAG = "myLogs";

    public static String [] CITY_LIST;
    public static String [] CITY_ID;
    String [] DAYS;
    // дни недели начиная с текущего
    static ArrayList<String> listDays = new ArrayList<>();
    SharedPreferences preferences;
    // поток, обрабатывающий картинку
    AsyncTaskSetImage task;
    PagerAdapter adapter;
    ItemToday toDay;
    @Bind(R.id.progBar) ProgressBar progressBar;
    @Bind(R.id.pager) ViewPager viewPager;
    @Bind(R.id.city) TextView city;
    @Bind(R.id.tab_layout) TabLayout tabLayout;
    @Bind(R.id.imageView2) ImageView image;
    @Bind(R.id.information) TextView temperature;
    @Bind(R.id.wtrn) TextView weathertype;
    @Bind(R.id.sid) TextView windspeed;
    @Bind(R.id.vid) TextView humidity;
    @Bind(R.id.did) TextView pressure;
    @Bind(R.id.icon) ImageView icon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // при смене ориентации экрана этот фрагмент сохраняет свое состояние. onDestroy не вызывается
        setRetainInstance(true);
        // create instance singleton
        HolidaysSingleton.getInstance();
        // create instance singleton
        WeatherSingleton.getInstance();
        // получаем в массивах строк bp string.xml города, id и days
        getResourse();
        // вычисляем день недели и создаем arraylist с днями в правильной последовательности
        isDay();
        // Register to receive messages.
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "custom-event-name".
        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(mMessageReceiver, new IntentFilter("custom-event-name"));

        // add first city
        createOrLoadCityID();
        adapter = new PagerAdapter(getFragmentManager(), 7);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        // registration butter
        ButterKnife.bind(this, v);
        // parse of information
        startParse();

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogCity().show(getFragmentManager(), "dialog_SEARCH");
            }
        });

        return v;
    }

    private void startParse() {
        Intent intent = new Intent(getActivity(), WeatherService.class);
        intent.putExtra("position", -1);
        getActivity().startService(intent);
    }

    // получаем в массивах строк города, id и days
    private void getResourse() {
        CITY_LIST = getResources().getStringArray(R.array.city);
        CITY_ID = getResources().getStringArray(R.array.id);
        DAYS = getResources().getStringArray(R.array.days);
    }

    private void weatherNow() {
        toDay = WeatherSingleton.getToDay();
        int temp = new Integer(toDay.getWeatherRightNow().toString());
        if (temp > 0) {
            temperature.setText("+" + toDay.getWeatherRightNow() + "C");
        } else {
            temperature.setText(toDay.getWeatherRightNow() + "C");
        }
        weathertype.setText(toDay.getWeatherType());
        windspeed.setText(toDay.getWindSpeed() + " м/с");
        humidity.setText(toDay.getHumidity() + "%");
        pressure.setText(toDay.getPressure() + " мм рт. ст.");

        setImage();
    }

    private void setImage() {
        if(getString(R.string.day).equals(toDay.getDay())) {
            if(toDay.getWeatherType().equals(getString(R.string.pasmurno))) {
                Picasso.with(getActivity()).load(R.drawable.ic_accuweather_tablet_07).into(icon);
            }
            if(toDay.getWeatherType().equals(getString(R.string.pas_rain))) {
                Picasso.with(getActivity()).load(R.drawable.ic_accuweather_tablet_12).into(icon);
            }
            if(toDay.getWeatherType().equals(getString(R.string.cloud_shine))) {
                Picasso.with(getActivity()).load(R.drawable.ic_accuweather_tablet_06).into(icon);
            }
            if(toDay.getWeatherType().equals(getString(R.string.power_rain))) {
                Picasso.with(getActivity()).load(R.drawable.ic_accuweather_tablet_18).into(icon);
            }
            if(toDay.getWeatherType().equals(getString(R.string.rain))) {
                Picasso.with(getActivity()).load(R.drawable.ic_accuweather_tablet_12).into(icon);
            }
        } else {
            if(toDay.getWeatherType().equals(getString(R.string.pasmurno))) {
                Picasso.with(getActivity()).load(R.drawable.ic_accuweather_tablet_38).into(icon);
            }
            if(toDay.getWeatherType().equals(getString(R.string.pas_rain))) {
                Picasso.with(getActivity()).load(R.drawable.ic_accuweather_tablet_39).into(icon);
            }
            if(toDay.getWeatherType().equals(getString(R.string.cloud_shine))) {
                Picasso.with(getActivity()).load(R.drawable.ic_accuweather_tablet_34).into(icon);
            }
            if(toDay.getWeatherType().equals(getString(R.string.power_rain))) {
                Picasso.with(getActivity()).load(R.drawable.ic_accuweather_tablet_40).into(icon);
            }
            if(toDay.getWeatherType().equals(getString(R.string.rain))) {
                Picasso.with(getActivity()).load(R.drawable.ic_accuweather_tablet_39).into(icon);
            }
        }
    }

    private void createOrLoadCityID() {
        // создаем файл для сохранения настроек
        preferences = getActivity().getSharedPreferences("saved", Context.MODE_PRIVATE);
        // если не пуст файл
        if (preferences.getString("city", "")!="") {
            // считываем с файла
            WeatherSingleton.getToDay().setCity(preferences.getString("city", ""));
        } else {
            // подключаемся к файлу
            SharedPreferences.Editor ed = preferences.edit();
            // сохраняем данные ключ - значение
            ed.putString("city", "Киев");
            ed.commit();
            preferences.getString("city", "Киев");
        }
    }

    /**
     *  remove old AllTabs
     *  then create new seven Tab
     */
    private void setDateForPage() {
        // удаляем все вкладки, если они существуют
        tabLayout.removeAllTabs();
        // добавляем 7 вкладок, считываем название class Handlerk --> toDay<ItemAllDay>
        for(int i = 0; i < 7; i++) {
            tabLayout.addTab(tabLayout.newTab().setText((WeatherSingleton.getgItems().get(i).getDate())
                    .substring((WeatherSingleton.getgItems().get(i).getDate()).length() - 2,
                            (WeatherSingleton.getgItems().get(i).getDate()).length())));
        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    /**
     *  attach Listener
     *  attach Adapter to viewPager
     */
    private void setPageAdapter() {
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, "номер страницы " + tab.getPosition());
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getStringExtra("message")) {
                case "ok":
                    // set weather information today
                    weatherNow();
                    // set days week for tabLayout
                    setDateForPage();
                    // adapter for tabLayout
                    setPageAdapter();
                    // cancel asyncktask if it run
                    if(task!=null) task.cancel(false);
                    task = new AsyncTaskSetImage();
                    task.execute();
                    break;
                case "disconnect":
                    new DialogInternetConnect().show(getFragmentManager(), "MyDialog");
                    break;
            }
        }
    };

    // вычисляем день недели и создаем arraylist с днями в правильной последовательности
    public void isDay() {
        String dayToday = new SimpleDateFormat("EEEE").format(new Date());
        for(int i = 0; i < DAYS.length; i ++) {
            if(dayToday.equals(DAYS[i])) {
                for(int j = 0 ; j < 7; j++) listDays.add(DAYS[i + j]);
                break;
            }
        }
    }

    // получаем из ссылки image в формате byte
    private class AsyncTaskSetImage extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            city.setText(WeatherSingleton.getToDay().getCity());
        }

        @Override
        protected String doInBackground(Void... params) {

            ItemGallery items = new GoogleConnect().search(1, WeatherSingleton.getToDay().getCity());

            return items.getOriginalURL();
        }

        protected void onPostExecute(String imageURL) {
            Picasso.with(getActivity())
                    .load(imageURL)
                    .resize(450, 350)
                    .centerCrop()
                    .into(image);
            progressBar.setVisibility(View.GONE);
            task = null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        if (task != null) {
            task.cancel(false);
        }
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        ButterKnife.unbind(this);
    }
}
