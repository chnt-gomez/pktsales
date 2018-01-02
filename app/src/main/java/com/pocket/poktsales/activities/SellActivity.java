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
import com.pocket.poktsales.presenter.SalesPresenter;
import com.pocket.poktsales.utils.Conversor;
import com.pocket.poktsales.utils.DataLoader;
import com.pocket.poktsales.utils.DialogBuilder;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SellActivity extends BaseActivity implements SearchView.OnQueryTextListener, View.OnClickListener, RequiredViewOps.SaleViewOps{

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

    SimpleProductAdapter productAdapter;
    SimpleProductAdapter tabListProductAdapter;
    ActivityAdapter activityAdapter;


    SearchView searchView;
    private long ticketId;

    private SalesPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResourceId = R.layout.activity_add_to_sale;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onLoadingPrepare() {
        super.onLoadingPrepare();
        if (activityAdapter == null)
            activityAdapter = new ActivityAdapter();
        if (productAdapter != null ){
            productAdapter.clear();
        }else{
            productAdapter = new SimpleProductAdapter(this, R.layout.row_simple_product, new ArrayList<MProduct>());
        }
        if (tabListProductAdapter != null){
            productAdapter.clear();
        }else{
            tabListProductAdapter = new SimpleProductAdapter(this, R.layout.row_simple_product, new ArrayList<MProduct>());
        }
    }

    @Override
    public void onLoading() {
        super.onLoading();
        activityAdapter.setProducts(presenter.getProductsToSell());
        activityAdapter.setTabProducts(presenter.getProductsFromTab(ticketId));
        activityAdapter.setTabReference(presenter.getTicket(ticketId).ticketReference);
        activityAdapter.setTabTotal(Conversor.asCurrency(presenter.getTicket(ticketId).saleTotal));
    }

    @Override
    public void onLoading(String searchArgs) {
        super.onLoading(searchArgs);
        activityAdapter.setProducts(presenter.getProductsFromSearch(searchArgs));
    }

    @Override
    public void onLoadingComplete() {
        super.onLoadingComplete();
        tabListProductAdapter.clear();
        productAdapter.clear();
        tabListProductAdapter.addAll(activityAdapter.getTabProducts());
        productAdapter.addAll(activityAdapter.getProducts());
        tvTabReference.setText(activityAdapter.getTabReference());
        tvTabTotal.setText(activityAdapter.getTabTotal());
        tabListProductAdapter.notifyDataSetChanged();
        productAdapter.notifyDataSetChanged();
    }

    @Override
    protected void init() {
        super.init();
        presenter = new SalesPresenter(this);
        lvSale.setEmptyView(findViewById(android.R.id.empty));

        if (getSupportActionBar()!= null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey("ticketId")) {
                ticketId = getIntent().getExtras().getLong("ticketId");
                btnApply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.applyTicket(ticketId);
                    }
                });
            } else {
                onError();
                finish();
            }
        }
        lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.addToSale(ticketId, id);
                if (searchView.isIconified()){
                    panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
            }
        });
        lvSale.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.removeFromSale(ticketId, id);
                return true;
            }
        });
        btnDelete.setOnClickListener(this);
        start();
    }

    @Override
    public void onBackPressed() {
        if (panel.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED)
            panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        else
            super.onBackPressed();
    }

    @Override
    public void onApplySale() {
        finish();
    }

    @Override
    public void onDeleteFromSale(long productId, String newTotal) {
        activityAdapter.deleteFromSale(productId);
        tabListProductAdapter.remove(productId);
        tvTabTotal.setText(newTotal);
        tabListProductAdapter.notifyDataSetChanged();
    }

    @Override
    public void onProductAddToSale(MProduct product, String newTotal) {
        activityAdapter.addToSale(product);
        tabListProductAdapter.add(product);
        tvTabTotal.setText(newTotal);
        tabListProductAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sale, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        if (tabListProductAdapter != null)
            lvSale.setAdapter(tabListProductAdapter);
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
            DialogBuilder.newTempDialog(SellActivity.this,
                    new DialogBuilder.DialogInteractionListener.OnNewTempDialogListener() {
                        @Override
                        public void onNewTempProductDialog(String productName, float productPrice) {
                            long id = presenter.saveAsTemp(productName, productPrice);
                            presenter.addToSale(ticketId, id);
                        }
                    }).show();
            return true;
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchProducts(query);
        return true;
    }

    private void searchProducts(String query) {
        loader = new DataLoader(this);
        loader.execute(query);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() >= 3 || newText.length() % 3 == 0){
            searchProducts(newText);
        }
        if(newText.length() == 0){
            searchProducts(newText);
        }
        return true;
    }

    @Override
    public void onCancelSale() {
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_delete)
            DialogBuilder.confirmDeleteTabDialog(SellActivity.this, presenter.getTicket(ticketId),
                    new DialogBuilder.DialogInteractionListener.OnDeleteTabListener() {
                        @Override
                        public void onDeleteTab(long ticketId) {
                            presenter.cancelTab(ticketId);
                        }
                    }).show();
    }

    class ActivityAdapter{
        List<MProduct> tabProducts;
        String tabReference;
        String tabTotal;
        List<MProduct> products;

        List<MProduct> getTabProducts() {
            return tabProducts;
        }

        void setTabProducts(List<MProduct> tabProducts) {
            this.tabProducts = tabProducts;
        }

        String getTabReference() {
            return tabReference;
        }

        void setTabReference(String tabReference) {
            this.tabReference = tabReference;
        }

        String getTabTotal() {
            return tabTotal;
        }

        void setTabTotal(String tabTotal) {
            this.tabTotal = tabTotal;
        }

        List<MProduct> getProducts() {
            return products;
        }

        void setProducts(List<MProduct> products) {
            this.products = products;
        }

        void addToSale(MProduct product) {
            tabProducts.add(product);
        }
        void deleteFromSale(long productId) {
            for (MProduct p : tabProducts) {
                if (p.id == productId){
                    tabProducts.remove(p);
                    break;
                }
            }
        }
    }
}
