package com.pocket.poktsales.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.pocket.poktsales.R;
import com.pocket.poktsales.adapters.RecentSaleAdapter;
import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.model.MDepartment;
import com.pocket.poktsales.model.MTicket;
import com.pocket.poktsales.presenter.HomePresenter;
import com.pocket.poktsales.utils.ChartValueFormatter;
import com.pocket.poktsales.utils.Conversor;
import com.pocket.poktsales.utils.MonthPerformanceMarker;

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

    @BindView(R.id.chart_performance)
    LineChart chartPerformance;

    @BindView(R.id.lv_recent_sales)
    ListView lvRecentSales;

    @BindView(R.id.card_advertising)
    CardView cvAdvertising;

    HomeActivityDataAdapter adapter;
    RequiredPresenterOps.HomePresenterOps presenter;
    RecentSaleAdapter recentSaleAdapter;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResourceId = R.layout.activity_home;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(getPreferenceString(SettingsActivity.KEY_BUSINESS_NAME,
                getString(R.string.set_default_business_name)));
        start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLoadingPrepare() {
        super.onLoadingPrepare();
        if (recentSaleAdapter != null)
            recentSaleAdapter.clear();
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
            adapter.addToDepartmentSales(new PieEntry(presenter.getSaleFromDepartment(d.id),
                    d.departmentName));
        }
        adapter.setRecentSales(presenter.getRecentSales());
    }

    @Override
    public void onLoadingComplete() {
        super.onLoadingComplete();
        tvTodayIncome.setText(adapter.getTodayIncome());
        tvPerformance.setText(adapter.getPerformance());
        setPerformanceChart(adapter.getDaySalesEntries());
        recentSaleAdapter.addAll(adapter.getRecentSales());
        recentSaleAdapter.notifyDataSetChanged();
    }

    @Override
    protected void init() {
        super.init();
        MobileAds.initialize(this, "ca-app-pub-2236350735048598~2611871037");
        recentSaleAdapter = new RecentSaleAdapter(this, R.layout.row_recent_sale, new ArrayList(0));
        presenter = new HomePresenter(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        lvRecentSales.setAdapter(recentSaleAdapter);

        mAdView = (AdView)findViewById(R.id.adView);
        cvAdvertising.setVisibility(View.GONE);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                cvAdvertising.setVisibility(View.VISIBLE);
            }
        });

    }

    private void setPerformanceChart( List<Entry> entries ){
        LineDataSet dataSet = new LineDataSet(entries, null);
        dataSet.setColors(new int[]{R.color.colorAccent}, getApplicationContext());
        dataSet.setLineWidth(2);
        dataSet.setCircleColor(R.color.colorPrimary);
        dataSet.setCircleHoleRadius(3);
        dataSet.setCircleRadius(6);
        dataSet.setHighlightEnabled(true);
        LineData lineData = new LineData(dataSet);
        chartPerformance.getXAxis().setDrawLabels(false);
        chartPerformance.getLegend().setEnabled(false);
        chartPerformance.setDescription(null);
        chartPerformance.setData(lineData);
        chartPerformance.setTouchEnabled(true);
        chartPerformance.getData().setValueFormatter(new ChartValueFormatter());
        chartPerformance.getData().setDrawValues(false);
        chartPerformance.invalidate();
        MonthPerformanceMarker mv = new MonthPerformanceMarker(this, R.layout.layout_marker, DateTime.now().getMonthOfYear(), DateTime.now().getYear());
        chartPerformance.setDragEnabled(true);
        chartPerformance.setMarkerView(mv);
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
        if (id== R.id.nav_business_report){
            goTo(BusinessReportActivity.class);
        }
        if (id == R.id.nav_business_settings){
            goTo(SettingsActivity.class);
        }
        if (id == R.id.nav_contact){
            goTo(ContactActivity.class);
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

        List<Entry> getDaySalesEntries() {
            return daySalesEntries;
        }

        List<PieEntry> getDepartmentSaleEntries() {
            return departmentSaleEntries;
        }

        List<Entry> daySalesEntries;
        List<PieEntry> departmentSaleEntries = new ArrayList<>();

        List<MTicket> getRecentSales() {
            return recentSales;
        }

        void setRecentSales(List<MTicket> recentSales) {
            this.recentSales = recentSales;
        }

        List<MTicket> recentSales;

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

        void addToDepartmentSales(PieEntry departmentSale){
            if (departmentSaleEntries == null)
                departmentSaleEntries = new ArrayList<>();
            departmentSaleEntries.add(departmentSale);
        }


    }
}
