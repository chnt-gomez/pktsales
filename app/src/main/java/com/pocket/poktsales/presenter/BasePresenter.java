package com.pocket.poktsales.presenter;

import android.util.Log;

import com.pocket.poktsales.model.MDepartment;
import com.pocket.poktsales.model.MProduct;
import com.pocket.poktsales.model.MTicket;
import com.pocket.poktsales.utils.Conversor;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MAV1GA on 02/11/2017.
 */

class BasePresenter {

    long getTodayStart(){
        return new DateTime().withTimeAtStartOfDay().getMillis();

    }

    long getTodayEnd(){
        return new DateTime().withTime(23,59,59,999).getMillis();
    }

    boolean isProductNameInUse(String productName){
        String args[] = {productName, String.valueOf(Product.ACTIVE)};
        return Product.find(Product.class, "product_name LIKE ? AND product_status = ?", args).size() >= 1;
    }

    Product findProductById(long id){
        try{
            return Product.findById(Product.class, id);
        }catch(Exception e){
            Log.wtf("findProductById()", String.format("Can't find product with id: %d", id));
            return null;
        }
    }

    List<Product> findAllProducts(Product.Sorting sorting) {
        String args[] = {String.valueOf(Product.ACTIVE)};
        if (sorting == Product.Sorting.ALPHABETICAL)
            return Product.find(Product.class, "product_status = ?", args, null, "product_name", null);
        if (sorting == Product.Sorting.PRICE)
            return Product.find(Product.class, "product_status = ?", args, null, "product_sell_price", null);
        return Product.find(Product.class, "product_status = ?", String.valueOf(Product.ACTIVE));
    }

    List<Product> searchProductsWithQuery(String searchArg) {
        return Product.find(Product.class, "product_name LIKE ? AND product_status = ?", "%"+formatForQuery(searchArg)+"%",
                String.valueOf(Product.ACTIVE));
    }

    List<Product> getProductsFromDepartment(long id) {
        return Product.find(Product.class, "department = ? AND product_status = ?", String.valueOf(id)
                , String.valueOf(Product.ACTIVE));
    }

    boolean isProductNameInUse(String productName, long productId){
        String args[] = {productName, String.valueOf(Product.ACTIVE)};
        for (Product p : Product.find(Product.class, "product_name = ? AND product_status = ?", args)){
            if (p.getId() != productId)
                return true;
        }
        return false;
    }

    boolean isDepartmentNameInUse(String departmentName) {
        return Department.find(Department.class, "department_name LIKE ?", departmentName).size() >= 1;
    }

    boolean isTicketInUse(String ticketReference){
        return Ticket.find(Ticket.class, "ticket_reference LIKE ? AND ticket_status = ?", ticketReference,
                String.valueOf(Ticket.TICKET_PENDING)).size() >= 1;
    }

    private static String formatForQuery(String rawQuery){
        return rawQuery.replace(" ", "%");
    }

    MDepartment fromDepartment(Department department){
        MDepartment modelDepartment = new MDepartment();
        modelDepartment.departmentName = department.getDepartmentName();
        modelDepartment.departmentStatus = String.valueOf(department.getDepartmentStatus());
        modelDepartment.colorResource = department.getColorResource();
        modelDepartment.iconResource = department.getIconResource();
        return modelDepartment;
    }

    List<MDepartment> fromDepartmentList(List<Department> departments){
        List<MDepartment> list = new ArrayList<>();
        for (Department d : departments){
            list.add(fromDepartment(d));
        }
        return list;
    }

    MTicket fromTicket(Ticket ticket){
        MTicket modelTicket = new MTicket();
        modelTicket.dateTime = String.valueOf(ticket.getDateTime());
        modelTicket.saleTotal = ticket.getSaleTotal();
        modelTicket.maskSaleTotal = Conversor.asCurrency(ticket.getSaleTotal());
        modelTicket.ticketReference = ticket.getTicketReference();
        return modelTicket;
    }

    List<MTicket> fromTicketList(List<Ticket> tickets){
        List<MTicket> list = new ArrayList<>();
        for (Ticket t: tickets){
            list.add(fromTicket(t));
        }
        return list;
    }

    MProduct fromProduct(Product product){
        MProduct modelProduct = new MProduct();
        modelProduct.productName = product.getProductName();
        modelProduct.productExistences = String.valueOf(product.getProductExistences());
        modelProduct.productInventory = String.valueOf(product.getProductInventory());
        modelProduct.productMeasureUnit = product.getProductMeasureUnit();
        if (product.getDepartment() != null) {
            modelProduct.productDepartment = product.getDepartment().getDepartmentName();
            modelProduct.departmentId = product.getDepartment().getId();
        }
        modelProduct.id = product.getId();
        modelProduct.maskProductSellPrice = Conversor.asCurrency(product.getProductSellPrice());
        modelProduct.productSellPrice = product.getProductSellPrice();
        return modelProduct;
    }

    List<MProduct> fromProductList(List<Product> products){
        List<MProduct> list = new ArrayList<>();
        for (Product p: products){
            list.add(fromProduct(p));
        }
        return list;
    }

}
