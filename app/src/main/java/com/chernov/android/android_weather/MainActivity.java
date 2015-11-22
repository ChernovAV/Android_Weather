package com.chernov.android.android_weather;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class MainActivity extends FragmentActivity {

    static final String TAG = "FragmentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (savedInstanceState != null) {
            ft.replace(R.id.fragmentContainer, getSupportFragmentManager().findFragmentById(R.id.fragmentContainer));
            ft.commit();
        } else {
            MainFragment mContentFragment = new MainFragment();
            ft.add(R.id.fragmentContainer, mContentFragment);
            ft.commit();
        }
    }

    // Вызывается для того, чтобы сохранить пользовательский интерфейс
    // перед выходом из "активного" состояния.
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Сохраните состояние UI в переменную savedInstanceState.
        // Она будет передана в метод onCreate при закрытии и
        // повторном запуске процесса.
        super.onSaveInstanceState(savedInstanceState);
    }

    // Вызывается, когда метод onCreate завершил свою работу,
    // и используется для восстановления состояния пользовательского
    // интерфейса
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Восстановите состояние UI из переменной savedInstanceState.
        // Этот объект типа Bundle также был передан в метод onCreate.
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i(TAG, "MainActivity onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i(TAG, "MainActivity onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.i(TAG, "MainActivity onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "MainActivity onDestroy()");
    }
}