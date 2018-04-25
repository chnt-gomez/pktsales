package com.pocket.poktsales.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import com.pocket.poktsales.utils.DialogBuilder;
import com.pocket.poktsales.utils.MonthPerformanceMarker;
import com.pocket.poktsales.utils.ReportView;

import org.joda.time.DateTime;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    RequiredPresenterOps.HomePresenterOps presenter;
    RecentSaleAdapter recentSaleAdapter;

    String todayIncome;
    String performance;
    List<Entry> qDaySales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        init();
        boolean firstStart = getPreferenceBoolean(SettingsActivity.KEY_IS_FIRST_START, true);
        boolean newVersion = getPreferenceBoolean(SettingsActivity.KEY_V_1_1_2, true);
        if (firstStart){
            startIntro();
        }else{
            if (newVersion){
                //showNewVersion();
            }
        }
    }

    private void showNewVersion(boolean all) {
        Intent intent = new Intent(this, NewVersionActivity.class);
        Bundle args = new Bundle();
        if (all)
            args.putBoolean("all", true);
        args.putString("version", SettingsActivity.KEY_V_1_1_2);
        intent.putExtras(args);
        startActivityForResult(intent, 102);
    }


    private void startIntro(){
        Intent intent = new Intent(this, IntroActivity.class);
        startActivityForResult(intent, 101);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101){
            if (resultCode == RESULT_OK){
                saveBooleanPreference(SettingsActivity.KEY_IS_FIRST_START, false);
            }
        }
        if (requestCode == 102){
            if (resultCode == RESULT_OK){
                saveBooleanPreference(SettingsActivity.KEY_V_1_1_2, false);
            }
        }
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
        if (qDaySales == null)
            qDaySales = new ArrayList<>();
        else
            qDaySales.clear();
        recentSaleAdapter.clear();
    }

    @Override
    public void onLoading() {
        super.onLoading();
        todayIncome = Conversor.asCurrency(presenter.getDaySales());
        performance = presenter.getImprovement(getApplicationContext());
        for (int i = 1; i<= DateTime.now().getDayOfMonth(); i++){
            qDaySales.add(new Entry(i, presenter.getSalesFromDay(i)));
        }
        recentSaleAdapter.addAll(presenter.getRecentSales());
    }

    @Override
    public void onLoadingComplete() {
        super.onLoadingComplete();
        tvTodayIncome.setText(todayIncome);
        tvPerformance.setText(performance);
        setPerformanceChart(qDaySales);
        recentSaleAdapter.notifyDataSetChanged();
    }

    @Override
    protected void init() {
        presenter = new HomePresenter(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        cvAdvertising.setVisibility(View.GONE);
        recentSaleAdapter = new RecentSaleAdapter(getApplicationContext());
        lvRecentSales.setAdapter(recentSaleAdapter);
        lvRecentSales.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReportView.showReport(HomeScreenActivity.this, presenter.getReport(id));
            }
        });
        loadAd();
    }

    private void loadAd(){
        MobileAds.initialize(this, "ca-app-pub-2236350735048598~2611871037");
        AdView mAdView = findViewById(R.id.adView);
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
        if (id == R.id.nav_intro){
            startIntro();
        }
        if (id == R.id.nav_rate){
            rate();
        }
        if (id == R.id.nav_version_features){
            //showNewVersion();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void rate(){
        Uri uri = Uri.parse("market://details?id=com.pocket.poktsales");
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=com.pocket.poktsales")));
        }
    }

    private void goTo(Class activity) {
        startActivity(new Intent(HomeScreenActivity.this, activity));
    }
}
