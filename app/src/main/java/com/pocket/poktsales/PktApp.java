package com.pocket.poktsales;

import android.app.Application;
import com.orm.SugarContext;

/**
 * Created by MAV1GA on 13/09/2017.
 */

public class PktApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
    }
}
