package com.pocket.poktsales.presenter;

import android.util.Log;

import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.interfaces.RequiredViewOps;
import com.pocket.poktsales.model.Department;
import com.pocket.poktsales.model.Product;

import java.util.List;

/**
 * Created by MAV1GA on 04/09/2017.
 */

public class SalesPresenter implements RequiredPresenterOps.ProductPresenterOps {

    private static SalesPresenter instance;
    private RequiredViewOps view;
    private SalesPresenter(){}

    public synchronized static SalesPresenter getInstance(RequiredViewOps view){
        if (instance == null)
            instance = new SalesPresenter();
        setViewOps(view);
        return  instance;
    }

    private static void setViewOps(RequiredViewOps view){
        instance.view = view;
    }

    /*
    Methods available ony for Product-related activities
     */

    @Override
    public long createProduct(Product product) {
        if (!isProductNameInUse(product.getProductName())) {
            product.save();
            view.onSuccess();
            return product.getId();
        }else{
            view.onError();
        }
        return -1;
    }

    @Override
    public Product getProduct(String productName) {
        Product product = findProductByName(productName);
        if (product == null) {
            view.onError("Cant find product");
            return new Product();
        }
        return product;
    }

    @Override
    public Product getProduct(long productId) {
        Product product = findProductById(productId);
        if (product == null){
            view.onError("Can't find product");
            return new Product();
        }
        return product;
    }

    @Override
    public List<Product> getAllProducts() {
        return findAllProducts();
    }

    @Override
    public List<Product> searchProducts(String searchArg) {
        return searchProductsWithQuery(searchArg);
    }


    @Override
    public List<Product> getProductsInDepartment(Department department) {
        return getProductsFromDepartment(department.getId());
    }

    @Override
    public List<Product> getProductsInDepartment(long departmentId) {
        return getProductsFromDepartment(departmentId);
    }

    @Override
    public Product updateProduct(Product newProductArgs) {
        if (isProductNameInUse(newProductArgs.getProductName())){
            view.onError("Name in use");
        }else{
            newProductArgs.save();
            view.onSuccess();
        }
        return newProductArgs;
    }

    @Override
    public void deactivateProduct(Product product) {
        product.setProductStatus(Product.INACTIVE);
        product.save();
        view.onSuccess();
    }

    @Override
    public void deactivateProduct(long productId) {
        Product product = findProductById(productId);
        if (product != null){
            product.setProductStatus(Product.INACTIVE);
            product.save();
            view.onSuccess();
        }else{
            view.onError("Can't find product");
        }
    }

    @Override
    public long reActivateProduct(long productId) {
        Product product = findProductById(productId);
        if (product != null) {
            product.setProductStatus(Product.ACTIVE);
            product.save();
            view.onSuccess();
            return product.getId();
        }else{
            view.onError("Can't find product");
            return -1;
        }
    }







    /*
    Internal Methods
     */

    private boolean isProductNameInUse(String productName){
        return Product.find(Product.class, "product_name = ?", productName).size() >= 1;
    }

    private Product findProductByName(String name){
        try {
            return Product.find(Product.class, "product_name = ?", name).get(0);
        }catch (Exception e){
            Log.w("findProductByName()", String.format("Can't find product with name: %s", name));
            return null;
        }
    }

    private Product findProductById(long id){
        try{
            return Product.findById(Product.class, id);
        }catch(Exception e){
            Log.w("findProductById()", String.format("Can't find product with id: %d", id));
            return null;
        }
    }

    private List<Product> findAllProducts() {
        return Product.listAll(Product.class);
    }

    private static String formatForQuery(String rawQuery){
        return rawQuery.replace(" ", "%");
    }

    private List<Product> searchProductsWithQuery(String searchArg) {
        return Product.find(Product.class, "product_name LIKE ?", "%"+formatForQuery(searchArg)+"%");
    }

    private List<Product> getProductsFromDepartment(Long id) {
        return Product.find(Product.class, "department = ?", String.valueOf(id));
    }
}
