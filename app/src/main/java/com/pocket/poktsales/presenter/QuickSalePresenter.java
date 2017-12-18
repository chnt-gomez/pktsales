package com.pocket.poktsales.presenter;

import android.text.method.DateTimeKeyListener;

import com.pocket.poktsales.R;
import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.interfaces.RequiredViewOps;
import com.pocket.poktsales.model.Product;
import com.pocket.poktsales.model.Sale;
import com.pocket.poktsales.model.Ticket;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by MAV1GA on 04/12/2017.
 */

public class QuickSalePresenter extends BasePresenter implements RequiredPresenterOps.QuickSalePresenterOps {

    private RequiredViewOps.QuickSaleOps view;

    public QuickSalePresenter (RequiredViewOps.QuickSaleOps view){
        this.view = view;
    }

    @Override
    public List<Product> getAllProducts() {
        return findAllProducts(Product.Sorting.NONE);
    }
    @Override
    public List<Product> getProductsFromSearch(String searchArgs) {
        return searchProductsWithQuery(searchArgs);
    }
    @Override
    public Product getProductFromId(long id) {
        return findProductById(id);
    }

    @Override
    public void apply(List<Product> saleProducts) {
    if (saleProducts == null || saleProducts.size() <= 0){
        view.onError(R.string.cant_apply_empty_sale);
        return;
    }
        Ticket ticket = new Ticket();
        ticket.save();
        for (Product p : saleProducts){
            Sale sale = new Sale();
            sale.setProduct(p);
            sale.setTicket(ticket);
            sale.setProductAmount(1F);
            sale.setSaleTotal(p.getProductSellPrice() * sale.getProductAmount());
            sale.save();
            ticket.setSaleTotal(ticket.getSaleTotal()+sale.getSaleTotal());
            ticket.save();
        }
        ticket.setTicketStatus(Ticket.TICKET_APPLIED);
        ticket.setDateTime(DateTime.now().getMillis());
        ticket.save();
        view.onApplySale();
    }

    @Override
    public long saveAsTemp(String productName, float productPrice) {
        Product product = new Product();
        product.setProductName(productName);
        product.setProductStatus(Product.TEMPORARY);
        product.setProductSellPrice(productPrice);
        product.setId(1000L);
        return product.save();
    }
}
