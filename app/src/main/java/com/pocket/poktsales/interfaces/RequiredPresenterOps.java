package com.pocket.poktsales.interfaces;

import com.pocket.poktsales.model.Department;
import com.pocket.poktsales.model.Product;

import java.util.List;

/**
 * Created by MAV1GA on 04/09/2017.
 */

public interface RequiredPresenterOps {

    interface ProductPresenterOps{
        long createProduct(Product product);
        Product getProduct(String productName);
        Product getProduct(long productId);
        List<Product> getAllProducts(Product.Sorting sorting);
        List<Product> searchProducts(String searchArg);
        List<Product> getProductsInDepartment(Department department);
        List<Product> getProductsInDepartment(long departmentId);
        Product updateProduct(Product newProductArgs);
        void deactivateProduct(Product product);
        void deactivateProduct(long productId);
        long reActivateProduct(long productId);
        List<Department> getAllDepartments();
    }
}
