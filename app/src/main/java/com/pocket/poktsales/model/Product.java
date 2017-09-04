package com.pocket.poktsales.model;

import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.Locale;

/**
 * Created by MAV1GA on 04/09/2017.
 */

public class Product extends SugarRecord {

    @Ignore
    public static final int ACTIVE = 0;
    @Ignore
    public static final int INACTIVE = 1;

    private String productName;
    private float productExistences;
    private int productMeasureUnit;
    private float productSellPrice;
    private int productStatus;

    private Department department;

    public String getProductName() {
        if (productName == null)
            return "";
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public float getProductExistences() {
        return productExistences;
    }

    public void setProductExistences(float productExistences) {
        this.productExistences = productExistences;
    }

    public int getProductMeasureUnit() {
        return productMeasureUnit;
    }

    public void setProductMeasureUnit(int productMeasureUnit) {
        this.productMeasureUnit = productMeasureUnit;
    }

    public float getProductSellPrice() {
        return productSellPrice;
    }

    public void setProductSellPrice(float productSellPrice) {
        this.productSellPrice = productSellPrice;
    }

    public int getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(int productStatus){
        if (productStatus == ACTIVE) {
            this.productStatus = productStatus;
        }else if (productStatus == INACTIVE) {
            this.productStatus = productStatus;
        }else {
            Log.w(getClass().getSimpleName(), String.format(Locale.getDefault(),
                    "Product status was expected to be 1 or 0. Setting 0 instead of %d", productStatus));
        }
    }
}
