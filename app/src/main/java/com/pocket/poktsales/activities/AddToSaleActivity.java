package com.pocket.poktsales.activities;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.pocket.poktsales.R;
import com.pocket.poktsales.adapters.SimpleProductAdapter;
import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.presenter.SalesPresenter;
import com.pocket.poktsales.utils.DataSearchLoader;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.BindView;

public class AddToSaleActivity extends BaseActivity {

    @BindView(R.id.sliding_up_panel)
    SlidingUpPanelLayout panel;

    @BindView(R.id.lv_products_in_sale)
    ListView lvSale;

    @BindView(R.id.lv_products)
    ListView lvProducts;

    @BindView(R.id.btn_apply)
    Button btnApply;

    SimpleProductAdapter productAdapter;

    long ticketId;

    RequiredPresenterOps.SalePresenterOps presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResourceId = R.layout.activity_add_to_sale;
        super.onCreate(savedInstanceState);

    }

    private void searchProducts(String args){
        DataSearchLoader productLoader = new DataSearchLoader(this);
        productLoader.execute(args);
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
    protected void init() {
        super.init();
        panel.setPanelHeight(0);
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


        if (getIntent().getExtras().containsKey("ticketId"))
            ticketId = getIntent().getExtras().getLong("ticketId");
        else
            onError();
        presenter = SalesPresenter.getInstance(this);
        lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.addToSale(ticketId, id);
                //shakePanel();
            }
        });
        searchProducts("");
    }

    /**
    private void shakePanel(){
        panel.animate().setInterpolator(new AccelerateInterpolator()).setDuration(200)
                .translationY(panel.getY()-40);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                panel.animate().setInterpolator(new AccelerateInterpolator()).setDuration(200)
                        .translationY(panel.getY()+40);
            }
        }, 300);
    }
     */
}
