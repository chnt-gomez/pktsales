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
import android.widget.ListView;
import android.widget.ProgressBar;
import com.pocket.poktsales.R;
import com.pocket.poktsales.adapters.SimpleProductAdapter;
import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.model.Product;
import com.pocket.poktsales.presenter.SalesPresenter;
import com.pocket.poktsales.utils.DataLoader;
import com.pocket.poktsales.utils.DataSearchLoader;
import com.pocket.poktsales.utils.DialogBuilder;

import butterknife.BindView;

public class InventoryActivity extends BaseActivity implements SearchView.OnQueryTextListener,
        AdapterView.OnItemClickListener {

    @BindView(R.id.fab)
    FloatingActionButton btnAdd;

    DataLoader loader;
    RequiredPresenterOps.ProductPresenterOps presenter;
    private SimpleProductAdapter adapter;

    @BindView(R.id.lv_products)
    ListView lvProducts;

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
    protected void init() {
        super.init();
        loadingBar = (ProgressBar)findViewById(R.id.progressBar);
        presenter = SalesPresenter.getInstance(this);
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
            DialogBuilder.seeProductDialog(InventoryActivity.this, presenter.getProduct(id),
                    presenter, new DialogBuilder.DialogInteractionListener.OnSaveProductListener() {
                        @Override
                        public void onSaveProduct(Product product) {
                            presenter.updateProduct(product);
                        }

                        @Override
                        public void onDeleteProduct(long productId) {
                            presenter.deactivateProduct(productId);
                        }
                    }).show();
        }else{
            onError();
        }
    }
}