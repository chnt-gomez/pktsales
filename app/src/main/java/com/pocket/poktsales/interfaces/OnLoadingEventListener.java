package com.pocket.poktsales.interfaces;

/**
 * Created by MAV1GA on 05/09/2017.
 */

public interface OnLoadingEventListener {

    void onLoadingPrepare();
    void onLoading();
    void onLoading(String searchArgs);
    void onLoadingComplete();
    void onLoadingError();

}
