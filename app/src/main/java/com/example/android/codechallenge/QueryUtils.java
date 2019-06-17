package com.example.android.codechallenge;

import android.util.JsonReader;
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
import java.util.List;

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Returns new URL object from the given string URL.
     */
    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    public static List<String> makeHttpRequest(URL url) throws IOException {
        List<String> jsonResponses = new ArrayList<>();

        // if the url is null, then return early
        if(url == null){
            return jsonResponses;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            // if the response was successful (response code 200), then read the input stream and parse the response.
            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponses = readFromStream(inputStream);

            } else{
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponses;
    }



    public static List<String> fetchData(String url_string){
        // Perform HTTP request to the URL and receive a JSON response back
        List<String> data = new ArrayList<>();
        if(url_string == null || url_string.length() < 1){
            return null;
        }
        URL url = QueryUtils.createUrl(url_string);

        try {
             data = QueryUtils.makeHttpRequest(url);
//            earthquakes.addAll(QueryUtils.extractEarthquakes(jsonResponse));
        } catch (IOException e) {
            // Handle the IOException
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        }

        return data;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    public static List<String> readFromStream(InputStream inputStream) throws IOException {
//        StringBuilder output = new StringBuilder();
        List<String> output = new ArrayList<>();

        if (inputStream != null) {
            // inputStream里存放的不是string是0101，因为我们知道里面应该是string，所以把inputStream里的0101，转换成string
            // 为了开始从inputStream中读取数据，我们将inputStream作为构造函数的一个参数传递给InputStreamReader
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            // inputStreamReader一次只能读一个字符，很慢，所以将inputStreamReader包装到BufferedReader中
            // BufferedReader在接收到对某个字符的请求后，会读取并保存该字符前后的一大块数据
            // 当继续请求另一个字符时，BufferedReader就能够利用提前读取的数据，来满足请求，而无需再回到inputStreamReader
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            for(int i = 0; i < 100; i++){
                output.add(line);
                line = reader.readLine();
            }

        }
        return output;
    }
}
