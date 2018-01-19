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
import com.pocket.poktsales.model.MTicket;
import com.pocket.poktsales.utils.Conversor;

import java.util.List;

/**
 * Created by MAV1GA on 08/09/2017.
 */

public class TabAdapter extends ArrayAdapter<MTicket> {
    public TabAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<MTicket> objects) {
        super(context, resource, objects);
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_layout_tab, null);
        }
        TextView tvTabName = (TextView)convertView.findViewById(R.id.tv_tab_reference);
        TextView tvTabTotal = (TextView)convertView.findViewById(R.id.tv_tab_total);
        MTicket tab = getItem(position);
        if (tab != null){
            tvTabName.setText(tab.ticketReference);
            tvTabTotal.setText(Conversor.asCurrency(tab.saleTotal));
        }
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        MTicket ticket = getItem(position);
        if (ticket != null)
            return ticket.id;
        return -1;
    }

    public void remove(long tabId) {
        for (int i = 0; i < getCount(); i++)
            if (getItemId(i) == tabId)
                remove(getItem(i));
    }
}
