package com.pocket.poktsales.presenter;

import android.util.Log;

import com.pocket.poktsales.model.Product;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by MAV1GA on 02/11/2017.
 */

class BasePresenter {

    long getTodayStart(){
        return new DateTime().withTimeAtStartOfDay().getMillis();

    }

    long getTodayEnd(){
        return new DateTime().withTime(23,59,59,999).getMillis();
    }

    boolean isProductNameInUse(String productName){
        String args[] = {productName, String.valueOf(Product.ACTIVE)};
        return Product.find(Product.class, "product_name LIKE ? AND product_status = ?", args).size() >= 1;
    }

    Product findProductById(long id){
        try{
            return Product.findById(Product.class, id);
        }catch(Exception e){
            Log.wtf("findProductById()", String.format("Can't find product with id: %d", id));
            return null;
        }
    }

    List<Product> findAllProducts(Product.Sorting sorting) {
        String args[] = {String.valueOf(Product.ACTIVE)};
        if (sorting == Product.Sorting.ALPHABETICAL)
            return Product.find(Product.class, "product_status = ?", args, null, "product_name", null);
        if (sorting == Product.Sorting.PRICE)
            return Product.find(Product.class, "product_status = ?", args, null, "product_sell_price", null);
        return Product.find(Product.class, "product_status = ?", String.valueOf(Product.ACTIVE));
    }

    List<Product> searchProductsWithQuery(String searchArg) {
        return Product.find(Product.class, "product_name LIKE ? AND product_status = ?", "%"+formatForQuery(searchArg)+"%",
                String.valueOf(Product.ACTIVE));
    }

    List<Product> getProductsFromDepartment(long id) {
        return Product.find(Product.class, "department = ? AND product_status = ?", String.valueOf(id)
                , String.valueOf(Product.ACTIVE));
    }

    boolean isProductNameInUse(String productName, long productId){
        String args[] = {productName, String.valueOf(Product.ACTIVE)};
        for (Product p : Product.find(Product.class, "product_name = ? AND product_status = ?", args)){
            if (p.getId() != productId)
                return true;
        }
        return false;
    }

    private static String formatForQuery(String rawQuery){
        return rawQuery.replace(" ", "%");
    }

}
