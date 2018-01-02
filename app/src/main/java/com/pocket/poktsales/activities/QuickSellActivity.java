package com.pocket.poktsales.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.pocket.poktsales.R;
import com.pocket.poktsales.adapters.SimpleProductAdapter;
import com.pocket.poktsales.interfaces.RequiredViewOps;
import com.pocket.poktsales.model.MProduct;
import com.pocket.poktsales.presenter.QuickSalePresenter;
import com.pocket.poktsales.utils.Conversor;
import com.pocket.poktsales.utils.DataLoader;
import com.pocket.poktsales.utils.DialogBuilder;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

/**
 * Created by MAV1GA on 30/11/2017.
 */

public class QuickSellActivity extends BaseActivity implements RequiredViewOps.QuickSaleOps,
        SearchView.OnQueryTextListener{

    @BindView(R.id.include)
    SlidingUpPanelLayout panel;

    @BindView(R.id.lv_products_in_sale)
    ListView lvSale;

    @BindView(R.id.lv_products)
    ListView lvProducts;

    @BindView(R.id.btn_apply)
    Button btnApply;

    @BindView(R.id.tv_tab_reference)
    TextView tvTabReference;

    @BindView(R.id.tv_total)
    TextView tvTabTotal;

    @BindView(R.id.btn_delete)
    ImageButton btnDelete;

    SearchView searchView;

    private QuickSalePresenter presenter;
    private ActivityAdapter activityAdapter;
    private SimpleProductAdapter productAdapter;
    private SimpleProductAdapter saleProductAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResourceId = R.layout.activity_add_to_sale;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init() {
        super.init();
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applySale();
            }
        });
        btnDelete.setVisibility(View.GONE);
        activityAdapter = new ActivityAdapter();
        presenter = new QuickSalePresenter(this);
        lvSale.setEmptyView(findViewById(android.R.id.empty));
        lvSale.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                removeFromSale(position);
                saleProductAdapter.clear();
                saleProductAdapter.addAll(activityAdapter.getSaleProducts());
                saleProductAdapter.notifyDataSetChanged();
                return true;
            }
        });
        if (productAdapter == null)
            productAdapter = new SimpleProductAdapter(this, R.layout.row_simple_product, new ArrayList<MProduct>());
        if (saleProductAdapter == null){
            saleProductAdapter = new SimpleProductAdapter(this, R.layout.row_simple_product, new ArrayList<MProduct>());
        }
        tvTabTotal.setText(Conversor.asCurrency(0));
        tvTabReference.setText(R.string.quick_sale);
        lvSale.setAdapter(saleProductAdapter);
        lvProducts.setAdapter(productAdapter);
        if (getSupportActionBar()!= null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addToSale(id, 1);
            }
        });
        final LinearLayout layout = (LinearLayout)findViewById(R.id.ll_header);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                panel.setPanelHeight(layout.getMeasuredHeight());
            }
        });
        panel.setScrollableView(lvSale);
        start();
    }

    private void removeFromSale(int position) {
        activityAdapter.remove(position);
    }

    private void addToSale(long id, int i) {
        activityAdapter.addToSale(presenter.getProductFromId(id), i);
        saleProductAdapter.notifyDataSetChanged();
        tvTabTotal.setText(Conversor.asCurrency(activityAdapter.getSaleTotal()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sale, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        if (searchManager != null)
            searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        if (saleProductAdapter != null)
            lvSale.setAdapter(saleProductAdapter);
        if (productAdapter != null)
            lvProducts.setAdapter(productAdapter);
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                panel.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                return true;
            }
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    }
                }, 500);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_temp_product){
            DialogBuilder.newTempDialog(QuickSellActivity.this,
                    new DialogBuilder.DialogInteractionListener.OnNewTempDialogListener() {
                        @Override
                        public void onNewTempProductDialog(String productName, float productPrice) {
                            long id = presenter.saveAsTemp(productName, productPrice);
                            addToSale(id, 1);
                        }
                    }).show();
            return true;
        }
        return false;
    }

    @Override
    public void onLoadingPrepare() {
        super.onLoadingPrepare();
        if (productAdapter == null){
            productAdapter = new SimpleProductAdapter(this, R.layout.row_simple_product, new ArrayList<MProduct>());
        }else{
            productAdapter.clear();
        }
    }

    @Override
    public void onApplySale() {
        finish();
    }

    private void applySale(){
        presenter.apply(activityAdapter.getSaleProducts());
    }

    @Override
    protected void start() {
        super.start();
    }

    @Override
    public void onLoading() {
        super.onLoading();
        activityAdapter.setAllProducts(presenter.getAllProducts());
    }

    @Override
    public void onLoading(String searchArgs) {
        super.onLoading(searchArgs);
        activityAdapter.setAllProducts(presenter.getProductsFromSearch(searchArgs));
    }

    @Override
    public void onLoadingComplete() {
        super.onLoadingComplete();
        productAdapter.addAll(activityAdapter.getAllProducts());
        productAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchProducts(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        if (query.length() >= 3 || query.length() % 3 == 0){
            searchProducts(query);
        }
        if(query.length() == 0){
            searchProducts(query);
        }
        return true;
    }

    private void searchProducts(String query) {
        loader = new DataLoader(this);
        loader.execute(query);
    }

    class ActivityAdapter{
        List<MProduct> allProducts;
        List<MProduct> saleProducts;
        float saleTotal = 0;
        List<MProduct> getAllProducts() {
            return allProducts;
        }
        void setAllProducts(List<MProduct> allProducts) {
            this.allProducts = allProducts;
        }
        void addToSale(MProduct product, int qty){
            if (saleProducts == null){
                saleProducts = new ArrayList<>();
            }
            for (int i=0; i<qty; i++){
                saleProducts.add(product);
                saleProductAdapter.add(product);
                saleTotal += product.productSellPrice;
            }
        }

        List<MProduct> getSaleProducts(){
            return saleProducts;
        }

        float getSaleTotal() {
            return saleTotal;
        }

        void remove(int position) {
            float productPrice = saleProducts.get(position).productSellPrice;
            saleProducts.remove(position);
            saleTotal -= productPrice;
            tvTabTotal.setText(Conversor.asCurrency(saleTotal));
        }
    }
}
