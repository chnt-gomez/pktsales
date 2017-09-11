package com.pocket.poktsales.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.pocket.poktsales.R;
import com.pocket.poktsales.adapters.TabAdapter;
import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.model.Ticket;
import com.pocket.poktsales.presenter.SalesPresenter;
import com.pocket.poktsales.utils.DataLoader;
import com.pocket.poktsales.utils.DialogBuilder;

import butterknife.BindView;

public class SellActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    @BindView(R.id.grid)
    GridView gridView;

    TabAdapter adapter;

    @BindView(R.id.fab)
    FloatingActionButton btnAdd;

    RequiredPresenterOps.TabPresenterOps presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResourceId = R.layout.activity_sell;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init() {
        super.init();
        presenter = SalesPresenter.getInstance(this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBuilder.newTabDialog(SellActivity.this, new DialogBuilder.DialogInteractionListener.OnNewTabListener() {
                    @Override
                    public void onNewTab(Ticket ticket) {
                        presenter.openTab(ticket);
                    }
                }).show();
            }
        });
        gridView.setOnItemClickListener(this);
        if (getSupportActionBar()!= null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        start();
    }

    @Override
    public void onLoading() {
        super.onLoading();
        adapter = new TabAdapter(getApplicationContext(), R.layout.grid_layout_tab,
                presenter.getAllOpenTabs());
    }

    @Override
    public void onLoadingComplete() {
        super.onLoadingComplete();
        gridView.setAdapter(adapter);
    }

    private void start(){
        DataLoader loader = new DataLoader(this);
        loader.execute();
    }

    @Override
    public void onSuccess() {
        super.onSuccess();
        start();
    }

    private void openTicket(long ticketId){
        if (ticketId != -1){
            Intent saleIntent = new Intent(SellActivity.this, AddToSaleActivity.class);
            saleIntent.putExtra("ticketId", ticketId);
            startActivity(saleIntent);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        openTicket(id);
    }
}
