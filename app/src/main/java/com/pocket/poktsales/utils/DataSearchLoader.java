package com.pocket.poktsales.utils;

import android.os.AsyncTask;
import android.os.Handler;

import com.pocket.poktsales.interfaces.OnLoadingEventListener;

/**
 * Created by MAV1GA on 05/09/2017.
 */

public class DataSearchLoader extends AsyncTask<String, Void, Void> {

    private final OnLoadingEventListener callback;

    public DataSearchLoader (OnLoadingEventListener callback){
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(String... params) {
        callback.onLoading(params[0]);
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
