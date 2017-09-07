package com.pocket.poktsales.utils;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.pocket.poktsales.interfaces.OnLoadingEventListener;

/**
 * Created by MAV1GA on 05/09/2017.
 */

public class DataLoader extends AsyncTask<Void, Void, Void> {

    private final OnLoadingEventListener callback;

    public DataLoader (OnLoadingEventListener callback){
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(Void... params) {
        callback.onLoading();
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        callback.onLoadingPrepare();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onLoadingComplete();
            }
        }, 1500);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        callback.onLoadingError();
    }
}