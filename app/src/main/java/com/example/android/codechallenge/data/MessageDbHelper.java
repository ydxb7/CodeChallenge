package com.example.android.codechallenge.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.codechallenge.data.MessageContract.MessageEntry;

public class MessageDbHelper extends SQLiteOpenHelper {

    // The name of the database
    private static final String DATABASE_NAME = "messagesDb.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 2;


    // Constructor
    MessageDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    /**
     * Called when the tasks database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create tasks table (careful to follow SQL formatting rules)
        final String CREATE_TABLE = "CREATE TABLE "  + MessageContract.MessageEntry.TABLE_NAME + " (" +
                MessageEntry._ID                + " INTEGER PRIMARY KEY, " +
                MessageEntry.COLUMN_TO_NAME     + " TEXT, " +
                MessageEntry.COLUMN_From_NAME   + " TEXT, " +
                MessageEntry.COLUMN_TIME        + " LONG, " +
                MessageEntry.COLUMN_ARE_FRIENDS + " INTEGER);";

        db.execSQL(CREATE_TABLE);
    }


    /**
     * This method discards the old table of data and calls onCreate to recreate a new one.
     * This only occurs when the version number for this database (DATABASE_VERSION) is incremented.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MessageEntry.TABLE_NAME);
        onCreate(db);
    }
}
