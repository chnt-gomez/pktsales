package com.pocket.poktsales.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.pocket.poktsales.R;
import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.presenter.SalesPresenter;
import com.pocket.poktsales.utils.Conversor;
import com.pocket.poktsales.utils.DataLoader;
import butterknife.BindView;

public class HomeScreenActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.tv_today_income)
    TextView tvTodayIncome;

    @BindView(R.id.tv_performance)
    TextView tvPerformance;

    CardsAdapter adapter;

    RequiredPresenterOps.HomePresenterOps presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResourceId = R.layout.activity_home;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {

        super.onResume();
        DataLoader loader = new DataLoader(this);
        loader.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLoading() {
        super.onLoading();
        adapter = new CardsAdapter();
        adapter.setTodayIncome(Conversor.asCurrency(presenter.getDaySales()));
        adapter.setPerformance(presenter.getImprovement(getApplicationContext()));
    }

    @Override
    public void onLoadingComplete() {
        super.onLoadingComplete();
        tvTodayIncome.setText(adapter.getTodayIncome());
        tvPerformance.setText(adapter.getPerformance());
    }

    @Override
    protected void init() {
        super.init();
        presenter = SalesPresenter.getInstance(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_sale, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inventory) {
            goTo(InventoryActivity.class);
        }
        if (id== R.id.nav_sales){
            goTo(OpenTabsActivity.class);
        }
        if (id == R.id.nav_categories){
            goTo(CategoriesActivity.class);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goTo(Class activity) {
        startActivity(new Intent(HomeScreenActivity.this, activity));
    }

    private class CardsAdapter {

        String todayIncome;
        String performance;

        String getTodayIncome() {
            return todayIncome;
        }

        void setTodayIncome(String todayIncome) {
            this.todayIncome = todayIncome;
        }

        String getPerformance() {
            return performance;
        }

        void setPerformance(String performance) {
            this.performance = performance;
        }
    }
}
