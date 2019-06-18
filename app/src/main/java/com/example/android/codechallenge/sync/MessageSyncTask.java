package com.example.android.codechallenge.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.text.format.DateUtils;

import com.example.android.codechallenge.MainActivity;
import com.example.android.codechallenge.QueryUtils;

import java.net.URL;

public class MessageSyncTask {

    /**
     * Performs the network request for updated message data, parses the JSON from that request, and
     * inserts the new weather information into our ContentProvider.
     *
     * @param context Used to access utility methods and the ContentResolver
     */
    synchronized public static void syncMessage(Context context) {

//        QueryUtils.loadDataIntoDatabase(context, MainActivity.CODE_CHALLENGE_URL);

    }
}