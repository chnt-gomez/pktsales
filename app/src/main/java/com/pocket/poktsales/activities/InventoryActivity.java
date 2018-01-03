package com.pocket.poktsales.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.pocket.poktsales.R;
import com.pocket.poktsales.adapters.SimpleProductAdapter;
import com.pocket.poktsales.interfaces.RequiredViewOps;
import com.pocket.poktsales.model.MProduct;
import com.pocket.poktsales.presenter.InventoryPresenter;
import com.pocket.poktsales.utils.Conversor;
import com.pocket.poktsales.utils.DataLoader;
import com.pocket.poktsales.utils.DialogBuilder;
import com.pocket.poktsales.utils.MeasurePicker;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class InventoryActivity extends BaseActivity implements SearchView.OnQueryTextListener,
        AdapterView.OnItemClickListener, RequiredViewOps.InventoryViewOps{

    @BindView(R.id.fab)
    FloatingActionButton btnAdd;
    InventoryPresenter presenter;
    private SimpleProductAdapter listAdapter;
    private InventoryActivityAdapter activityAdapter;

    @BindView(R.id.lv_products)
    ListView lvProducts;

    @BindView(R.id.sliding_up_panel)
    SlidingUpPanelLayout panel;

    @BindView(R.id.et_product_name)
    EditText etProductName;

    @BindView(R.id.et_product_price)
    EditText etProductPrice;

    @BindView(R.id.tv_product_name_header)
    TextView tvProductName;

    @BindView(R.id.spn_product_measure)
    Spinner spnProductMeasure;

    @BindView(R.id.btn_ok)
    ImageButton btnOk;

    @BindView(R.id.btn_delete)
    ImageButton btnDelete;

    @BindView(R.id.btn_category)
    ImageButton btnCategory;

    @BindView(R.id.tv_product_category)
    TextView tvProductCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResourceId = R.layout.activity_inventory;
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_products, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (panel.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED)
            panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        else
            super.onBackPressed();
    }

    @Override
    public void onPostResume() {
        super.onPostResume();
        start();
    }

    @Override
    protected void init() {
        super.init();
        if (presenter == null)
            presenter = new InventoryPresenter(this);
        loadingBar = (ProgressBar)findViewById(R.id.progressBar);
        if (getSupportActionBar()!= null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBuilder.newProductDialog(InventoryActivity.this,
                        new DialogBuilder.DialogInteractionListener.OnNewProductListener() {
                    @Override
                    public void onNewProduct(MProduct product) {
                        presenter.createProduct(product);
                    }
                }).show();
            }
        });
        spnProductMeasure.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,
                MeasurePicker.getEntries(getApplicationContext().getResources())));
        panel.setPanelHeight(0);
        panel.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                btnAdd.setVisibility(View.GONE);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED || newState == SlidingUpPanelLayout.PanelState.HIDDEN)
                    btnAdd.setVisibility(View.VISIBLE);
                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED)
                    btnAdd.setVisibility(View.GONE);
            }
        });
        panel.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
        lvProducts.setOnItemClickListener(this);
        activityAdapter = new InventoryActivityAdapter();
        listAdapter = new SimpleProductAdapter(getApplicationContext(), R.layout.row_simple_product, new ArrayList<MProduct>());
        lvProducts.setAdapter(listAdapter);
    }

    @Override
    public void onLoadingPrepare() {
        super.onLoadingPrepare();
        listAdapter.clear();
    }

    @Override
    public void onSuccess(int messageRes) {
        super.onSuccess(messageRes);
    }

    @Override
    public void onLoading() {
        activityAdapter.setList(presenter.getAllProducts());
    }

    @Override
    public void onLoadingComplete() {
        super.onLoadingComplete();
        if (listAdapter.getCount() > 0)
            listAdapter.clear();
        listAdapter.addAll(activityAdapter.getProductList());
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        search(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() >= 3 || newText.length() % 3 == 0){
            search(newText);
        }
        if(newText.length() == 0){
            search(newText);
        }
        return true;
    }

    private void search(String newText) {
        loader = new DataLoader(this);
        loader.execute(newText);
    }

    @Override
    public void onLoading(String searchArgs) {
        activityAdapter.setList(presenter.searchProducts(searchArgs));
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
       seeProduct(l);
    }

    private void seeProduct(long id) {
        if (id != -1){
            showProduct(id);
        }else{
            onError();
        }
    }

    private void showProduct(final long productId){
        final MProduct product = presenter.getProduct(productId);
        if (product != null){
            tvProductName.setText(product.productName);
            etProductName.setText(product.productName);
            etProductPrice.setText(Conversor.asFloat(product.productSellPrice));
            spnProductMeasure.setSelection(product.productMeasureUnit);
            if (product.productDepartment != null){
                tvProductCategory.setText(product.productDepartment);
            }else{
                tvProductCategory.setText(getString(R.string.no_category));
            }
        }
        if (panel.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED
                || panel.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN)
            panel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.updateProduct(productId, etProductName.getText().toString(), Conversor.toFloat(etProductPrice.getText().toString()),
                        spnProductMeasure.getSelectedItemPosition());
                panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogBuilder.confirmDeleteProductDialog(InventoryActivity.this, product, new DialogBuilder.DialogInteractionListener.OnDeleteProductListener() {
                    @Override
                    public void onDeleteProduct(long productId) {
                        presenter.deactivateProduct(productId);
                        panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    }
                }).show();
            }
        });
        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBuilder.addToCategoryDialog(InventoryActivity.this, new DialogBuilder.DialogInteractionListener.OnCategoryPickedListener() {
                    @Override
                    public void onCategorySelected(long categoryId) {
                        presenter.addProductToCategory(product.id, categoryId);
                        if (categoryId != 0)
                            tvProductCategory.setText(presenter.getCategoryName(categoryId));
                        else
                            tvProductCategory.setText(getString(R.string.no_category));
                    }
                }, presenter).show();
            }
        });
    }

    @Override
    public void onProductAdded(MProduct product) {
        listAdapter.add(product);
        activityAdapter.addProduct(product);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onProductUpdated(MProduct product) {
        activityAdapter.updateProduct(product);
        listAdapter.clear();
        listAdapter.addAll(activityAdapter.getProductList());
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onProductDeleted(long productId) {
        listAdapter.remove(productId);
        activityAdapter.remove(productId);
        listAdapter.notifyDataSetChanged();
    }

    class InventoryActivityAdapter{
        List<MProduct> productList;

        public void setList(List<MProduct> products){
            productList = products;
        }

        List<MProduct> getProductList() {
            return productList;
        }

        void addProduct(MProduct product){
            if (productList== null)
                productList = new ArrayList<>();
            productList.add(productList.size(), product);
        }

        void remove(long productId){
            for(int i=0; i<productList.size(); i++){
                if (productList.get(i).id == productId) {
                    productList.remove(i);
                    return;
                }
            }
        }

        void updateProduct(MProduct product){
           for (int i=0; i<productList.size(); i++){
               if (productList.get(i).id == product.id) {
                   productList.set(i, product);
                   return;
               }
           }
        }
    }
}
