package com.pocket.poktsales.presenter;

import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.interfaces.RequiredViewOps;
import com.pocket.poktsales.model.Product;

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
}
