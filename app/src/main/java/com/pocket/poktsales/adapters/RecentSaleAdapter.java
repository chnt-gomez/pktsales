package com.pocket.poktsales.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.pocket.poktsales.R;
import com.pocket.poktsales.model.MSale;
import com.pocket.poktsales.model.MTicket;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by MAV1GA on 03/01/2018.
 */

public class RecentSaleAdapter extends BaseAdapter {

    private Context context;
    private List<MTicket> items;

    public RecentSaleAdapter(@NonNull Context context) {
        this.context = context;
        items = new ArrayList<>();
    }

    public void addAll(List<MTicket> items){
        this.items.addAll(items);
    }

    public void clear(){
        if (items != null && items.size()>0){
            items.clear();
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.row_recent_sale, null);
        }
        MTicket ticket = getItem(position);
        TextView tvTicketId = convertView.findViewById(R.id.tv_ticket_id);
        TextView tvTicketReference = convertView.findViewById(R.id.tv_ticket_reference);
        TextView tvTicketTotal = convertView.findViewById(R.id.tv_ticket_total);
        if (ticket != null){
            tvTicketId.setText(String.format("# %d", ticket.id));
            tvTicketReference.setText(ticket.ticketReference);
            tvTicketTotal.setText(ticket.maskSaleTotal);
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return (items != null ? items.size() : 0);
    }

    @Override
    public MTicket getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        MTicket ticket = getItem(position);
        if (ticket != null)
            return ticket.id;
        return -1;
    }

    public void refreshItems(List<MTicket> items){
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }
}
