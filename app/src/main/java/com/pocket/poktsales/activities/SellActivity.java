package com.pocket.poktsales.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;

import com.pocket.poktsales.R;

import butterknife.BindView;

public class SellActivity extends BaseActivity {

    @BindView(R.id.grid)
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResourceId = R.layout.activity_sell;
        super.onCreate(savedInstanceState);
    }
}
