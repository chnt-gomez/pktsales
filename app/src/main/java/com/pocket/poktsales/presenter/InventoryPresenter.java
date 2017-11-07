package com.pocket.poktsales.presenter;

import com.pocket.poktsales.R;
import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.interfaces.RequiredViewOps;
import com.pocket.poktsales.model.Department;
import com.pocket.poktsales.model.Product;

import java.util.List;


/**
 * Created by MAV1GA on 02/11/2017.
 */

public class InventoryPresenter extends BasePresenter implements RequiredPresenterOps.ProductPresenterOps {

    private RequiredViewOps.InventoryViewOps view;

    public InventoryPresenter (RequiredViewOps.InventoryViewOps view){
        this.view = view;
    }

    @Override
    public void createProduct(Product product) {
        if (!isProductNameInUse(product.getProductName())) {
            if (product.getProductName().equals("")) {
                view.onError(R.string.product_without_name);
                return;
            }
            product.save();
            view.onSuccess();
            view.onProductAdded(product);
        }else{
            view.onError();
        }
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
    public void updateProduct(long productId, String newProductArgs, float newPriceArgs, int newMeasure) {
        if (isProductNameInUse(newProductArgs, productId)){
            view.onError(R.string.product_in_use);
        }else{
            Product product = findProductById(productId);
            product.setProductName(newProductArgs);
            product.setProductMeasureUnit(newMeasure);
            product.setProductSellPrice(newPriceArgs);
            product.save();
            view.onProductUpdated(product);
        }
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
            view.onProductDeleted(product.getId());
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
}
