package com.technophile.sarah;



import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public final class HttpHandler {

    private static final String LOG_TAG = HttpHandler.class.getSimpleName();

    private HttpHandler() {
    }
    public static List<Articles> fetchArticles(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        return extractFeatureFromJson(jsonResponse);
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String response = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    private static List<Articles> extractFeatureFromJson(String articlesJSON) {

        if (TextUtils.isEmpty(articlesJSON)) {
            return null;
        }
        List<Articles> articlesList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(articlesJSON);

            JSONObject response = jsonObject.getJSONObject("response");

            JSONArray result = response.getJSONArray("results");

            for (int i = 0; i < result.length(); i++) {
                List<String> contributors=new ArrayList<>();
                JSONObject articlesObj = result.getJSONObject(i);
                String title=articlesObj.getString("webTitle");
                String date=articlesObj.getString("webPublicationDate");
                String section=articlesObj.getString("sectionName");
                JSONArray tagsArray=  articlesObj.optJSONArray("tags");
                if (tagsArray!=null){
                    for (int j=0;j<tagsArray.length();j++){
                        JSONObject contributorsObj=tagsArray.getJSONObject(j);
                        contributors.add(contributorsObj.getString("webTitle"));
                    }
                }

                Articles articles = new Articles(title, date, section,contributors);
                articlesList.add(articles);
            }
        } catch (JSONException e) {
            Log.e("HttpHandler", "Some Error Occured", e);
        }
        return articlesList;
    }
}