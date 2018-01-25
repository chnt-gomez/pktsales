package com.pocket.poktsales.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.daimajia.swipe.adapters.ArraySwipeAdapter;
import com.pocket.poktsales.R;
import com.pocket.poktsales.model.MProduct;

import java.util.List;

/**
 * Created by MAV1GA on 25/01/2018.
 */

public class PickToSellProductAdapter extends ArraySwipeAdapter<MProduct> {
    PickToSellProductAdapter.ViewOperations view;

    public PickToSellProductAdapter(Context context, int resource, List<MProduct> objects) {
        super(context, resource, objects);
        view = (PickToSellProductAdapter.ViewOperations)context;
    }

    @Override
    public long getItemId(int position) {
        MProduct sale = (MProduct)getItem(position);
        if (sale != null)
            return sale.id;
        return -1;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_pick_product, null);
        }
        TextView tvProductName = (TextView) convertView.findViewById(R.id.tv_product_name);
        TextView tvProductPrice = (TextView) convertView.findViewById(R.id.tv_product_price);

        MProduct product = (MProduct)getItem(position);
        if (product != null){

            tvProductName.setText(product.productName);
            tvProductPrice.setText(product.maskProductSellPrice);
        }
        final int pos = position;
        convertView.findViewById(R.id.btn_add_many).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.requestAddMany(getItemId(position));
            }
        });
        convertView.findViewById(R.id.btn_add_one).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                view.addOne(getItemId(position));
            }
        });
        return convertView;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public interface ViewOperations{
        void addOne(long productId);
        void requestAddMany(long productId);
    }
}
