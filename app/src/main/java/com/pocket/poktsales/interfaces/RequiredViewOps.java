package com.pocket.poktsales.interfaces;

/**
 * Created by MAV1GA on 04/09/2017.
 */

public interface RequiredViewOps {


    void onSuccess();
    void onSuccess(int messageRes);
    void onSuccess(String message);
    void onError();
    void onError(int messageRes);
    void onError(String message);

}
