package com.pocket.poktsales.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.adapters.ArraySwipeAdapter;
import com.pocket.poktsales.R;
import com.pocket.poktsales.model.MSale;
import com.pocket.poktsales.utils.Conversor;

import java.util.List;

/**
 * Created by vicente on 20/01/18.
 */

public class SimpleSaleAdapter extends ArraySwipeAdapter<MSale>{

    ViewOperations view;

    public SimpleSaleAdapter(Context context, int resource, List<MSale> objects) {
        super(context, resource, objects);
        view = (ViewOperations)context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_sale_row, null);
        }
        TextView tvQty = (TextView) convertView.findViewById(R.id.tv_product_qty);
        TextView tvSaleName = (TextView) convertView.findViewById(R.id.tv_sale_concept);
        TextView tvSaleTotal = (TextView) convertView.findViewById(R.id.tv_sale_total);

        MSale sale = (MSale)getItem(position);
        if (sale != null){
            tvQty.setText(sale.productAmount);
            tvSaleName.setText(sale.saleConcept);
            tvSaleTotal.setText(Conversor.asCurrency(sale.saleTotal));
        }
        final int pos = position;
        convertView.findViewById(R.id.btn_confirm_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("OnClick", String.format("onClick: &d", pos));
            }
        });
        return convertView;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public interface ViewOperations{
        void requestDelete(long productId);
    }
}
