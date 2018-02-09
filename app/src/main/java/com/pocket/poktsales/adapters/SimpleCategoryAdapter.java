package com.pocket.poktsales.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pocket.poktsales.R;
import com.pocket.poktsales.model.MDepartment;

import java.util.List;
import java.util.Locale;

/**
 * Created by MAV1GA on 02/10/2017.
 */

public class SimpleCategoryAdapter extends ArrayAdapter<MDepartment> {

    public SimpleCategoryAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<MDepartment> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_department_item, null);
        }
        MDepartment department = getItem(position);
        if (department == null) {
            Log.wtf("CategoryAdapter:", String.format(Locale.getDefault(), "The department at %d was null!!", position));
            department = new MDepartment();
        }
        TextView tvCategoryName = (TextView)convertView.findViewById(R.id.tv_department_name);

        tvCategoryName.setText(department.departmentName);
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        MDepartment department = getItem(position);
        if (department != null){
            return department.id;
        }
        Log.wtf("CategoryAdapter:", String.format(Locale.getDefault(), "The department at %d was null!!", position));
        return -1L;
    }
}
