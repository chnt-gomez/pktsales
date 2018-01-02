package com.pocket.poktsales.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.pocket.poktsales.R;
import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.model.MDepartment;
import com.pocket.poktsales.presenter.HomePresenter;
import com.pocket.poktsales.utils.ChartValueFormatter;
import com.pocket.poktsales.utils.Conversor;

import org.joda.time.DateTime;
import java.util.ArrayList;
import java.util.List;
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

    @BindView(R.id.chart_cat_sales)
    PieChart chartCatSales;

    @BindView(R.id.chart_performance)
    LineChart chartPerformance;

    @BindView(R.id.tv_star_product)
    TextView tvStartProduct;
    HomeActivityDataAdapter adapter;
    RequiredPresenterOps.HomePresenterOps presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResourceId = R.layout.activity_home;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLoading() {
        super.onLoading();
        adapter = new HomeActivityDataAdapter();
        adapter.setTodayIncome(Conversor.asCurrency(presenter.getDaySales()));
        adapter.setPerformance(presenter.getImprovement(getApplicationContext()));
        for (int i = 1; i<= DateTime.now().getDayOfMonth(); i++){
            adapter.addToDaySales(new Entry(i, presenter.getSalesFromDay(i)));
        }
        for (MDepartment d : presenter.getAllDepartments()){
            adapter.addToDeparmentSales(new PieEntry(presenter.getSaleFromDepartment(d.id),
                    d.departmentName));
        }
        adapter.setBestSeller(presenter.getBestSellerOfTheDay());
    }

    @Override
    public void onLoadingComplete() {
        super.onLoadingComplete();
        tvTodayIncome.setText(adapter.getTodayIncome());
        tvPerformance.setText(adapter.getPerformance());
        setPerformanceChart(adapter.getDaySalesEntries());
        setCategorySalesChart(adapter.getDepartmentSaleEntries());
        setBestProductChart(adapter.getBestSeller());
    }

    @Override
    protected void init() {
        super.init();
        presenter = new HomePresenter(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

    }

    private void setPerformanceChart( List<Entry> entries ){
        LineDataSet dataSet = new LineDataSet(entries, null);
        dataSet.setColors(new int[]{R.color.colorAccent}, getApplicationContext());
        dataSet.setLineWidth(2);
        LineData lineData = new LineData(dataSet);
        chartPerformance.getLegend().setEnabled(false);
        chartPerformance.setDescription(null);
        chartPerformance.setData(lineData);
        chartPerformance.getData().setValueFormatter(new ChartValueFormatter());
        chartPerformance.getData().setDrawValues(false);
        chartPerformance.invalidate();
    }

    private void setCategorySalesChart(List<PieEntry> entries){
        PieDataSet set = new PieDataSet(entries, null);
        set.setColors(new int[]{R.color.chart_red, R.color.chart_red_dark,
                R.color.chart_purple, R.color.chart_blue, R.color.chart_blue_light,
                R.color.chart_green, R.color.chart_green_light}, getApplicationContext());
        PieData data = new PieData(set);
        data.setValueFormatter(new ChartValueFormatter());
        chartCatSales.setData(data);
        chartCatSales.getLegend().setEnabled(false);
        chartCatSales.setDescription(null);
        chartCatSales.getData().setValueTextColor(Color.WHITE);
        chartCatSales.getData().setValueTextSize(16);
        chartCatSales.invalidate();
    }

    private void setBestProductChart(String data){
        tvStartProduct.setText(data);
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
        if (id == R.id.nav_day_report){
            goTo(DayReportActivity.class);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goTo(Class activity) {
        startActivity(new Intent(HomeScreenActivity.this, activity));
    }

    private class HomeActivityDataAdapter {

        String todayIncome;
        String performance;
        String bestSeller;

        List<Entry> getDaySalesEntries() {
            return daySalesEntries;
        }

        List<PieEntry> getDepartmentSaleEntries() {
            return departmentSaleEntries;
        }

        List<Entry> daySalesEntries;
        List<PieEntry> departmentSaleEntries = new ArrayList<>();

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

        void addToDaySales(Entry daySale){
            if (daySalesEntries == null)
                daySalesEntries = new ArrayList<>();
            daySalesEntries.add(daySale);
        }

        void addToDeparmentSales(PieEntry departmentSale){
            if (departmentSaleEntries == null)
                departmentSaleEntries = new ArrayList<>();
            departmentSaleEntries.add(departmentSale);
        }

        String getBestSeller() {
            return bestSeller;
        }

        void setBestSeller(String bestSeller) {
            this.bestSeller = bestSeller;
        }
    }
}
