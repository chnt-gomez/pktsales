package com.pocket.poktsales.utils;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.pocket.poktsales.interfaces.OnLoadingEventListener;

/**
 * Created by MAV1GA on 05/09/2017.
 */

public class DataLoader extends AsyncTask<String, Void, Void> {

    private final OnLoadingEventListener callback;

    private static final String TAG = "DataLoader";

    public DataLoader (OnLoadingEventListener callback){
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(String... params) {
        if (params.length == 0)
            callback.onLoading();
        else
            callback.onLoading(params[0]);
        return null;
    }

    @Override
    protected void onPreExecute() {
        Log.i(TAG, "onPreExecute: is executing!");
        super.onPreExecute();
        callback.onLoadingPrepare();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.i(TAG, "onPostExecute: is called. Loading is finished");
        super.onPostExecute(aVoid);
        callback.onLoadingComplete();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        callback.onLoadingError();
    }
}
