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
import com.pocket.poktsales.interfaces.RequiredViewOps;
import com.pocket.poktsales.model.MTicket;
import com.pocket.poktsales.presenter.TabPresenter;
import com.pocket.poktsales.utils.DialogBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OpenTabsActivity extends BaseActivity implements AdapterView.OnItemClickListener, RequiredViewOps.TabViewOps{

    @BindView(R.id.grid)
    GridView gridView;

    TabAdapter adapter;

    @BindView(R.id.fab)
    FloatingActionButton btnAdd;

    private TabPresenter presenter;

    ActivityAdapter activityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tabs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_quick_tab){
            openTicket();
            return true;
        }

        return false;
    }

    @Override
    protected void init() {
        super.init();
        if (presenter == null)
            presenter = new TabPresenter(this);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBuilder.newTabDialog(OpenTabsActivity.this, new DialogBuilder.DialogInteractionListener.OnNewTabListener() {
                    @Override
                    public void onNewTab(String ticketReference) {
                        presenter.openTab(ticketReference);
                    }
                }).show();
            }
        });
        gridView.setOnItemClickListener(this);
        if (getSupportActionBar()!= null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adapter = new TabAdapter(getApplicationContext(), R.layout.grid_layout_tab, new ArrayList<MTicket>());
        gridView.setAdapter(adapter);
        activityAdapter = new ActivityAdapter();
        setTitle(R.string.title_activity_sell);
        gridView.setEmptyView(findViewById(android.R.id.empty));
    }

    @Override
    protected void onResume() {
        super.onResume();
        start();
    }

    @Override
    public void onLoading() {
        super.onLoading();
        activityAdapter.setTabList(presenter.getAllOpenTabs());
    }

    @Override
    public void onLoadingComplete() {
        super.onLoadingComplete();
        if (adapter.getCount() > 0)
            adapter.clear();
        adapter.addAll(activityAdapter.getTabList());
        adapter.notifyDataSetChanged();
    }

    private void openTicket(long ticketId){
        Intent saleIntent = new Intent(OpenTabsActivity.this, SellActivity.class);
        saleIntent.putExtra("ticketId", ticketId);
        startActivity(saleIntent);
    }

    private void openTicket(){
        Intent saleIntent = new Intent(OpenTabsActivity.this, QuickSellActivity.class);
        startActivity(saleIntent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        openTicket(id);
    }

    @Override
    public void onNewTab(MTicket ticket) {
        activityAdapter.addTab(ticket);
        adapter.add(ticket);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onTabApplied(long tabId) {
        activityAdapter.remove(tabId);
        adapter.remove(tabId);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onTabCancelled(long tabId) {
        activityAdapter.remove(tabId);
        adapter.remove(tabId);
        adapter.notifyDataSetChanged();
    }

    class ActivityAdapter {
        List<MTicket> getTabList() {
            return tabList;
        }

        void setTabList(List<MTicket> tabList) {
            this.tabList = tabList;
        }

        List<MTicket> tabList;

        void addTab(MTicket ticket) {
            if (tabList == null)
                tabList = new ArrayList<>();
            tabList.add(ticket);
        }

        void remove(long ticketId){
            if (tabList == null)
                return;
            for (int i=0; i<tabList.size(); i++){
                if (tabList.get(i).id == ticketId) {
                    tabList.remove(i);
                    break;
                }
            }
        }
    }
}
