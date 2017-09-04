package com.pocket.poktsales.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.pocket.poktsales.R;
import com.pocket.poktsales.model.Product;

/**
 * Created by MAV1GA on 04/09/2017.
 */

public class DialogBuilder {

    public static Dialog newProductDialog(final Context context,
                                          DialogInteractionListener.OnNewProductListener callback){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        @SuppressLint("InflateParams")
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_new_product, null);


        builder.setView(dialogView);
        return builder.create();

    }

    public static class DialogInteractionListener{
        public interface OnNewProductListener{
            void onNewProduct(Product product);
        }
    }

}
