package com.example.android.codechallenge.sync;

import android.app.IntentService;
import android.content.Intent;

public class MessageSyncIntentService extends IntentService {

    public MessageSyncIntentService() {
        super("MessageSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        MessageSyncTask.syncMessage(this);
    }
}
