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
import com.pocket.poktsales.model.MTicket;


import java.util.List;

/**
 * Created by MAV1GA on 03/01/2018.
 */

public class RecentSaleAdapter extends ArrayAdapter<MTicket> {

    public RecentSaleAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_recent_sale, null);
        }
        MTicket ticket = getItem(position);
        TextView tvTicketId = (TextView)convertView.findViewById(R.id.tv_ticket_id);
        TextView tvTicketReference = (TextView)convertView.findViewById(R.id.tv_ticket_reference);
        TextView tvTicketTotal = (TextView)convertView.findViewById(R.id.tv_ticket_total);
        if (ticket != null){
            tvTicketId.setText(String.format("# %d", ticket.id));
            tvTicketReference.setText(ticket.ticketReference);
            tvTicketTotal.setText(ticket.maskSaleTotal);
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
}
