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
import com.pocket.poktsales.model.MProduct;
import com.pocket.poktsales.utils.Conversor;

import java.util.List;

/**
 * Created by MAV1GA on 05/09/2017.
 */

public class SimpleProductAdapter extends ArrayAdapter<MProduct> {
    public SimpleProductAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<MProduct> objects) {
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

        MProduct product = getItem(position);
        if (product == null){
            product = new MProduct();
        }
        tvProductName.setText(product.productName);
        tvProductPrice.setText(product.maskProductSellPrice);

        return convertView;
    }

    public void updateProduct(MProduct product){
        for (int i=0; i<getCount(); i++)
            if (getItemId(i) == product.id)
                remove(getItemId(i));

    }

    @Override
    public long getItemId(int position) {
        MProduct product = getItem(position);
        return product != null ? product.id: -1;
    }

    public void remove(long productId){
        for (int i=0; i<getCount(); i++)
            if (getItemId(i) == productId)
                remove(getItem(i));
    }

}
