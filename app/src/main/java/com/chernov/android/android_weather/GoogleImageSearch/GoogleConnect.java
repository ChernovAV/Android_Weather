package com.chernov.android.android_weather.GoogleImageSearch;

import android.net.Uri;
import android.util.Log;

import com.chernov.android.android_weather.Item.ItemGallery;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GoogleConnect {

    public static final String TAG = "GoogleConnect";

    private static final String ENDPOINT = "https://www.googleapis.com/customsearch/v1";
    private static final String API_KEY = "AIzaSyD6lMKN7708l1CLrj_izW6dMUVbY3hHF_c";
    private static final String CSE = "001483299844206938585:c-xqfemqlnm";
    private static final String FILE_TYPES = "jpg";
    private static final String SEARCH_TYPE_TARGET = "image";

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    String getUrl(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    // https://www.googleapis.com/customsearch/v1?key=AIzaSyAMCi27x6880P8BwoKjSu4e5xrbUpew7jk&cx=001483299844206938585:c-xqfemqlnm&q=android&fileType=jpg&start=1&searchType=image
    public ItemGallery search(int start, String searchValue) {
        Log.i(TAG, "enter googleItems");
        String url = Uri.parse(ENDPOINT).buildUpon()
                .appendQueryParameter("key", API_KEY)
                .appendQueryParameter("cx", CSE)
                .appendQueryParameter("searchType", SEARCH_TYPE_TARGET)
                .appendQueryParameter("fileType", FILE_TYPES)
                .appendQueryParameter("q", searchValue)
                .appendQueryParameter("start", String.format("%d", start))
                .appendQueryParameter("num", "1")
                .build().toString();
        String xmlString = null;
        Log.i(TAG, "url = " + url);
        try {
            xmlString = getUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return parseItems(xmlString);
    }

    public ItemGallery parseItems(String jsonStr) {
        JSONObject jsonObj = null;

        ItemGallery gItems = new ItemGallery();
        try {

            jsonObj = new JSONObject(jsonStr);

            JSONArray imgItems = jsonObj.getJSONArray("items");

            for (int i = 0; i < imgItems.length(); i++) {

                JSONObject item = imgItems.getJSONObject(i);
                String title = item.getString("title");
                String link = item.getString("link");

                gItems.setName(title);
                gItems.setOriginalURL(link);
            }
        }
        catch (Exception e) {}

        return gItems;
    }
}
