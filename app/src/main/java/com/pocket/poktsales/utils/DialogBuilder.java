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
import android.widget.TextView;

import com.pocket.poktsales.R;
import com.pocket.poktsales.adapters.DropDownDepartmentAdapter;
import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.model.Department;
import com.pocket.poktsales.model.Product;
import com.pocket.poktsales.presenter.SalesPresenter;

import java.util.List;

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
        builder.setView(dialogView);
        instance = builder.create();
        return instance;

    }

    public static Dialog sortProductsDialog(final Context context,
                                          final DialogInteractionListener.OnSortProductsListener callback){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        @SuppressLint("InflateParams")
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_sort_products, null);


        ImageButton positiveButton = (ImageButton)dialogView.findViewById(R.id.btn_ok);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (instance != null)
                    instance.dismiss();
                //callback.onSortProducts();
            }
        });
        builder.setView(dialogView);
        instance = builder.create();
        return instance;

    }


    public static Dialog seeProductDialog(final Context context, final Product product,
                                          RequiredPresenterOps.ProductPresenterOps presenterOps,
                                          final DialogInteractionListener.OnSaveProductListener callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        @SuppressLint("InflateParams")
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_see_product, null);
        /*
        Init widgets
         */
        final TextView tvProductName = (TextView) dialogView.findViewById(R.id.tv_product_name);
        final Spinner spnProductMeasure = (Spinner) dialogView.findViewById(R.id.spn_product_measure);
        final EditText etProductName = (EditText) dialogView.findViewById(R.id.et_product_name);
        final EditText etProductPrice = (EditText) dialogView.findViewById(R.id.et_product_price);
        final Spinner spnDepartments = (Spinner) dialogView.findViewById(R.id.spn_product_department);
        spnProductMeasure.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item,
                MeasurePicker.getEntries(context.getResources())));
        List<Department>deps = presenterOps.getAllDepartments();
        if (deps != null && deps.size() > 0){
            spnDepartments.setAdapter(new DropDownDepartmentAdapter(context, R.layout.dropdown_department_item,
                    presenterOps.getAllDepartments()));
        }else{
            spnDepartments.setVisibility(View.GONE);
        }


        /*
        Set values
         */
        tvProductName.setText(product.getProductName());
        etProductName.setText(product.getProductName());
        etProductPrice.setText(Conversor.asCurrency(product.getProductSellPrice()));
        spnProductMeasure.setSelection(product.getProductMeasureUnit());

        ImageButton positiveButton = (ImageButton) dialogView.findViewById(R.id.btn_ok);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product.setProductName(etProductName.getText().toString());
                product.setProductMeasureUnit(spnProductMeasure.getSelectedItemPosition());
                product.setProductSellPrice(Conversor.toFloat(etProductPrice.getText().toString()));
                if (instance != null)
                    instance.dismiss();

                callback.onSaveProduct(product);
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
        public interface OnSaveProductListener{
            void onSaveProduct(Product product);
        }
        public interface OnSortProductsListener{
            void onSortProducts(long departmentId, Product.Sorting sorting);
        }
    }

}
