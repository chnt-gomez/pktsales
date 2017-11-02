package com.pocket.poktsales.presenter;

import android.content.Context;
import android.util.Log;

import com.pocket.poktsales.R;
import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.interfaces.RequiredViewOps;
import com.pocket.poktsales.model.Department;
import com.pocket.poktsales.model.Product;
import com.pocket.poktsales.model.Sale;
import com.pocket.poktsales.model.Ticket;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

/**
 * Created by MAV1GA on 04/09/2017.
 */

public class SalesPresenter implements RequiredPresenterOps.TabPresenterOps,
        RequiredPresenterOps.SalePresenterOps, RequiredPresenterOps.DepartmentPresenterOps{

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





    /*
    TAB OPERATIONS ---------------------------------------------------------------------------------------
     */

    @Override
    public List<Ticket> getAllOpenTabs() {
        return getAllTickets();
    }

    @Override
    public void openTab(Ticket tabReference) {
        if (!isTicketInUse(tabReference.getTicketReference())) {
            tabReference.setTicketStatus(Ticket.TICKET_PENDING);
            tabReference.save();
            view.onSuccess();
        }else{
            view.onError();
        }
    }

    /*
    Sales Operations --------------------------------------------------------------------------------
     */

    @Override
    public void addToSale(long ticketId, long productId) {
        Product product = Product.findById(Product.class, productId);
        Ticket ticket = Ticket.findById(Ticket.class, ticketId);
        Sale sale = new Sale();
        sale.setProduct(product);
        sale.setTicket(ticket);
        sale.setProductAmount(1F);
        sale.setSaleConcept(product.getProductName());
        sale.setSaleTotal(product.getProductSellPrice()*sale.getProductAmount());
        sale.save();
        float newTotal = ticket.getSaleTotal() + sale.getSaleTotal();
        ticket.setSaleTotal(newTotal);
        ticket.save();
        view.onSuccess();
    }

    @Override
    public void addToSale(long ticketId, Product product) {
        product.setProductStatus(Product.TEMPORARY);
        product.save();
        addToSale(ticketId, product.getId());
    }

    @Override
    public void applyTicket(long ticketId) {
        Ticket ticket = Ticket.findById(Ticket.class, ticketId);
        ticket.setTicketStatus(Ticket.TICKET_APPLIED);
        ticket.setDateTime(DateTime.now().getMillis());
        ticket.save();
    }

    @Override
    public List<Product> getProductsFromSearch(String args) {
        return null;
    }

    @Override
    public Ticket getTicket(long ticketId) {
        return Ticket.findById(Ticket.class, ticketId);
    }

    @Override
    public List<Product> getProductsFromTab(long ticketId) {

        List<Sale> sales = Sale.find(Sale.class, "ticket = ?", String.valueOf(ticketId));
        List <Product> products = new ArrayList<>();
        for (Sale s : sales){
            products.add(s.getProduct());
        }
        return products;
    }

    @Override
    public void cancelTab(long ticketId) {
        Ticket ticket = Ticket.findById(Ticket.class, ticketId);
        ticket.setTicketStatus(Ticket.TICKET_CANCELED);
        ticket.save();
        view.onSuccess();
    }

    @Override
    public void removeFromSale(long ticketId, long productId) {
        Ticket ticket = Ticket.findById(Ticket.class, ticketId);
        String args[] = {String.valueOf(productId), String.valueOf(ticketId)};
        for (Sale s : Sale.find(Sale.class, "product = ? AND ticket = ?", args,
                null, null, String.valueOf(1))){
            float saleTotal = s.getSaleTotal();
            float ticketTotal = ticket.getSaleTotal()-saleTotal;
            ticket.setSaleTotal(ticketTotal);
            s.delete();
            ticket.save();
        }
        view.onSuccess();
    }

    /*
    Department Operations
     */

    @Override
    public void addNewDepartment(Department department) {
        if (!isDepartmentNameInUse(department.getDepartmentName())) {
            if (department.getDepartmentName().equals("")){
                //TODO: add a custom name
            }
            department.save();
            view.onSuccess();
        }else{
            view.onError();
        }
    }

    @Override
    public List<Department> getAllDepartments() {
        return Department.listAll(Department.class);
    }

    @Override
    public void removeDepartment(long departmentId, long moveProductsToDepartmentId) {

    }

    @Override
    public Department getDepartment(long id) {
        return Department.findById(Department.class, id);
    }

    @Override
    public int getProductCountFromDepartment(long departmentId) {
        String args[] = {String.valueOf(departmentId)};
        return (int) Product.count(Product.class, "department = ?", args);
    }

    /*
    Home Methods
     */



    /*
    Internal Methods
     */

    private boolean isTicketInUse(String ticketReference){
        return Ticket.find(Ticket.class, "ticket_reference LIKE ? AND ticket_status = ?", ticketReference,
                String.valueOf(Ticket.TICKET_PENDING)).size() >= 1;
    }


    private Product findProductByName(String name){
        try {
            return Product.find(Product.class, "product_name = ?", name).get(0);
        }catch (Exception e){
            Log.w("findProductByName()", String.format("Can't find product with name: %s", name));
            return null;
        }
    }


    private boolean isDepartmentNameInUse(String departmentName) {
        return Department.find(Department.class, "department_name LIKE ?", departmentName).size() >= 1;
    }



    private static String formatForQuery(String rawQuery){
        return rawQuery.replace(" ", "%");
    }

    private List<Product> searchProductsWithQuery(String searchArg) {
        return Product.find(Product.class, "product_name LIKE ? AND product_status = ?", "%"+formatForQuery(searchArg)+"%",
                String.valueOf(Product.ACTIVE));
    }

    private List<Product> getProductsFromDepartment(long id) {
        return Product.find(Product.class, "department = ? AND product_status = ?", String.valueOf(id)
        , String.valueOf(Product.ACTIVE));
    }

    private List<Ticket> getAllTickets(){
        return Ticket.find(Ticket.class, "ticket_status = ?",
                String.valueOf(Ticket.TICKET_PENDING));
    }


}
