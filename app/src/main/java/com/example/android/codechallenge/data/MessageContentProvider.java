package com.example.android.codechallenge.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.android.codechallenge.data.MessageContract.MessageEntry;

import static com.example.android.codechallenge.data.MessageContract.MessageEntry.TABLE_NAME;

public class MessageContentProvider extends ContentProvider {

    // Define final integer constants for the directory of tasks and a single item.
    // It's convention to use 100, 200, 300, etc for directories,
    // and related ints (101, 102, ..) for items in that directory.
    public static final int MESSAGES = 100;
    public static final int MESSAGE_WITH_ID = 101;

    // CDeclare a static variable for the Uri matcher that you construct
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // Define a static buildUriMatcher method that associates URI's with their int match
    /**
     Initialize a new matcher object without any matches,
     then use .addURI(String authority, String path, int match) to add matches
     */
    public static UriMatcher buildUriMatcher() {

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /*
          All paths added to the UriMatcher have a corresponding int.
          For each kind of uri you may want to access, add the corresponding match with addURI.
          The two calls below add matches for the task directory and a single item by ID.
         */
        uriMatcher.addURI(MessageContract.AUTHORITY, MessageContract.PATH_TASKS, MESSAGES);
        uriMatcher.addURI(MessageContract.AUTHORITY, MessageContract.PATH_TASKS + "/#", MESSAGE_WITH_ID);

        return uriMatcher;
    }

    // Member variable for a TaskDbHelper that's initialized in the onCreate() method
    private MessageDbHelper mMessageDbHelper;

    /* onCreate() is where you should initialize anything you’ll need to setup
    your underlying data source.
    In this case, you’re working with a SQLite database, so you’ll need to
    initialize a DbHelper to gain access to it.
     */
    @Override
    public boolean onCreate() {
        // Complete onCreate() and initialize a TaskDbhelper on startup
        // [Hint] Declare the DbHelper as a global variable

        Context context = getContext();
        mMessageDbHelper = new MessageDbHelper(context);
        return true;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mMessageDbHelper.getWritableDatabase();

        Log.d("bulkInsert", "match = " + sUriMatcher.match(uri));

        switch (sUriMatcher.match(uri)) {

            case MESSAGES:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(MessageEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }
    }

    // We aren't going to do anything with this method.
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new RuntimeException(
                "We are not implementing insert in CodeChallenge. Use bulkInsert instead");
    }

//    // Implement insert to handle requests to insert a single new row of data
//    @Override
//    public Uri insert(Uri uri, ContentValues values) {
//        // Get access to the task database (to write new data to)
//        final SQLiteDatabase db = mMessageDbHelper.getWritableDatabase();
//
//        // Write URI matching code to identify the match for the tasks directory
//        int match = sUriMatcher.match(uri);
//        Uri returnUri; // URI to be returned
//
//        switch (match) {
//            case MESSAGES:
//                // Insert new values into the database
//                // Inserting values into tasks table
//                long id = db.insert(TABLE_NAME, null, values);
//                if ( id > 0 ) {
//                    returnUri = ContentUris.withAppendedId(MessageEntry.CONTENT_URI, id);
//                } else {
//                    throw new android.database.SQLException("Failed to insert row into " + uri);
//                }
//                break;
//            // Set the value for the returnedUri and write the default case for unknown URI's
//            // Default case throws an UnsupportedOperationException
//            default:
//                throw new UnsupportedOperationException("Unknown uri: " + uri);
//        }
//
//        // Notify the resolver if the uri has been changed, and return the newly inserted URI
//        getContext().getContentResolver().notifyChange(uri, null);
//
//        // Return constructed uri (this points to the newly inserted row of data)
//        return returnUri;
//    }


    // Implement query to handle requests for data by URI
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Get access to underlying database (read-only for query)
        final SQLiteDatabase db = mMessageDbHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        // Query for the tasks directory and write a default case
        switch (match) {
            // Query for the tasks directory
            case MESSAGES:
                retCursor =  db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return retCursor;
    }


    // Implement delete to delete a single row of data
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = mMessageDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        // Keep track of the number of deleted tasks
        int tasksDeleted; // starts as 0

        // Write the code to delete a single row of data
        // [Hint] Use selections to delete an item by its row ID
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case MESSAGE_WITH_ID:
                // Get the task ID from the URI path
                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksDeleted = db.delete(TABLE_NAME, "_id=?", new String[]{id});
                break;
            case MESSAGES:
                tasksDeleted = db.delete(TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (tasksDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of tasks deleted
        return tasksDeleted;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public String getType(Uri uri) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

}
