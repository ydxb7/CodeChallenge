package com.example.android.codechallenge;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.android.codechallenge.data.MessageContract;

public class MessageLoader extends AsyncTaskLoader<Cursor> {

    private Context mContext;
    private Cursor mCursor = null;

    private static String LOG_TAG = MessageLoader.class.getSimpleName();
//    List<String> mCachedResults;

    public MessageLoader(Context context) {
        super(context);
        mContext = context;
    }


    @Override
    synchronized public Cursor loadInBackground() {
        QueryUtils.loadDataIntoDatabase(mContext, MainActivity.CODE_CHALLENGE_URL);
        try {
            Cursor cursor = mContext.getContentResolver().query(MessageContract.MessageEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    MessageContract.MessageEntry.COLUMN_TIME + " DESC");

            return cursor;

        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to asynchronously load data.");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onStartLoading() {

        if (mCursor != null) {
            // Delivers any previously loaded data immediately
            deliverResult(mCursor);
        } else {
            // Force a new load
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    @Override
    public void deliverResult(Cursor data) {
        mCursor = data;
        super.deliverResult(data);
    }


}
