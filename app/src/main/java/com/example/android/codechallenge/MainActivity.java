package com.example.android.codechallenge;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JsonReader> {

    public static final String LOG_TAG = MainActivity.class.getName();
    private RecyclerView mRecyclerView;
    private MessageAdapter mAdapter;
    TextView mErrorMessageDisplay;

    // Store a member variable for the listener
    private EndlessRecyclerViewScrollListener scrollListener;

    private ProgressBar mLoadingIndicator;

    private static final String CODE_CHALLENGE_URL = "https://codechallenge.secrethouse.party/";
    private static final int READER_LOADER = 22;

    private static JsonReader mReader;

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
        mAdapter = new MessageAdapter();
        mRecyclerView.setAdapter(mAdapter);

        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
                Log.d(LOG_TAG, "onLoadMore  aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            }
        };
        // Adds the scroll listener to RecyclerView
        mRecyclerView.addOnScrollListener(scrollListener);

        getReader();
//        loadMoreData();
    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int offset) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
        loadMoreData();
    }

    private void getReader(){
        showMessageDataView();

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<JsonReader> ReaderLoader = loaderManager.getLoader(READER_LOADER);
        if (ReaderLoader == null) {
            loaderManager.initLoader(READER_LOADER, null, this);
        } else {
            loaderManager.restartLoader(READER_LOADER, null, this);
        }
    }

    private void loadMoreData(){
        new AsyncTask<JsonReader, Void, List<Message>>(){

            @Override
            protected List<Message> doInBackground(JsonReader... jsonReaders) {
                JsonReader reader = jsonReaders[0];
                List<Message> messages = QueryUtils.readFromJsonReader(reader);
                return messages;
            }

            @Override
            protected void onPostExecute(List<Message> messages) {
                mAdapter.addMoreMessageData(messages);
                Log.d(LOG_TAG, "add more message data aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            }
        }.execute(mReader);

    }


//    /**
//     * This method will get the user's preferred location for weather, and then tell some
//     * background method to get the weather data in the background.
//     */
//    private void loadData() {
//        showMessageDataView();
//
//        LoaderManager loaderManager = getSupportLoaderManager();
//        Loader<List<String>> MessageLoader = loaderManager.getLoader(MESSAGE_LOADER);
//        if (MessageLoader == null) {
//            loaderManager.initLoader(MESSAGE_LOADER, null, this);
//        } else {
//            loaderManager.restartLoader(MESSAGE_LOADER, null, this);
//        }
//    }

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
                getSupportLoaderManager().restartLoader(READER_LOADER, null, this);
                mAdapter = new MessageAdapter();
                mRecyclerView.setAdapter(mAdapter);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<JsonReader> onCreateLoader(int id, Bundle args) {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        Log.v(LOG_TAG , CODE_CHALLENGE_URL);
        // Create a new loader for the given URL
        return new ReaderLoader(this, CODE_CHALLENGE_URL);
    }

    @Override
    public void onLoadFinished(Loader<JsonReader> loader, JsonReader data) {
        // Hide loading indicator because the data has been loaded
        mLoadingIndicator.setVisibility(View.GONE);

        // 如果存在 {@link Earthquake} 的有效列表，则将其添加到适配器的
        // 数据集。这将触发 ListView 执行更新。
        if (data != null) {
            showMessageDataView();
            mReader = data;
            loadMoreData();
        } else{
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<JsonReader> loader) {

    }
}
