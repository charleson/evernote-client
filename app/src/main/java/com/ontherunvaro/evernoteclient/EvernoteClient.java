package com.ontherunvaro.evernoteclient;

import android.app.Application;
import android.util.Log;

import com.evernote.client.android.EvernoteSession;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by ontherunvaro on 28/07/16.
 */
public class EvernoteClient extends Application {

    private static final String TAG = EvernoteClient.class.getName();

    // TODO: 28/07/16 change to production
    private static final EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.SANDBOX;

    // TODO: 28/07/16 initialize keys and obfuscate instead of using properties file
    //private static final String CONSUMER_KEY;
    //private static final String CONSUMER_SECRET;
    private String CONSUMER_KEY;
    private String CONSUMER_SECRET;

    private static final boolean SUPPORT_APP_LINKED_NOTEBOOKS = true;

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            Properties p = new Properties();
            p.load(getBaseContext().getAssets().open("properties/keys.properties"));

            CONSUMER_KEY = p.getProperty("consumer.key");
            CONSUMER_SECRET = p.getProperty("consumer.secret");
        } catch (IOException e) {
            // TODO: 28/07/16 handle missing file?
            e.printStackTrace();
        }


        new EvernoteSession.Builder(this).setEvernoteService(EVERNOTE_SERVICE)
                .setSupportAppLinkedNotebooks(SUPPORT_APP_LINKED_NOTEBOOKS)
                .build(CONSUMER_KEY, CONSUMER_SECRET)
                .asSingleton();

        Log.d(TAG, "Evernote session created");
    }
}
