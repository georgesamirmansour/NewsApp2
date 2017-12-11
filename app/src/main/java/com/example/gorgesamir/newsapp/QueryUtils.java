package com.example.gorgesamir.newsapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by gogos on 2017-12-05.
 */


public final class QueryUtils {

    private final static String TAG = QueryUtils.class.getSimpleName();
    private static final ArrayList<NewsApp> newsAppArrayList = new ArrayList<>();
    private static final int readTimeOut = 10000;
    private static final int connectTimeOut = 15000;
    private static final String requestMethod = "GET";
    private static final int responseCode = 200;

    private QueryUtils() {
    }

    private static URL createUrls(String Urls) {
        URL url = null;
        try {
            url = new URL(Urls);
        } catch (MalformedURLException e) {
            Log.e(TAG, "createUrls: error creating urls ", e);
        }
        return url;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String newLine = bufferedReader.readLine();
            while (newLine != null) {
                stringBuilder.append(newLine);
                newLine = bufferedReader.readLine();
            }
        }
        return stringBuilder.toString();
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = " ";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(readTimeOut);
            urlConnection.setConnectTimeout(connectTimeOut);
            urlConnection.setRequestMethod(requestMethod);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == responseCode) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(TAG, "makeHttpRequest: error response code " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(TAG, "makeHttpRequest: error retriving json results", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String fetchNewsAppData(String requestUrl) {
        URL url = createUrls(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(TAG, "fetchNewsAppData: error closing input stream", e);
        }
        return jsonResponse;
    }

    public static ArrayList<NewsApp> extractNewsAppData(String newsAppJson) {
        if (TextUtils.isEmpty(newsAppJson)) {
            return null;
        }
        String jsonResult = fetchNewsAppData(newsAppJson);
        String sectionName;
        String webPublicationDate;
        String webTitle;
        String webUrl;
        Bitmap thumbnail = null;
        try {
            JSONObject object = new JSONObject(jsonResult);
            JSONObject responseJsonObject = object.getJSONObject("response");
            JSONArray responseArray = responseJsonObject.optJSONArray("results");
            if (responseJsonObject.has("results")) {
                for (int i = 0; i < responseArray.length(); i++) {
                    JSONObject result = responseArray.getJSONObject(i);
                    if (result.has("sectionName")) {
                        sectionName = result.getString("sectionName");
                    } else {
                        sectionName = "No Section Provided";
                    }
                    if (result.has("webPublicationDate")) {
                        webPublicationDate = result.getString("webPublicationDate");
                    } else {
                        webPublicationDate = "No Date provided";
                    }
                    if (result.has("webTitle")) {
                        webTitle = result.getString("webTitle");
                    } else {
                        webTitle = "no title provided";
                    }
                    if (result.has("webUrl")) {
                        webUrl = result.getString("webUrl");
                    } else {
                        webUrl = null;
                    }
                    JSONObject fields = result.getJSONObject("fields");
                    if (fields.has("thumbnail")) {
                        try {
                            URL urlImage = new URL(fields.getString("thumbnail"));
                            thumbnail = BitmapFactory.decodeStream(urlImage.openConnection().getInputStream());
                        } catch (IOException e) {
                            Log.e(TAG, "extractNewsAppData: error parsing Image", e);
                        }
                    } else {
                        thumbnail = null;
                    }
                    newsAppArrayList.add(new
                            NewsApp(sectionName, webPublicationDate, webTitle, webUrl, thumbnail));
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "extractNewsAppData: problem in parsing news app json result ", e);
        }
        return newsAppArrayList;
    }

}
