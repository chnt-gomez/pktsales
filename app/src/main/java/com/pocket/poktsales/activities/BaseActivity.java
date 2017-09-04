package com.pocket.poktsales.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import com.pocket.poktsales.R;
import com.pocket.poktsales.interfaces.RequiredViewOps;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vicente on 30/08/17.
 */

public class BaseActivity extends AppCompatActivity implements RequiredViewOps {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    protected int layoutResourceId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(layoutResourceId);
        }catch (Exception e){
            Log.w(getClass().getSimpleName(), "Invalid resource id. Set layoutResourceId");
        }finally{
            init();
        }
    }

    protected void init() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onSuccess() {
        Snackbar.make(coordinatorLayout, R.string.success, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(int messageRes) {
        Snackbar.make(coordinatorLayout, messageRes, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onError() {
        Snackbar snack = Snackbar.make(coordinatorLayout, R.string.error, Snackbar.LENGTH_SHORT);
        TextView tv = (TextView)snack.getView().findViewById((android.support.design.R.id.snackbar_text));
        tv.setTextColor(Color.parseColor("#ff7f7f"));
        snack.show();
    }

    @Override
    public void onError(int messageRes) {
        Snackbar snack = Snackbar.make(coordinatorLayout, messageRes, Snackbar.LENGTH_SHORT);
        TextView tv = (TextView)snack.getView().findViewById((android.support.design.R.id.snackbar_text));
        tv.setTextColor(Color.parseColor("#ff7f7f"));
        snack.show();
    }

    @Override
    public void onError(String message) {
        Snackbar snack = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT);
        TextView tv = (TextView)snack.getView().findViewById((android.support.design.R.id.snackbar_text));
        tv.setTextColor(Color.parseColor("#ff7f7f"));
        snack.show();
    }
}
