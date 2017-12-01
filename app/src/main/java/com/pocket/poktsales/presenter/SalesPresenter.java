package com.pocket.poktsales.presenter;

import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.interfaces.RequiredViewOps;
import com.pocket.poktsales.model.Product;
import com.pocket.poktsales.model.Sale;
import com.pocket.poktsales.model.Ticket;
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
        view.onProductAddToSale(product, Conversor.asCurrency(newTotal));
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
        view.onApplySale();
    }

    @Override
    public List<Product> getProductsFromSearch(String args) {
        return searchProductsWithQuery(args);
    }

    @Override
    public List<Product> getProductsToSell() {
        return Product.find(Product.class, "product_status = ?", String.valueOf(Product.ACTIVE));
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
        view.onCancelSale();
    }

    @Override
    public void removeFromSale(long ticketId, long productId) {
        Ticket ticket = Ticket.findById(Ticket.class, ticketId);
        float ticketTotal= 0;
        String args[] = {String.valueOf(productId), String.valueOf(ticketId)};
        for (Sale s : Sale.find(Sale.class, "product = ? AND ticket = ?", args,
                null, null, String.valueOf(1))){
            float saleTotal = s.getSaleTotal();
            ticketTotal = ticket.getSaleTotal()-saleTotal;
            ticket.setSaleTotal(ticketTotal);
            s.delete();
            ticket.save();
        }
        view.onDeleteFromSale(productId, Conversor.asCurrency(ticketTotal));
    }
}
