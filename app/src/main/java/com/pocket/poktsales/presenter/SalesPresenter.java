package com.pocket.poktsales.presenter;

import com.pocket.poktsales.R;
import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.interfaces.RequiredViewOps;
import com.pocket.poktsales.model.MProduct;
import com.pocket.poktsales.model.MSale;
import com.pocket.poktsales.model.MTicket;
import com.pocket.poktsales.utils.Conversor;

import org.joda.time.DateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by MAV1GA on 04/09/2017.
 */

public class SalesPresenter extends BasePresenter implements
        RequiredPresenterOps.SalePresenterOps{

    private RequiredViewOps.SaleViewOps view;
    public SalesPresenter(RequiredViewOps.SaleViewOps view){
        this.view = view;
    }

    @Override
    public void addToSale(long ticketId, long productId, int qty) {
        Product product;
        try{
            product = Product.findById(Product.class, productId);
        }catch (NullPointerException e){
            product = Product.findById(Product.class, 1000L);
        }
        Ticket ticket = Ticket.findById(Ticket.class, ticketId);
        Sale sale = new Sale();
        sale.setProduct(product);
        sale.setTicket(ticket);
        sale.setProductAmount(qty);
        sale.setSaleConcept(product.getProductName());
        sale.setSaleTotal(product.getProductSellPrice()*sale.getProductAmount());
        sale.save();
        float newTotal = ticket.getSaleTotal() + sale.getSaleTotal();
        ticket.setSaleTotal(newTotal);
        ticket.save();
        view.onSuccess();
        view.onProductAddToSale(fromSale(sale), Conversor.asCurrency(newTotal), qty);
    }

    @Override
    public void applyTicket(long ticketId) {
        Ticket ticket = Ticket.findById(Ticket.class, ticketId);
        List<Sale> sales = Sale.find(Sale.class, "ticket = ?", String.valueOf(ticket.getId()));
        if (sales == null || sales.size() <= 0){
            view.onError(R.string.cant_apply_empty_sale);
            return;
        }
        ticket.setTicketStatus(Ticket.TICKET_APPLIED);
        ticket.setDateTime(DateTime.now().getMillis());
        ticket.save();
        view.onApplySale();
    }

    @Override
    public List<MProduct> getProductsFromSearch(String args) {
        return fromProductList(searchProductsWithQuery(args));
    }

    @Override
    public List<MProduct> getProductsToSell() {
        return fromProductList(Product.find(Product.class, "product_status = ?", String.valueOf(Product.ACTIVE)));
    }

    @Override
    public MTicket getTicket(long ticketId) {
        return fromTicket(Ticket.findById(Ticket.class, ticketId));
    }

    @Override
    public List<MSale> getProductsFromTab(long ticketId) {
        return fromSaleList(Sale.find(Sale.class, "ticket = ?", String.valueOf(ticketId)));
    }

    @Override
    public void cancelTab(long ticketId) {
        Ticket ticket = Ticket.findById(Ticket.class, ticketId);
        ticket.setTicketStatus(Ticket.TICKET_CANCELED);
        ticket.save();
        view.onSuccess();
        view.onCancelSale();
    }

    @Override
    public void removeFromSale(long ticketId, long saleId) {
        Ticket ticket = Ticket.findById(Ticket.class, ticketId);
        float ticketTotal= 0;
        String args[] = {String.valueOf(saleId), String.valueOf(ticketId)};
        for (Sale s : Sale.find(Sale.class, "id = ? AND ticket = ?", args,
                null, null, String.valueOf(1))){
            float saleTotal = s.getSaleTotal();
            ticketTotal = ticket.getSaleTotal()-saleTotal;
            ticket.setSaleTotal(ticketTotal);
            s.delete();
            ticket.save();
        }
        view.onDeleteFromSale(saleId, Conversor.asCurrency(ticketTotal));
    }

    @Override
    public long saveAsTemp(String productName, float productPrice) {
        if (!isValid(productName)){
            view.onError(R.string.err_invalid_product_name);
            return -1;
        }
        Product product = new Product();
        product.setProductName(productName);
        product.setProductStatus(Product.TEMPORARY);
        product.setProductSellPrice(productPrice);
        product.setId(1000L);
        return product.save();
    }

    private boolean isValid(String argument){
        return !argument.contains("*") && argument.length() > 0;
    }
}
