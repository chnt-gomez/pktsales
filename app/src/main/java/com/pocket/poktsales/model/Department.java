package com.pocket.poktsales.model;

import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.Locale;

/**
 * Created by MAV1GA on 04/09/2017.
 */

public class Department extends SugarRecord {

    @Ignore
    public static final int ACTIVE = 0;
    @Ignore
    public static final int INACTIVE = 1;

    private String departmentName;
    private int departmentStatus;
    private int iconResource;
    private int colorResource;

    public Department(){}

    public String getDepartmentName() {
        if (departmentName == null)
            return "";
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getDepartmentStatus() {
        return departmentStatus;
    }

    public void setDepartmentStatus(int departmentStatus) {
        if (departmentStatus == ACTIVE) {
            this.departmentStatus = departmentStatus;
        }else if (departmentStatus == INACTIVE) {
            this.departmentStatus = departmentStatus;
        }else {
            Log.w(getClass().getSimpleName(), String.format(Locale.getDefault(),
                    "Product status was expected to be 1 or 0. Setting 0 instead of %d", departmentStatus));
        }
    }

    public int getIconResource() {
        return iconResource;
    }

    public void setIconResource(int iconResource) {
        this.iconResource = iconResource;
    }

    public int getColorResource() {
        return colorResource;
    }

    public void setColorResource(int colorResource) {
        this.colorResource = colorResource;
    }
}
