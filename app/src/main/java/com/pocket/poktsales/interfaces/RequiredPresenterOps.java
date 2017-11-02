package com.pocket.poktsales.interfaces;

import android.content.Context;

import com.pocket.poktsales.model.Department;
import com.pocket.poktsales.model.Product;
import com.pocket.poktsales.model.Ticket;

import java.util.List;

import rx.Observable;


/**
 * Created by MAV1GA on 04/09/2017.
 */

public interface RequiredPresenterOps {

    interface ProductPresenterOps{
        long createProduct(Product product);
        Product getProduct(long productId);
        List<Product> getAllProducts(Product.Sorting sorting);
        List<Product> searchProducts(String searchArg);
        List<Product> getProductsInDepartment(Department department);
        List<Product> getProductsInDepartment(long departmentId);
        Product updateProduct(long productId, String newProductName, float newProductPrice, int newMeasureUnit);
        void deactivateProduct(Product product);
        void deactivateProduct(long productId);
        long reActivateProduct(long productId);
        List<Department> getAllDepartments();
        void addProductToCategory(long productId, long categoryId);
        String getCategoryName(long categoryId);
    }

    interface TabPresenterOps{
        List<Ticket> getAllOpenTabs();
        void openTab(Ticket tabReference);
    }

    interface SalePresenterOps{
        void addToSale(long ticketId, long productId);
        void applyTicket(long ticketId);
        List<Product> getProductsFromSearch(String args);
        Ticket getTicket(long ticketId);
        List<Product> getProductsFromTab(long ticketId);
        void cancelTab(long ticketId);
        void removeFromSale(long ticketId, long id);
        void addToSale(long ticketId, Product product);
    }

    interface DepartmentPresenterOps{
        void addNewDepartment(Department department);
        List<Department> getAllDepartments();
        void removeDepartment(long departmentId, long moveProductsToDepartmentId);
        Department getDepartment(long id);
        int getProductCountFromDepartment(long departmentId);
    }

    interface HomePresenterOps{
        float getDaySales();
        float getYesterdaySales();
        String getImprovement(Context context);
        List<Department> getAllDepartments();
        float getSaleFromDepartment(long departmentId);
        float getSalesFromDay(int dayOfMonth);
        String getBestSellerOfTheDay();

        /* Rx Methods now */
        Observable<List<Department>> getDepartments();
    }
}
