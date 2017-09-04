package com.pocket.poktsales.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.pocket.poktsales.R;
import com.pocket.poktsales.model.Product;
import com.pocket.poktsales.utils.DialogBuilder;

import butterknife.BindView;

public class InventoryActivity extends BaseActivity implements SearchView.OnQueryTextListener {

    @BindView(R.id.fab)
    FloatingActionButton btnAdd;

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
    protected void init() {
        super.init();
        if (getSupportActionBar()!= null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBuilder.newProductDialog(InventoryActivity.this,
                        new DialogBuilder.DialogInteractionListener.OnNewProductListener() {
                    @Override
                    public void onNewProduct(Product product) {

                    }
                }).show();
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        onSuccess();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() >= 3 && newText.length() % 3 == 0){
            //TODO(1):Search here
        }

        if(newText.length() == 0){
            //TODO(2):Show all
        }
        return true;
    }
}
