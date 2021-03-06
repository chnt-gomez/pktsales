package com.pocket.poktsales.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.pocket.poktsales.R;
import com.pocket.poktsales.interfaces.OnLoadingEventListener;
import com.pocket.poktsales.interfaces.RequiredViewOps;
import com.pocket.poktsales.utils.DataLoader;

import java.util.prefs.PreferenceChangeEvent;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vicente on 30/08/17.
 */

public abstract class BaseActivity extends AppCompatActivity implements RequiredViewOps, OnLoadingEventListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    protected ProgressBar loadingBar;

    protected DataLoader loader;

    protected void start(){
        loader = new DataLoader(this);
        loader.execute();
    }

    @Override
    public void onSuccess() {

    }

    protected void init(){

    }



    protected void setToolbar(){
        setSupportActionBar(toolbar);
    }



    @Override
    public void onSuccess(int messageRes) {
        Snackbar.make(coordinatorLayout, messageRes, Snackbar.LENGTH_SHORT).show();
        onSuccess();
    }

    @Override
    public void onSuccess(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
        onSuccess();
    }

    public void showMessage(String message) {

    }

    public void showMessage(int stringResource){
        Snackbar.make(coordinatorLayout, getString(stringResource), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onError() {

    }

    @Override
    public void onError(int messageRes) {
        Snackbar snack = Snackbar.make(coordinatorLayout, messageRes, Snackbar.LENGTH_SHORT);
        TextView tv = (TextView)snack.getView().findViewById((android.support.design.R.id.snackbar_text));
        tv.setTextColor(Color.parseColor("#ff7f7f"));
        snack.show();
        onError();
    }

    @Override
    public String getResString(int resourceId) {
        return getString(resourceId);
    }

    @Override
    public void onError(String message) {
        Snackbar snack = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT);
        TextView tv = (TextView)snack.getView().findViewById((android.support.design.R.id.snackbar_text));
        tv.setTextColor(Color.parseColor("#ff7f7f"));
        snack.show();
        onError();
    }

    @Override
    public void onLoadingPrepare() {
        Log.d("ON LOADING PREPARE", "start");
        if (loadingBar != null){
            loadingBar.animate()
                    .setInterpolator(new AccelerateInterpolator())
                    .setDuration(300)
                    .alpha(1f);
        }
        Log.d("ON LOADING PREPARE", "end");
    }

    @Override
    public void onLoading(){
        Log.d("ON LOADING", "start");
        Log.d("ON LOADING", "end");
    }

    @Override
    public void onLoading(String searchArgs) {

    }

    @Override
    public void onLoadingComplete() {
        Log.d("ON LOADING COMPLETE", "start");
        if (loadingBar != null){
            loadingBar.animate()
                    .translationY(-loadingBar.getHeight())
                    .setInterpolator(new AccelerateInterpolator())
                    .setDuration(300)
                    .alpha(0f);
        }
        Log.d("ON LOADING COMPLETE", "end");
    }

    @Override
    public void onLoadingError() {
        if (loadingBar != null){
            loadingBar.animate()
                    .translationY(-loadingBar.getHeight())
                    .setInterpolator(new AccelerateInterpolator())
                    .setDuration(300)
                    .alpha(0f);
        }
    }

    protected String getPreferenceString(String key, String defVal){
        return PreferenceManager.getDefaultSharedPreferences(this).getString((key), defVal);
    }

    protected boolean getPreferenceBoolean(String key, boolean defVal) {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(key, defVal);
    }

    protected void saveBooleanPreference(String key, boolean value){
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(key, value).apply();
    }
}
