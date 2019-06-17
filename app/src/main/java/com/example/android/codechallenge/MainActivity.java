package com.example.android.codechallenge;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Message>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    private RecyclerView mRecyclerView;
    private MessageAdapter mAdapter;
    TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    private static final String CODE_CHALLENGE_URL = "https://codechallenge.secrethouse.party/";
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
        mAdapter = new MessageAdapter();
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
        Loader<List<String>> MessageLoader = loaderManager.getLoader(MESSAGE_LOADER);
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
                mAdapter = new MessageAdapter();
                mRecyclerView.setAdapter(mAdapter);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<List<Message>> onCreateLoader(int id, Bundle args) {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        Log.v(LOG_TAG , CODE_CHALLENGE_URL + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        // Create a new loader for the given URL
        return new MessageLoader(this, CODE_CHALLENGE_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Message>> loader, List<Message> data) {
        // Hide loading indicator because the data has been loaded
        mLoadingIndicator.setVisibility(View.GONE);

        // 如果存在 {@link Earthquake} 的有效列表，则将其添加到适配器的
        // 数据集。这将触发 ListView 执行更新。
        if (data != null && !data.isEmpty()) {
            showMessageDataView();
            mAdapter.setMessageData(data);
        } else{
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Message>> loader) {

    }
}
