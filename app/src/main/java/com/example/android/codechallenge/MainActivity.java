package com.example.android.codechallenge;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = MainActivity.class.getName();
    private RecyclerView mRecyclerView;
    private MessageAdapter mAdapter;
    TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    public static final String CODE_CHALLENGE_URL = "https://codechallenge.secrethouse.party/";
    private static final int MESSAGE_LOADER = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErrorMessageDisplay = findViewById(R.id.error_message_test_view);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);

        // Get a reference to our RecyclerView from xml, to show the data loaded from the internet
        mRecyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);

        // Use a LinearLayout to display the data
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

//        Use this setting to improve performance, because changes in content do not
//        change the child layout size in the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MessageAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        loadData();
    }

    /**
     * This method will get the user's preferred location for weather, and then tell some
     * background method to get the weather data in the background.
     */
    private void loadData() {
        showMessageDataView();

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Cursor> MessageLoader = loaderManager.getLoader(MESSAGE_LOADER);

        if (MessageLoader == null) {
            loaderManager.initLoader(MESSAGE_LOADER, null, this);
        } else {
            loaderManager.restartLoader(MESSAGE_LOADER, null, this);
        }
    }

    // This method will make the View for the data visible and hide the error message.
    private void showMessageDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    //This method will make the error message visible and hide the Message View.
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {
            /*
             * When you click the reset menu item, we want to start all over
             * and display the pretty gradient again. There are a few similar
             * ways of doing this, with this one being the simplest of those
             * ways. (in our humble opinion)
             */
            case R.id.action_refresh:
                getSupportLoaderManager().restartLoader(MESSAGE_LOADER, null, this);
                mAdapter = new MessageAdapter(this);
                mRecyclerView.setAdapter(mAdapter);
                return true;
            case R.id.action_settings:
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        Log.v(LOG_TAG, CODE_CHALLENGE_URL);
        // Create a new loader for the given URL

        return new MessageLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mLoadingIndicator.setVisibility(View.GONE);
        if(data == null || data.getCount() == 0){
            showErrorMessage();
        } else {
            showMessageDataView();
            mAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
