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
import java.util.List;
import java.util.Locale;

/**
 * Created by MAV1GA on 04/09/2017.
 */

public class SalesPresenter implements RequiredPresenterOps.ProductPresenterOps, RequiredPresenterOps.TabPresenterOps,
        RequiredPresenterOps.SalePresenterOps, RequiredPresenterOps.DepartmentPresenterOps,
        RequiredPresenterOps.HomePresenterOps{

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
            if (product.getProductName().equals("")){}
                //TODO: Name it with a custom label.
            product.save();
            view.onSuccess();
            return product.getId();
        }else{
            view.onError();
        }
        return -1;
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
        view.onSuccess();
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
            view.onSuccess();
            return product;
        }
        return null;

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
        for (Product p : getProductsFromTab(ticketId)){
            if (p.getProductStatus() == Product.TEMPORARY){

            }
        }
    }

    @Override
    public List<Product> getProductsFromSearch(String args) {
        if (args.equals(""))
            return getAllProducts(Product.Sorting.NONE);
        return searchProducts(args);
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

    @Override
    public float getDaySales() {
        String todayStart  = String.valueOf(new DateTime().withTimeAtStartOfDay().getMillis());
        String todayEnd = String.valueOf(new DateTime().withTime(23,59,59,999));
        float total = 0F;
        for (Ticket t : Ticket.find(Ticket.class, "date_time >= ? AND date_time < ?",
                todayStart, todayEnd)) {
            total += t.getSaleTotal();
        }
        return total;
    }

    @Override
    public float getSalesFromDay(int dayOfMonth) {
        float total = 0;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH)+1;
        long start = new DateTime(year, month, dayOfMonth, 0,0,0).getMillis();
        long end = new DateTime(year, month, dayOfMonth, 23,59,59).getMillis();
        String args[] = {String.valueOf(start), String.valueOf(end)};
        for (Ticket t : Ticket.find(Ticket.class, "date_time >= ? and date_time <= ?", args, null, null, null)){
            total += t.getSaleTotal();
        }
        return total;
    }

    @Override
    public float getSaleFromDepartment(long departmentId) {
        float total = 0;
        for (Sale s : Sale.findWithQuery(
                Sale.class, "SELECT * from Sale s, Product p, Department d WHERE " +
                        "p.department = d.id AND d.id = ? AND s.product = p.id", String.valueOf(departmentId)
        )){
            total += s.getSaleTotal();
        }
        return total;
    }

    @Override
    public float getYesterdaySales() {
        String yesterdayStart  = String.valueOf(new DateTime().minusDays(1).withTimeAtStartOfDay().getMillis());
        String yesterdayEnd = String.valueOf(new DateTime().minusDays(1).withTime(23,59,59,999).getMillis());
        float total = 0F;
        for (Ticket t : Ticket.find(Ticket.class, "date_time >= ? AND date_time < ?",
                yesterdayStart, yesterdayEnd)) {
            total += t.getSaleTotal();
        }
        return total;
    }

    @Override
    public String getImprovement(Context context) {
        float overHand =  getDaySales() - getYesterdaySales();
        if (overHand > 0){
            float demiHand = overHand / getYesterdaySales();
            demiHand *= 100;
            return String.format(Locale.getDefault(), context.getString(R.string.improvement_positive), demiHand);
        }else{
            return String.format(Locale.getDefault(), context.getString(R.string.improvement_negative), overHand);
        }
    }

    /*
    Internal Methods
     */

    private boolean isTicketInUse(String ticketReference){
        return Ticket.find(Ticket.class, "ticket_reference LIKE ? AND ticket_status = ?", ticketReference,
                String.valueOf(Ticket.TICKET_PENDING)).size() >= 1;
    }

    private boolean isProductNameInUse(String productName){
        String args[] = {productName, String.valueOf(Product.ACTIVE)};
        return Product.find(Product.class, "product_name LIKE ? AND product_status = ?", args).size() >= 1;
    }

    private boolean isProductNameInUse(String productName, long productId){
        String args[] = {productName, String.valueOf(Product.ACTIVE)};
        for (Product p : Product.find(Product.class, "product_name = ? AND product_status = ?", args)){
            if (p.getId() != productId)
                return true;
        }
        return false;
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

    private boolean isDepartmentNameInUse(String departmentName) {
        return Department.find(Department.class, "department_name LIKE ?", departmentName).size() >= 1;
    }

    private List<Product> findAllProducts(Product.Sorting sorting) {
        String args[] = {String.valueOf(Product.ACTIVE)};
        if (sorting == Product.Sorting.ALPHABETICAL)
            return Product.find(Product.class, "product_status = ?", args, null, "product_name", null);
        if (sorting == Product.Sorting.PRICE)
            return Product.find(Product.class, "product_status = ?", args, null, "product_sell_price", null);
        return Product.find(Product.class, "product_status = ?", String.valueOf(Product.ACTIVE));
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
