package com.pocket.poktsales.utils;

import android.util.Log;

import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
            return new DecimalFormat("$ ###,###,##0.00").format(argument);
        }catch (NumberFormatException e){
            Log.w("Conversor", String.format("%f can't be converted to a currency String", argument));
            return "$ "+String.valueOf(argument);
        }
    }

    public static String asFloat(float argument){
        try{
            return new DecimalFormat("###,###,##0.00").format(argument);
        }catch (NumberFormatException e){
            Log.w("Conversor", String.format("%f can't be converted to a currency String", argument));
            return String.valueOf(argument);
        }
    }

    public static String asPerc(float argument){
       return new DecimalFormat("##%").format(argument);
    }

    public static String formatDate(DateTime time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        return simpleDateFormat.format(time);
    }

    public static String asTicketFormat(long arg){
        try{
            return new DecimalFormat("0000000").format(arg);
        }catch(NumberFormatException e){
            Log.w("Conversor", String.format("%d can't be converted to a ticket String", arg));
            return String.valueOf(arg);
        }
    }

}
