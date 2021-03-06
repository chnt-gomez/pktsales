package com.pocket.poktsales.activities;


import android.animation.ValueAnimator;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.graphics.ColorUtils;
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

import com.daimajia.swipe.SwipeLayout;
import com.pocket.poktsales.R;
import com.pocket.poktsales.adapters.PickToSellProductAdapter;
import com.pocket.poktsales.adapters.SimpleSaleAdapter;
import com.pocket.poktsales.interfaces.RequiredViewOps;
import com.pocket.poktsales.model.MProduct;
import com.pocket.poktsales.model.MSale;
import com.pocket.poktsales.presenter.SalesPresenter;
import com.pocket.poktsales.utils.Conversor;
import com.pocket.poktsales.utils.DataLoader;
import com.pocket.poktsales.utils.DialogBuilder;
import com.pocket.poktsales.utils.HtmlTicketBuilder;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SellActivity extends BaseActivity implements SearchView.OnQueryTextListener, View.OnClickListener, RequiredViewOps.SaleViewOps,
    SimpleSaleAdapter.ViewOperations, PickToSellProductAdapter.ViewOperations{

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

    PickToSellProductAdapter productAdapter;
    SimpleSaleAdapter tabListProductAdapter;
    List<MProduct> availableProducts;
    List<MSale> tabSales;
    String tabName;
    float tabTotal = 0f;


    SearchView searchView;
    private long ticketId;

    private SalesPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_sale);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        init();
    }

    @Override
    public void onLoadingPrepare() {
        super.onLoadingPrepare();
        availableProducts = new ArrayList<>();
        tabSales = new ArrayList<>();
        if (productAdapter != null ){
            productAdapter.clear();
        }else{
            productAdapter = new PickToSellProductAdapter(this, R.layout.row_pick_product, new ArrayList<MProduct>());
        }
        if (tabListProductAdapter == null){
            tabListProductAdapter = new SimpleSaleAdapter(this, R.layout.simple_sale_row, new ArrayList<MSale>());
        }else{
            tabListProductAdapter.clear();
        }
    }

    @Override
    public void onLoading() {
        super.onLoading();
        availableProducts.addAll(presenter.getProductsToSell());

        tabName = presenter.getTicket(ticketId).ticketReference;
        tabTotal = presenter.getTicket(ticketId).saleTotal;
        tabSales.addAll(presenter.getProductsFromTab(ticketId));
    }

    @Override
    public void onLoading(String searchArgs) {
        super.onLoading(searchArgs);
        availableProducts.addAll(presenter.getProductsFromSearch(searchArgs));

    }

    @Override
    public void onLoadingComplete() {
        super.onLoadingComplete();
        productAdapter.addAll(availableProducts);
        tabListProductAdapter.addAll(tabSales);
        tvTabReference.setText(tabName);
        productAdapter.notifyDataSetChanged();
        tabListProductAdapter.notifyDataSetChanged();
        tvTabTotal.setText(Conversor.asCurrency(tabTotal));
        tabName = null;
        availableProducts = null;
    }

    @Override
    protected void init() {
        super.init();
        presenter = new SalesPresenter(this);
        lvSale.setEmptyView(findViewById(R.id.empty_sale));
        lvProducts.setEmptyView(findViewById(R.id.empty_products));

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
                if (((SwipeLayout)(lvProducts.getChildAt(position - lvProducts.getFirstVisiblePosition()))).getOpenStatus() == SwipeLayout.Status.Close){
                    ((SwipeLayout)(lvProducts.getChildAt(position - lvProducts.getFirstVisiblePosition()))).open(true);
                }
            }
        });

        lvSale.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (((SwipeLayout)(lvSale.getChildAt(position - lvSale.getFirstVisiblePosition()))).getOpenStatus() == SwipeLayout.Status.Open){
                    ((SwipeLayout)(lvSale.getChildAt(position - lvSale.getFirstVisiblePosition()))).close(true);
                }else{
                    ((SwipeLayout)(lvSale.getChildAt(position - lvSale.getFirstVisiblePosition()))).open(true);
                }
            }
        });
        btnDelete.setOnClickListener(this);
        setTitle(R.string.title_activity_add_to_sale);
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
        DialogBuilder.saleSuccessDialog(SellActivity.this, new DialogBuilder.DialogInteractionListener.OnSaleSuccessListener(){
            @Override
            public void onSuccess() {
                finish();
            }

            @Override
            public void onShareTicket() {
                final Intent shareIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "The Subject");
                shareIntent.putExtra(
                        Intent.EXTRA_TEXT,
                        HtmlTicketBuilder.buildTicket()
                );
                startActivity(shareIntent);
            }
        }).show();
    }

    @Override
    public void onDeleteFromSale(long productId, String newTotal) {
        tabListProductAdapter.remove(productId);
        tabListProductAdapter.notifyDataSetChanged();
        animateTotalNegative();
        tvTabTotal.setText(newTotal);
    }

    @Override
    public void onProductAddToSale(MSale sale, String newTotal, int qty) {
        tabListProductAdapter.add(sale);
        tvTabTotal.setText(newTotal);
        animateTotal();
        tabListProductAdapter.notifyDataSetChanged();
    }

    private void animateTotalNegative(){
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fractionAnim = (float) valueAnimator.getAnimatedValue();

                tvTabTotal.setTextColor(ColorUtils.blendARGB(Color.parseColor("#FF0000")
                        , Color.parseColor("#FFFFFF")
                        , fractionAnim));
            }
        });
        valueAnimator.start();
    }

    private void animateTotal() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fractionAnim = (float) valueAnimator.getAnimatedValue();

                tvTabTotal.setTextColor(ColorUtils.blendARGB(Color.parseColor("#00ff00")
                        , Color.parseColor("#FFFFFF")
                        , fractionAnim));
            }
        });
        valueAnimator.start();
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
                            if (id != -1)
                                presenter.addToSale(ticketId, id, 1);
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
            return true;
        }
        if(newText.length() == 0){
            searchProducts(newText);
            return true;
        }
        return false;
    }

    @Override
    public void onCancelSale() {
        DialogBuilder.saleCanceledDialog(SellActivity.this, new DialogBuilder.DialogInteractionListener.OnSaleCancelListener(){
            @Override
            public void onCancel() {
                finish();
            }
        }).show();
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

    @Override
    public void requestDelete(long productId) {
        presenter.removeFromSale(ticketId, productId);
    }

    @Override
    public void addOne(long productId) {
        presenter.addToSale(ticketId, productId, 1);
    }

    @Override
    public void requestAddMany(long productId) {
        DialogBuilder.addToSaleDialog(SellActivity.this, new DialogBuilder.DialogInteractionListener.OnAddToSale() {
            @Override
            public void onAddToSale(long productId, int qty) {
                presenter.addToSale(ticketId, productId, qty);
            }
        }, productId).show();
    }

}
