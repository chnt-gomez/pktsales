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
import com.pocket.poktsales.presenter.Product;

import java.util.List;

/**
 * Created by Vicente on 24/02/2018.
 */

public class SimpleTicketAdapter extends ArrayAdapter<MSale> {
    public SimpleTicketAdapter(@NonNull Context context, int resource, @NonNull List<MSale> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_simple_sale, null);
        }
        TextView tvProductAmount = (TextView)convertView.findViewById(R.id.tv_product_amount) ;
        TextView tvProductName = (TextView)convertView.findViewById(R.id.tv_product_name);
        TextView tvProductPrice = (TextView)convertView.findViewById(R.id.tv_product_price);
        View view = convertView.findViewById(R.id.color_view);

        MSale sale = getItem(position);
        if (sale == null){
            sale = new MSale();
        }
        if (sale.productId == 1000L){
            view.setBackgroundColor(getContext().getResources().getColor(R.color.colorSubtleGray));
        }else{
            view.setBackgroundColor(getContext().getResources().getColor(R.color.colorAccentDark));
        }
        tvProductAmount.setText(sale.productAmount);
        String sConcept = sale.saleConcept;
        if (sConcept == null){
            sConcept = getContext().getString(R.string.unknown);
        }
        tvProductName.setText(sConcept);
        tvProductPrice.setText(sale.maskSaleTotal);
        return convertView;
    }
}
