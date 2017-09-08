package com.pocket.poktsales.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import com.pocket.poktsales.model.Ticket;

import java.util.List;

/**
 * Created by MAV1GA on 08/09/2017.
 */

public class TabAdapter extends ArrayAdapter<Ticket> {
    public TabAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Ticket> objects) {
        super(context, resource, objects);
    }
}
