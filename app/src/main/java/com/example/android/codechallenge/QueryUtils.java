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
    public static List<Message> makeHttpRequest(URL url) throws IOException {
        List<Message> jsonResponses = new ArrayList<>();

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

    /**
     * Return a list of {@link Message} objects that has been built up from
     * parsing a JSON response.
     */
    public static Message transferJsonToMessage(String oneLineJsonResponse) {
        if(oneLineJsonResponse == null || oneLineJsonResponse.length() < 1){
            return null;
        }

        Message message = null;

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.
            JSONObject root = new JSONObject(oneLineJsonResponse);

            JSONObject toObject = root.getJSONObject("to");
            String toName = toObject.getString("name");

            JSONObject fromObject = root.getJSONObject("from");
            String fromName = fromObject.getString("name");

            long timeInMilliseconds = root.getLong("timestamp");

            boolean areFriends = root.getBoolean("areFriends");

            message = new Message(toName, fromName, timeInMilliseconds, areFriends);


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtilsaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", oneLineJsonResponse);
            Log.e("QueryUtils", "Problem parsing the Message JSON results", e);
        }

        // Return the Message instance
        return message;
    }



    public static List<Message> fetchData(String url_string){
        // Perform HTTP request to the URL and receive a JSON response back
        List<Message> messages = new ArrayList<>();
        if(url_string == null || url_string.length() < 1){
            return null;
        }
        URL url = QueryUtils.createUrl(url_string);

        try {
            messages = QueryUtils.makeHttpRequest(url);
//            earthquakes.addAll(QueryUtils.extractEarthquakes(jsonResponse));
        } catch (IOException e) {
            // Handle the IOException
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        }

        return messages;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    public static List<Message> readFromStream(InputStream inputStream) throws IOException {
//        StringBuilder output = new StringBuilder();
        List<Message> output = new ArrayList<>();

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
                Message message = transferJsonToMessage(line);
                output.add(message);
                line = reader.readLine();
            }

        }
        return output;
    }
}
