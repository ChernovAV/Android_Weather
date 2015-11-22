package com.chernov.android.android_weather;

import android.app.IntentService;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.xml.sax.SAXException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class WeatherService extends IntentService {

    public static boolean isStopped = false;
    private static final String ENDPOINT = "http://export.yandex.ru/weather-ng/forecasts/";
    private static String CITYID = "33345";
    private static String PATH = "";

    public WeatherService() {
        super("WeatherService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // get code city
        int city = intent.getIntExtra("position", -1);

        if(city!=-1) CITYID = MainFragment.CITY_ID[city];

        // создать файл text.xml если его не существуют, в нем будет сохранена полученная страницы
        try {
            checkDB("test");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String message;
        if(!isNetworkConnected()) {
            message = "disconnect";
        } else {
            startParse();
            message = "ok";
        }

        LocalBroadcastManager
                .getInstance(this)
                .sendBroadcast(new Intent("custom-event-name")
                        .putExtra("message", message));

        isStopped = true;
    }

    // проверка существования файла
    private void checkDB(String db_name) throws IOException {
        // Получение пути и проверка существования БД
        String filename = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        // Проверка существования каталога
        File file = new File(filename);
        // Если каталог не существует
        if (!file.exists()) {
            // Создать подпапки
            file.mkdirs();
        }// Теперь сам файл
        file = new File(file, db_name);
        // Если файла нет
        if (!file.exists()) {
            // Создать новый файл - ОБЯЗАТЕЛЬНО
            file.createNewFile();
        }
        PATH = filename + db_name;
    }

    private void startParse() {
       try {
           // получаем массив byte[] по ссылке и сохраняем в файл
           readAndWrite(CITYID);
        } catch (IOException e) {e.printStackTrace();}
       // SAXParserFactory - для подгрузки документа XML
       SAXParserFactory parserF = SAXParserFactory.newInstance();
       try {
           // парсер
           SAXParser saxparser = parserF.newSAXParser();
           // к объекту привязываем готовую строку XML и указываем слушатель
           saxparser.parse(new File(PATH), new HandlerParse());
       } catch (ParserConfigurationException e) {e.printStackTrace();} catch (SAXException e) {
                    e.printStackTrace();} catch (IOException e) {e.printStackTrace();}
    }

    // получает данные по URL и возвращает их в виде массива байтов, выгружаем в файл test.xml
    private void readAndWrite(String CITYID) throws IOException {
        // создаем объект URL на базе строки urlSpec, например
        URL url = new URL(ENDPOINT + CITYID + ".xml");
        // создаем объект подключения к заданному URL адресу
        // url.openConnection() - возвращает URLConnection (подключение по протоколу HTTP)
        // это откроет доступ для работы с методами запросов. HttpURLConnection - предоставляет подключение
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            // создаем пустой массив байтов
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // связь с конечной точкой
            InputStream in = connection.getInputStream();
            // если подключение с интернетом отсутствует (нет кода страницы)
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return;
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            // разбираем код по 1024 buffer, пока не закончится информация
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            FileWriter fw = new FileWriter(PATH);
            fw.write(out.toString());
            fw.close();
        } finally {
            connection.disconnect();
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(getApplication().CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
