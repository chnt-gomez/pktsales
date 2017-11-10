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
import com.pocket.poktsales.interfaces.OnLoadingEventListener;
import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.interfaces.RequiredViewOps;
import com.pocket.poktsales.model.Product;
import com.pocket.poktsales.model.Ticket;
import com.pocket.poktsales.presenter.SalesPresenter;
import com.pocket.poktsales.utils.Conversor;
import com.pocket.poktsales.utils.DataLoader;
import com.pocket.poktsales.utils.DialogBuilder;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

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


    private SearchView searchView;
    long ticketId;

    private SalesPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResourceId = R.layout.activity_add_to_sale;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onLoading(String searchArgs) {
        super.onLoading(searchArgs);
        productAdapter = new SimpleProductAdapter(getApplicationContext(), R.layout.row_simple_product,
                presenter.getProductsFromSearch(searchArgs));
    }

    @Override
    public void onLoadingComplete() {
        super.onLoadingComplete();
        lvProducts.setAdapter(productAdapter);
    }

    @Override
    public void onSuccess() {

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

        if (getIntent().getExtras().containsKey("ticketId")) {
            ticketId = getIntent().getExtras().getLong("ticketId");
            Ticket ticket = presenter.getTicket(ticketId);
            tvTabReference.setText(ticket.getTicketReference());
            tvTabTotal.setText(Conversor.asCurrency(ticket.getSaleTotal()));
            btnApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.applyTicket(ticketId);
                    finish();
                }
            });

        }else{
            onError();
            finish();
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
    }

    @Override
    public void onBackPressed() {
        if (panel.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED)
            panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        else
            super.onBackPressed();
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
                        public void onNewTempProductDialog(Product product) {
                            presenter.addToSale(ticketId, product);
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
    public void onClick(View v) {
        if (v.getId() == R.id.btn_delete)
            DialogBuilder.confirmDeleteTabDialog(SellActivity.this, presenter.getTicket(ticketId),
                    new DialogBuilder.DialogInteractionListener.OnDeleteTabListener() {
                        @Override
                        public void onDeleteTab(long ticketId) {
                            presenter.cancelTab(ticketId);
                            finish();
                        }
                    }).show();
    }

    class ActivityAdapter{
        List<Product> tabProducts;
        String tabReference;
        String tabTotal;
        List<Product> products;

        public List<Product> getTabProducts() {
            return tabProducts;
        }

        public void setTabProducts(List<Product> tabProducts) {
            this.tabProducts = tabProducts;
        }

        public String getTabReference() {
            return tabReference;
        }

        public void setTabReference(String tabReference) {
            this.tabReference = tabReference;
        }

        public String getTabTotal() {
            return tabTotal;
        }

        public void setTabTotal(String tabTotal) {
            this.tabTotal = tabTotal;
        }

        public List<Product> getProducts() {
            return products;
        }

        public void setProducts(List<Product> products) {
            this.products = products;
        }
    }
}
