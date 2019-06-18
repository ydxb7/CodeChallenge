package com.example.android.codechallenge;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.widget.Toast;
import android.util.JsonReader;

import com.example.android.codechallenge.data.MessageContract;

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

import javax.net.ssl.HttpsURLConnection;
import javax.sql.DataSource;

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
    public static void makeHttpRequest(Context context, URL url) {

        // if the url is null, then return early
        if (url == null) {
            return;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
            // Timeout for reading InputStream arbitrarily set to 3000ms.
            urlConnection.setReadTimeout(300000);
            // Timeout for connection.connect() arbitrarily set to 3000ms.
            urlConnection.setConnectTimeout(300000);
            // For this use case, set HTTP method to GET.
            urlConnection.setRequestMethod("GET");
            // Already true by default but setting just in case; needs to be true since this request
            // is carrying an input (response) body.
            urlConnection.setDoInput(true);
            // Open communications link (network traffic occurs here).
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            // Retrieve the response body as an InputStream.
            inputStream = urlConnection.getInputStream();
            readFromStream(context, inputStream);

        } catch (IOException e) {
            // Handle the exception
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Problem closing input stream", e);
                }
            }
        }
        return;
    }


    public static void loadDataIntoDatabase(Context context, String url_string) {
        // Perform HTTP request to the URL and receive a JSON response back
        if (url_string == null || url_string.length() < 1) {
            return;
        }
        URL url = QueryUtils.createUrl(url_string);


        makeHttpRequest(context, url);
//            earthquakes.addAll(QueryUtils.extractEarthquakes(jsonResponse));


        return;
    }


    public static void readFromStream(Context context, InputStream in) throws IOException {
        Log.d(LOG_TAG, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa readFromStream");
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        reader.setLenient(true);
        try {
            readMessagesObjects(context, reader);
            return;
        } finally {
            reader.close();
        }
    }

    public static void readMessagesObjects(Context context, JsonReader reader) {
        Log.d(LOG_TAG, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa readMessagesObjects");
        ContentValues[] messageContentValues = new ContentValues[2000];
//        List<Message> messages = new ArrayList<Message>();

        try {
            int i = 0;
            while (reader.hasNext() && i < 2000) {
                messageContentValues[i] = readMessage(reader);
                if (i % 10 == 0) {
                    Log.d(LOG_TAG, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa insert " + i);
                }
                if(!reader.hasNext()){
                    Log.d(LOG_TAG, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa no next! " + i);
                }
                i++;
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem reading messages objects");
        }


        Log.d("readMessagesObjects", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa messageContentValues length = " + messageContentValues.length);
        context.getContentResolver().bulkInsert(
                MessageContract.MessageEntry.CONTENT_URI,
                messageContentValues);


        Log.d("readMessagesObjects", "finish insert  aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        return;
    }


    public static ContentValues readMessage(JsonReader reader) throws IOException {
        ContentValues weatherValues = new ContentValues();

        String toName = null;
        String fromName = null;
        Long timeInMilliseconds = null;
        boolean areFriends = false;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();

            if (name.equals("to")) {
                toName = readName(reader);
            } else if (name.equals("from")) {
                fromName = readName(reader);
            } else if (name.equals("timestamp")) {
                timeInMilliseconds = reader.nextLong();
            } else if (name.equals("areFriends")) {
                areFriends = reader.nextBoolean();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
//        Message message = new Message(toName, fromName, timeInMilliseconds, areFriends);
        weatherValues.put(MessageContract.MessageEntry.COLUMN_TO_NAME, toName);
        weatherValues.put(MessageContract.MessageEntry.COLUMN_From_NAME, fromName);
        weatherValues.put(MessageContract.MessageEntry.COLUMN_TIME, timeInMilliseconds);
        weatherValues.put(MessageContract.MessageEntry.COLUMN_ARE_FRIENDS, areFriends);

        return weatherValues;
    }

    public static String readName(JsonReader reader) throws IOException {
        String username = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("name")) {
                username = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return username;
    }

    public static void deleteAllMessagesInDatabase(Context context) {
        Uri uri = MessageContract.MessageEntry.CONTENT_URI;
        context.getContentResolver().delete(uri, null, null);
    }

}
