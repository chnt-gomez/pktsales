package com.pocket.poktsales.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;
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

public class OpenTabsActivity extends BaseActivity implements AdapterView.OnItemClickListener{

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tabs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_quick_tab){
            Ticket ticket = new Ticket();
            ticket.setTicketReference(getString(R.string.new_sale));
            presenter.openTab(ticket);
            if (ticket.getId() != null)
                openTicket(ticket.getId(), true);
            return true;
        }

        return false;
    }

    @Override
    protected void init() {
        super.init();
        if (presenter == null)
            presenter = SalesPresenter.getInstance(this);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBuilder.newTabDialog(OpenTabsActivity.this, new DialogBuilder.DialogInteractionListener.OnNewTabListener() {
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter = SalesPresenter.getInstance(this);
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

    @Override
    public void onSuccess() {
        super.onSuccess();
        start();
    }

    private void openTicket(long ticketId){
        Intent saleIntent = new Intent(OpenTabsActivity.this, SellActivity.class);
        saleIntent.putExtra("ticketId", ticketId);
        startActivity(saleIntent);
    }

    private void openTicket(long ticketId, boolean isQuickSale){
        Intent saleIntent = new Intent(OpenTabsActivity.this, SellActivity.class);
        saleIntent.putExtra("ticketId", ticketId);
        saleIntent.putExtra("isQuickSale", isQuickSale);
        startActivity(saleIntent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        openTicket(id);
    }
}
