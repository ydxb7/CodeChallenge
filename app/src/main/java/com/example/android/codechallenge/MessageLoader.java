package com.example.android.codechallenge;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

public class MessageLoader extends AsyncTaskLoader<List<String>> {

    private String mUrl;
    private static String LOG_TAG = MessageLoader.class.getSimpleName();
//    List<String> mCachedResults;

    public MessageLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }


    @Override
    public List<String> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // 执行网络请求、解析响应和提取列表。
        List<String> data = QueryUtils.fetchData(mUrl);
        return data;
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
