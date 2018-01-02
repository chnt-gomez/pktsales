package com.pocket.poktsales.presenter;

import com.pocket.poktsales.R;
import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.interfaces.RequiredViewOps;
import com.pocket.poktsales.model.MProduct;

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
    public List<MProduct> getAllProducts() {
        return fromProductList(findAllProducts(Product.Sorting.NONE));
    }
    @Override
    public List<MProduct> getProductsFromSearch(String searchArgs) {
        return fromProductList(searchProductsWithQuery(searchArgs));
    }
    @Override
    public MProduct getProductFromId(long id) {
        return fromProduct(findProductById(id));
    }

    @Override
    public void apply(List<MProduct> saleProducts) {
    if (saleProducts == null || saleProducts.size() <= 0){
        view.onError(R.string.cant_apply_empty_sale);
        return;
    }
        Ticket ticket = new Ticket();
        ticket.save();
        for (MProduct p : saleProducts){
            Sale sale = new Sale();
            sale.setProduct(findProductById(p.id));
            sale.setTicket(ticket);
            sale.setProductAmount(1F);
            sale.setSaleTotal(p.productSellPrice * sale.getProductAmount());
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
