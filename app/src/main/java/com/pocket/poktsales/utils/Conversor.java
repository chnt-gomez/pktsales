package com.pocket.poktsales.utils;

import android.util.Log;

import java.util.Locale;

/**
 * Created by MAV1GA on 05/09/2017.
 */

public class Conversor {

    public static float toFloat(String argument){
        try{
            return Float.valueOf(argument);
        }catch (NumberFormatException e){
            Log.w("Conversor", String.format("%s can't be converted to float. Returning 0 instead", argument) );
            return 0.0F;
        }
    }

    public static String asCurrency(float argument){
        try{
            return String.format(Locale.US, "$ %.2f", argument);
        }catch (NumberFormatException e){
            Log.w("Conversor", String.format("%d can't be converted to a currency String", argument));
            return "$ "+String.valueOf(argument);
        }
    }

}
