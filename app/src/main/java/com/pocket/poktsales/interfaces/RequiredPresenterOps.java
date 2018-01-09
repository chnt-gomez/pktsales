package com.pocket.poktsales.interfaces;

import android.content.Context;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.pocket.poktsales.model.MDepartment;
import com.pocket.poktsales.model.MProduct;
import com.pocket.poktsales.model.MTicket;
import com.pocket.poktsales.presenter.Ticket;

import java.util.List;


/**
 * Created by MAV1GA on 04/09/2017.
 */

public interface RequiredPresenterOps {

    interface ProductPresenterOps{
        void createProduct(MProduct product);
        MProduct getProduct(long productId);
        List<MProduct> getAllProducts();
        List<MProduct> searchProducts(String searchArg);
        List<MProduct> getProductsInDepartment(MDepartment department);
        List<MProduct> getProductsInDepartment(long departmentId);
        void updateProduct(long productId, String newProductName, float newProductPrice, int newMeasureUnit);
        void deactivateProduct(long productId);
        long reActivateProduct(long productId);
        List<MDepartment> getAllDepartments();
        void addProductToCategory(long productId, long categoryId);
        String getCategoryName(long categoryId);
    }

    interface TabPresenterOps{
        List<MTicket> getAllOpenTabs();
        void openTab(String tabReference);
    }

    interface SalePresenterOps{
        void addToSale(long ticketId, long productId);
        void applyTicket(long ticketId);
        List<MProduct> getProductsFromSearch(String args);
        List<MProduct> getProductsToSell();
        MTicket getTicket(long ticketId);
        List<MProduct> getProductsFromTab(long ticketId);
        void cancelTab(long ticketId);
        void removeFromSale(long ticketId, long id);
        long saveAsTemp(String productName, float productPrice);
    }

    interface DepartmentPresenterOps{
        void addNewDepartment(MDepartment department);
        List<MDepartment> getAllDepartments();
        void removeDepartment(long departmentId, long moveProductsToDepartmentId);
        MDepartment getDepartment(long id);
        int getProductCountFromDepartment(long departmentId);
        void updateDepartment(String newDepartmentArgs, long departmentId);
    }

    interface HomePresenterOps{
        float getDaySales();
        float getYesterdaySales();
        String getImprovement(Context context);
        List<MDepartment> getAllDepartments();
        float getSaleFromDepartment(long departmentId);
        float getSalesFromDay(int dayOfMonth);
        String getBestSellerOfTheDay();
        List<MTicket> getRecentSales();
    }

    interface QuickSalePresenterOps{
        List<MProduct> getAllProducts();
        List<MProduct> getProductsFromSearch(String searchArgs);
        MProduct getProductFromId(long id);
        void apply(List<MProduct> saleProducts);
        long saveAsTemp(String productName, float productPrice);
    }

    interface DayReportPresenterOps {
        List<MTicket> getTickets(long dateTimeStart, long dateTimeEnd);
        List<MTicket> getTickets(long dateTime);
        String getSaleOfTheDay(long dateTimeStart, long dateTimeEnd);
        String getSaleOfTheDay(long dateTime);
        float geTotalSalesAtTime(long from, long to);
        float getSalesFromDepartment(long departmentId, long from, long to);
        List<MDepartment> getAllActiveDepartments();
    }

    interface ReportsPresenterOps {
        float getMonthSales(int monthOfYear, int year);
        List<Entry> getMonthPerformance(int monthOfYear, int year);
        List<MDepartment> getDepartments();
        float getSaleByDepartment(long departmentId, int monthOfYear, int year);
        List<PieEntry> getMonthPerformanceByCategory(int monthOfYear, int year);
    }


}
