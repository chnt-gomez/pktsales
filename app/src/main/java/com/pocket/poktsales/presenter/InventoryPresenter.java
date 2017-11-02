package com.pocket.poktsales.presenter;

import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.interfaces.RequiredViewOps;
import com.pocket.poktsales.model.Department;
import com.pocket.poktsales.model.Product;

import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;

/**
 * Created by MAV1GA on 02/11/2017.
 */

public class InventoryPresenter extends BasePresenter implements RequiredPresenterOps.ProductPresenterOps {

    private RequiredViewOps view;

    public InventoryPresenter (RequiredViewOps view){
        this.view = view;
    }

    @Override
    public long createProduct(Product product) {
        if (!isProductNameInUse(product.getProductName())) {
            if (product.getProductName().equals("")){}
            //TODO: Name it with a custom label.
            product.save();
            return product.getId();
        }else{
            view.onError();
        }
        return -1;
    }

    @Override
    public List<Department> getAllDepartments() {
        return Department.find(Department.class, "department_status = ?", String.valueOf(Department.ACTIVE));
    }

    @Override
    public String getCategoryName(long categoryId) {
        return Department.findById(Department.class, categoryId).getDepartmentName();
    }

    @Override
    public void addProductToCategory(long productId, long categoryId) {
        Product product = Product.findById(Product.class, productId);
        product.setDepartment(Department.findById(Department.class, categoryId));
        product.save();
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
    public List<Product> getAllProducts(Product.Sorting sorting) {
        return findAllProducts(sorting);
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
    public Product updateProduct(long productId, String newProductArgs, float newPriceArgs, int newMeasure) {
        if (isProductNameInUse(newProductArgs, productId)){
            view.onError("Name in use");
        }else{
            Product product = findProductById(productId);
            product.setProductName(newProductArgs);
            product.setProductMeasureUnit(newMeasure);
            product.setProductSellPrice(newPriceArgs);
            product.save();
            return product;
        }
        return null;

    }

    @Override
    public void deactivateProduct(Product product) {
        product.setProductStatus(Product.INACTIVE);
        product.save();
    }

    @Override
    public void deactivateProduct(long productId) {
        Product product = findProductById(productId);
        if (product != null){
            product.setProductStatus(Product.INACTIVE);
            product.save();
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
            return product.getId();
        }else{
            view.onError("Can't find product");
            return -1;
        }
    }

    public Observable<List<Product>> getRxProducts(){
        return Observable.fromCallable(new Callable<List<Product>>(){
            @Override
            public List<Product> call() throws Exception {
                return getAllProducts(Product.Sorting.NONE);
            }
        });
    }
}
