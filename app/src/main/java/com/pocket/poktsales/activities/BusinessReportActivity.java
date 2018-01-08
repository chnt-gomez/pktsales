package com.pocket.poktsales.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.pocket.poktsales.R;
import com.pocket.poktsales.interfaces.RequiredViewOps;

/**
 * Created by MAV1GA on 08/01/2018.
 */

public class BusinessReportActivity extends BaseActivity implements RequiredViewOps.BusinessReportViewOps{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.layoutResourceId = R.layout.activity_reports;
        super.onCreate(savedInstanceState);
    }
}
