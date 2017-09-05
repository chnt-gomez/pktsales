package com.pocket.poktsales.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pocket.poktsales.R;
import com.pocket.poktsales.model.Product;
import com.pocket.poktsales.utils.Conversor;

import java.util.List;

/**
 * Created by MAV1GA on 05/09/2017.
 */

public class SimpleProductAdapter extends ArrayAdapter<Product> {
    public SimpleProductAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Product> objects) {
        super(context, resource, objects);
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_simple_product, null);
        }
        TextView tvProductName = (TextView)convertView.findViewById(R.id.tv_product_name);
        TextView tvProductPrice = (TextView)convertView.findViewById(R.id.tv_product_price);

        Product product = getItem(position);
        if (product == null){
            product = new Product();
        }
        tvProductName.setText(product.getProductName());
        tvProductPrice.setText(Conversor.asCurrency(product.getProductSellPrice()));

        return convertView;
    }
}
