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
    @Ignore
    public static final int INVENTORY_YES = 1;
    @Ignore
    public static final int INVENTORY_NO = 0;
    @Ignore
    public static final int TEMPORARY = 3;

    private String productName;
    private float productExistences;
    private int productMeasureUnit;
    private float productSellPrice;
    private int productStatus;
    private int productInventory;

    public String getCodeBar() {
        return codeBar;
    }

    public void setCodeBar(String codeBar) {
        this.codeBar = codeBar;
    }

    private String codeBar;
    private Department department;

    public Product (){}

    public int getProductInventory() {
        return productInventory;
    }

    public void setProductInventory(int productInventory) {
        if (productInventory == INVENTORY_YES) {
            this.productInventory = productInventory;
        }else if (productInventory == INVENTORY_NO) {
            this.productInventory = productInventory;
        }else {
            Log.w(getClass().getSimpleName(), String.format(Locale.getDefault(),
                    "Product inventory was expected to be 1 or 0. Setting 0 instead of %d", productInventory));
        }
    }

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
        }else if(productStatus == TEMPORARY) {
            this.productStatus = productStatus;
        }else{
            Log.w(getClass().getSimpleName(), String.format(Locale.getDefault(),
                    "Product status was expected to be 3, 1 or 0. Setting 0 instead of %d", productStatus));
        }
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Department getDepartment() {
        return department;
    }

    public enum Sorting {
        ALPHABETICAL, PRICE, NONE
    }
}
