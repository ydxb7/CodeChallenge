package com.example.android.codechallenge;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.JsonReader;

import java.util.List;

public class ReaderLoader extends AsyncTaskLoader<JsonReader> {

private String mUrl;
private static String LOG_TAG = MessageLoader.class.getSimpleName();
//    List<String> mCachedResults;

public ReaderLoader(Context context, String url) {
        super(context);
        mUrl = url;
        }


@Override
public JsonReader loadInBackground() {
        if (mUrl == null) {
        return null;
        }
        // 执行网络请求、解析响应和提取列表。

        JsonReader reader  = QueryUtils.getDateFromUrlString(mUrl);
        return reader;
        }

@Override
protected void onStartLoading() {
//        if(mCachedResults != null){
//            deliverResult(mCachedResults);
//        } else {
//            forceLoad();
//        }
        forceLoad();
        }

@Override
protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
        }

//    @Override
//    public void deliverResult(List<String> data) {
//        mCachedResults = data;
//        super.deliverResult(data);
//    }
        }