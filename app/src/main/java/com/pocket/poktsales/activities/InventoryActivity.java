package com.pocket.poktsales.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
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
import com.pocket.poktsales.adapters.DropDownDepartmentAdapter;
import com.pocket.poktsales.adapters.SimpleProductAdapter;
import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.model.Department;
import com.pocket.poktsales.model.Product;
import com.pocket.poktsales.presenter.SalesPresenter;
import com.pocket.poktsales.utils.Conversor;
import com.pocket.poktsales.utils.DataLoader;
import com.pocket.poktsales.utils.DataSearchLoader;
import com.pocket.poktsales.utils.DialogBuilder;
import com.pocket.poktsales.utils.MeasurePicker;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.BindView;

public class InventoryActivity extends BaseActivity implements SearchView.OnQueryTextListener,
        AdapterView.OnItemClickListener{

    @BindView(R.id.fab)
    FloatingActionButton btnAdd;

    DataLoader loader;
    RequiredPresenterOps.ProductPresenterOps presenter;
    private SimpleProductAdapter adapter;

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

    Product.Sorting sessionSorting = Product.Sorting.NONE;

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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sort){
            openSelectionDialog();
            return true;
        }
        return false;
    }

    private void openSelectionDialog() {
        DialogBuilder.sortProductsDialog(InventoryActivity.this, presenter, sessionSorting, new DialogBuilder.DialogInteractionListener.OnSortProductsListener() {
            @Override
            public void onSortProducts(long departmentId, Product.Sorting sorting) {
                sessionSorting = sorting;
                start();
            }
        }).show();
    }

    @Override
    public void onPostResume() {
        super.onPostResume();
        start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter = SalesPresenter.getInstance(this);
    }

    @Override
    protected void init() {
        super.init();
        if (presenter == null)
            presenter = SalesPresenter.getInstance(this);
        loadingBar = (ProgressBar)findViewById(R.id.progressBar);
        if (getSupportActionBar()!= null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBuilder.newProductDialog(InventoryActivity.this,
                        new DialogBuilder.DialogInteractionListener.OnNewProductListener() {
                    @Override
                    public void onNewProduct(Product product) {
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
    }

    private void start() {
        loader = new DataLoader(this);
        loader.execute();
    }

    @Override
    public void onLoading() {
        adapter = new SimpleProductAdapter(getApplicationContext(), R.layout.row_simple_product,
                presenter.getAllProducts(sessionSorting));
    }

    @Override
    public void onLoading(String searchArgs) {
        adapter = new SimpleProductAdapter(getApplicationContext(), R.layout.row_simple_product,
                presenter.searchProducts(searchArgs));
    }

    @Override
    public void onLoadingComplete() {
        lvProducts.setAdapter(adapter);
        super.onLoadingComplete();
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
        DataSearchLoader loader = new DataSearchLoader(this);
        loader.execute(newText);
    }

    @Override
    public void onSuccess() {
        start();
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

    private void showProduct(long productId){
        final Product product = presenter.getProduct(productId);
        if (product != null){
            tvProductName.setText(product.getProductName());
            etProductName.setText(product.getProductName());
            etProductPrice.setText(Conversor.asFloat(product.getProductSellPrice()));
            spnProductMeasure.setSelection(product.getProductMeasureUnit());
            if (product.getDepartment() != null){
                tvProductCategory.setText(product.getDepartment().getDepartmentName());
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
                product.setProductMeasureUnit(spnProductMeasure.getSelectedItemPosition());
                product.setProductName(etProductName.getText().toString());
                product.setProductSellPrice(Conversor.toFloat(etProductPrice.getText().toString()));
                presenter.updateProduct(product);
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
                        presenter.addProductToCategory(product.getId(), categoryId);
                    }
                }, presenter).show();
            }
        });
    }
}
