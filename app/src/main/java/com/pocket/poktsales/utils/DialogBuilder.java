package com.pocket.poktsales.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.pocket.poktsales.R;
import com.pocket.poktsales.model.Product;

/**
 * Created by MAV1GA on 04/09/2017.
 */

public class DialogBuilder {

    static Dialog instance;

    public static Dialog newProductDialog(final Context context,
                                          final DialogInteractionListener.OnNewProductListener callback){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        @SuppressLint("InflateParams")
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_new_product, null);
        final Spinner spnProductMeasure = (Spinner)dialogView.findViewById(R.id.spn_product_measure);
        final EditText etProductName = (EditText)dialogView.findViewById(R.id.et_product_name);
        final EditText etProductPrice = (EditText)dialogView.findViewById(R.id.et_product_price);
        spnProductMeasure.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item,
                MeasurePicker.getEntries(context.getResources())));
        ImageButton positiveButton = (ImageButton)dialogView.findViewById(R.id.btn_ok);
        ImageButton negativeButton = (ImageButton)dialogView.findViewById(R.id.btn_cancel);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = new Product();
                product.setProductName(etProductName.getText().toString());
                product.setProductMeasureUnit(spnProductMeasure.getSelectedItemPosition());
                product.setProductSellPrice(Conversor.toFloat(etProductPrice.getText().toString()));
                if (instance != null)
                    instance.dismiss();
                callback.onNewProduct(product);
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instance.dismiss();
            }
        });
        builder.setView(dialogView);
        instance = builder.create();
        return instance;

    }

    public static class DialogInteractionListener{
        public interface OnNewProductListener{
            void onNewProduct(Product product);
        }
    }

}
