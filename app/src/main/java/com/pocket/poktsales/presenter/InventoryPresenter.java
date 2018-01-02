package com.pocket.poktsales.presenter;

import com.pocket.poktsales.R;
import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.interfaces.RequiredViewOps;
import com.pocket.poktsales.model.MDepartment;
import com.pocket.poktsales.model.MProduct;

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
    public void createProduct(MProduct product) {
        if (!isProductNameInUse(product.productName)) {
            if (product.productName.equals("")) {
                view.onError(R.string.product_without_name);
                return;
            }
            Product product1 = new Product(product);
            product1.setProductStatus(Product.ACTIVE);
            product.id = product1.save();
            view.onSuccess();
            view.onProductAdded(product);
        }else{
            view.onError();
        }
    }

    @Override
    public List<MDepartment> getAllDepartments() {
        return fromDepartmentList(Department.find(Department.class, "department_status = ?",
                String.valueOf(Department.ACTIVE)));
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
    public MProduct getProduct(long productId) {
        Product product = findProductById(productId);
        if (product == null){
            view.onError("Can't find product");
            return new MProduct();
        }
        return fromProduct(product);
    }

    @Override
    public List<MProduct> getAllProducts() {
        return fromProductList(findAllProducts(Product.Sorting.NONE));
    }

    @Override
    public List<MProduct> searchProducts(String searchArg) {
        return fromProductList(searchProductsWithQuery(searchArg));
    }


    @Override
    public List<MProduct> getProductsInDepartment(MDepartment department) {
        return fromProductList(getProductsFromDepartment(department.id));
    }

    @Override
    public List<MProduct> getProductsInDepartment(long departmentId) {
        return fromProductList(getProductsFromDepartment(departmentId));
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
            view.onProductUpdated(fromProduct(product));
        }
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
