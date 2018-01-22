package com.pocket.poktsales.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.pocket.poktsales.R;
import com.pocket.poktsales.model.MSale;
import com.pocket.poktsales.utils.Conversor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vicente on 20/01/18.
 */

public class SimpleSaleAdapter extends BaseSwipeAdapter{

    private Context context;
    private List<MSale> items;

    public SimpleSaleAdapter(Context context){
        this.context = context;
        items = new ArrayList<>();
    }

    public void addAll(List<MSale> items){
        if (this.items != null)
            this.items.addAll(items);
    }

    public void clear(){
        this.items.clear();
    }

    public void add(MSale sale){
        items.add(sale);
    }

    public void remove(long id){
        //
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.simple_sale_row, null);
    }

    @Override
    public void fillValues(int position, View convertView) {
        TextView tvQty = (TextView) convertView.findViewById(R.id.tv_product_qty);
        TextView tvSaleName = (TextView) convertView.findViewById(R.id.tv_sale_concept);
        TextView tvSaleTotal = (TextView) convertView.findViewById(R.id.tv_sale_total);

        MSale sale = (MSale)getItem(position);
        if (sale != null){
            tvQty.setText(sale.productAmount);
            tvSaleName.setText(sale.saleConcept);
            tvSaleTotal.setText(Conversor.asCurrency(sale.saleTotal));
        }
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        if (items != null)
            return items.get(i);
        return null;
    }

    @Override
    public long getItemId(int position) {
        MSale sale = (MSale)getItem(position);
        return sale != null ? sale.id: -1;
    }


}
