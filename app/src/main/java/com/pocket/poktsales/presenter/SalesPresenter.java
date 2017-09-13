package com.pocket.poktsales.presenter;

import android.util.Log;

import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.interfaces.RequiredViewOps;
import com.pocket.poktsales.model.Department;
import com.pocket.poktsales.model.Product;
import com.pocket.poktsales.model.Sale;
import com.pocket.poktsales.model.Ticket;

import java.util.List;

/**
 * Created by MAV1GA on 04/09/2017.
 */

public class SalesPresenter implements RequiredPresenterOps.ProductPresenterOps, RequiredPresenterOps.TabPresenterOps,
        RequiredPresenterOps.SalePresenterOps{

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
    public Product updateProduct(Product newProductArgs) {
        if (isProductNameInUse(newProductArgs.getProductName(), newProductArgs.getId())){
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

    @Override
    public List<Department> getAllDepartments() {
        return Department.listAll(Department.class);
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
    public void applyTicket(long ticketId) {
        Ticket ticket = Ticket.findById(Ticket.class, ticketId);
        ticket.setTicketStatus(Ticket.TICKET_APPLIED);
        ticket.save();
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
        return Product.findWithQuery(Product.class, "SELECT * FROM Product p, Sale s, Ticket t where " +
                "s.product = p.id AND s.ticket = t.id and t.id = "+String.valueOf(ticketId));
    }

    @Override
    public void cancelTab(long ticketId) {
        Ticket ticket = Ticket.findById(Ticket.class, ticketId);
        ticket.setTicketStatus(Ticket.TICKET_CANCELED);
        ticket.save();
        view.onSuccess();
    }

    /*
    Internal Methods
     */

    private boolean isTicketInUse(String ticketReference){
        return Ticket.find(Ticket.class, "ticket_reference LIKE ?", ticketReference ).size() >= 1;
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
