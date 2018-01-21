package com.pocket.poktsales.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pocket.poktsales.R;
import com.pocket.poktsales.model.MSale;
import com.pocket.poktsales.utils.Conversor;

import java.util.List;

/**
 * Created by vicente on 20/01/18.
 */

public class SimpleSaleAdapter extends ArrayAdapter<MSale>{

    public SimpleSaleAdapter(@NonNull Context context, int resource, @NonNull List<MSale> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_sale_row, null);
        }
        TextView tvQty = (TextView) convertView.findViewById(R.id.tv_product_qty);
        TextView tvSaleName = (TextView) convertView.findViewById(R.id.tv_sale_concept);
        TextView tvSaleTotal = (TextView) convertView.findViewById(R.id.tv_sale_total);

        MSale sale = getItem(position);
        if (sale != null){
            tvQty.setText(sale.productAmount);
            tvSaleName.setText(sale.saleConcept);
            tvSaleTotal.setText(Conversor.asCurrency(sale.saleTotal));
        }
        return convertView;

    }

    @Override
    public long getItemId(int position) {
        MSale sale = getItem(position);
        return sale != null ? sale.id: -1;
    }

    public void remove(long productId){
        for (int i=0; i<getCount(); i++)
            if (getItemId(i) == productId)
                remove(getItem(i));
    }
}
