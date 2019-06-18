package com.example.android.codechallenge.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MessageContract {
    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.example.android.messages";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    public static final String PATH_TASKS = "messages";

    /* TaskEntry is an inner class that defines the contents of the task table */
    public static final class MessageEntry implements BaseColumns {

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();


        // Task table and column names
        public static final String TABLE_NAME = "messages";

        // Since TaskEntry implements the interface "BaseColumns", it has an automatically produced
        // "_ID" column in addition to the two below
        public static final String COLUMN_TO_NAME = "toName";
        public static final String COLUMN_From_NAME = "fromName";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_ARE_FRIENDS = "areFriends";

    }
}
