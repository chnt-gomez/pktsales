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
import com.pocket.poktsales.model.MDepartment;

import java.util.List;

/**
 * Created by vicente on 6/09/17.
 */

public class DropDownDepartmentAdapter extends ArrayAdapter<MDepartment> {
    public DropDownDepartmentAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<MDepartment> objects) {
        super(context, resource, objects);
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dropdown_department_item, null);
        TextView tvDepartmentName = (TextView)convertView.findViewById(R.id.tv_item);
        MDepartment department = getItem(position);
        if (department != null)
            tvDepartmentName.setText(department.departmentName);
        return convertView;
    }

    
    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dropdown_department_item, null);
        TextView tvDepartmentName = (TextView)convertView.findViewById(R.id.tv_item);
        MDepartment department = getItem(position);
        if (department != null)
            tvDepartmentName.setText(department.departmentName);
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        MDepartment department = getItem(position);
        return department != null ? department.id : -1;
    }
}
