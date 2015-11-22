package com.chernov.android.android_weather;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DialogCity extends DialogFragment {

    ListView listView;
    ArrayAdapter<String> adapter;
    EditText inputSearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Adding items to listview
        adapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, R.id.obj, MainFragment.CITY_LIST);
    }

    @SuppressLint("InflateParams")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        final View v = inflater.inflate(R.layout.city, null);

        // строка ввода search
        inputSearch = (EditText) v.findViewById(R.id.inputSearch);
        // получаем экземпляр элемента ListView
        listView = (ListView)v.findViewById(R.id.list_view);
        addByTag();

        return v;
    }

    // добавление элементов из базы данных Tag
    public void addByTag() {
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
            // set city in singleton item
            WeatherSingleton.getToDay().setCity(adapter.getItem(position).toString());
            saveCity(position);
            sendMessage(position);
            dismiss();
            }
        });
        adapter.notifyDataSetChanged();
    }

    public void saveCity(int position) {
        // подключаемся к файлу
        SharedPreferences.Editor ed = getActivity()
                .getSharedPreferences("saved", Context.MODE_PRIVATE).edit();
        // сохраняем данные ключ - значение
        ed.putString("city", adapter.getItem(position).toString());
        ed.commit();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    private void sendMessage(int position) {
        getActivity().startService(new Intent(getActivity(), WeatherService.class)
                .putExtra("position", position));
    }
}