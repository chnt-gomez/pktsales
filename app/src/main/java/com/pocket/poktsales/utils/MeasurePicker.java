package com.pocket.poktsales.utils;
import android.content.res.Resources;
import com.pocket.poktsales.R;

/**
 * Created by MAV1GA on 05/09/2017.
 */

public class MeasurePicker {
    private static final int PIECE = 0;
    private static final int KG = 1;
    private static final int LB = 2;
    private static final int G = 3;
    private static final int LT = 4;
    private static final int OZ = 5;

    public static String getString(Resources res, int measure){
        int ref;
        switch (measure){

            case PIECE:
                ref = R.string.measure_piece;
                break;
            case KG:
                ref = R.string.measure_kg;
                break;
            case LB:
                ref = R.string.measure_lb;
                break;
            case G:
                ref = R.string.measure_g;
                break;
            case LT:
                ref = R.string.measure_lt;
                break;
            case OZ:
                ref = R.string.measure_oz;
                break;
            default:
                ref = R.string.measure_non_specified;
        }
        return res.getString(ref);
    }

    public static String[] getEntries(Resources res){
        String[] entries = new String[6];
        for (int i=0; i<6; i++){
            entries[i] = getString(res, i);
        }
        return entries;
    }
}
