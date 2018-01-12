package com.pocket.poktsales.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
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
import com.pocket.poktsales.interfaces.RequiredViewOps;
import com.pocket.poktsales.presenter.ReportPresenter;
import com.pocket.poktsales.utils.ChartValueFormatter;
import com.pocket.poktsales.utils.Conversor;
import com.pocket.poktsales.utils.DialogBuilder;
import com.pocket.poktsales.utils.MonthPerformanceMarker;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by MAV1GA on 08/01/2018.
 */

public class BusinessReportActivity extends BaseActivity implements RequiredViewOps.BusinessReportViewOps{

    @BindView(R.id.tv_month_income)
    TextView tvMonthIncome;

    @BindView(R.id.chart_sales_performance)
    LineChart chartPerformance;

    @BindView(R.id.chart_category_month_performance)
    PieChart categoryChart;

    @BindView(R.id.fab)
    FloatingActionButton btnPickDate;

    ActivityAdapter activityAdapter;

    ReportPresenter presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.layoutResourceId = R.layout.activity_reports;

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init() {
        super.init();
        if (getSupportActionBar()!= null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presenter = new ReportPresenter(this);
        start();
        btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBuilder.newDatePickerDialogMonthOnly(BusinessReportActivity.this, new DialogBuilder.DialogInteractionListener.OnDateSelected() {
                    @Override
                    public void onDateSelected(DateTime date) {

                    }
                }).show();
            }
        });
    }

    @Override
    public void onLoadingPrepare() {
        super.onLoadingPrepare();
        if (activityAdapter == null)
            activityAdapter = new ActivityAdapter();
    }

    @Override
    public void onLoading() {
        super.onLoading();
        activityAdapter.setMonthIncome(
                Conversor.asCurrency(presenter.getMonthSales(activityAdapter.getMonth(), activityAdapter.getYear())));
        activityAdapter.setMonthPerformanceEntries(presenter.getMonthPerformance(activityAdapter.getYear(), activityAdapter.getMonth()));
        activityAdapter.setMonthCategorieEntries(presenter.getMonthPerformanceByCategory(activityAdapter.getMonth(), activityAdapter.getYear()));
    }

    @Override
    public void onLoadingComplete() {
        super.onLoadingComplete();
        tvMonthIncome.setText(activityAdapter.getMonthIncome());
        setPerformanceChart(activityAdapter.getMonthPerformanceEntries());
        setCategoriesChart(activityAdapter.getMonthCategorieEntries());
    }

    private void setCategoriesChart(List<PieEntry> monthPerformanceEntries) {
        PieDataSet set = new PieDataSet(monthPerformanceEntries, null);
        set.setColors(new int[]{R.color.chart_red, R.color.chart_red_dark,
                R.color.chart_purple, R.color.chart_blue, R.color.chart_blue_light,
                R.color.chart_green, R.color.chart_green_light}, getApplicationContext());
        PieData data = new PieData(set);
        data.setValueFormatter(new ChartValueFormatter());
        categoryChart.setData(data);
        categoryChart.getLegend().setEnabled(false);
        categoryChart.setDescription(null);
        categoryChart.getData().setValueTextColor(Color.WHITE);
        categoryChart.getData().setValueTextSize(16);
        categoryChart.invalidate();
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
        MonthPerformanceMarker mv = new MonthPerformanceMarker(this, R.layout.layout_marker);
        chartPerformance.setDragEnabled(true);
        chartPerformance.setMarkerView(mv);
    }

    class ActivityAdapter{
        int year;
        int month;
        List<Entry> monthPerformanceEntries;
        List<PieEntry> monthCategorieEntries;

        int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        List<Entry> getMonthPerformanceEntries() {
            return monthPerformanceEntries;
        }

        void setMonthPerformanceEntries(List<Entry> monthPerformanceEntries) {
            if (this.monthPerformanceEntries!= null)
                this.monthPerformanceEntries.clear();
            this.monthPerformanceEntries = monthPerformanceEntries;
        }

        void setMonthCategorieEntries(List<PieEntry> entries){
            if (monthCategorieEntries != null)
                this.monthCategorieEntries.clear();
            this.monthCategorieEntries = entries;
        }

        List<PieEntry> getMonthCategorieEntries(){
            if (monthCategorieEntries == null)
                monthCategorieEntries = new ArrayList<>();
            return monthCategorieEntries;
        }

        ActivityAdapter(){
            this.year = DateTime.now().getYear();
            this.month = DateTime.now().getMonthOfYear();
            monthPerformanceEntries = new ArrayList<>();
        }

        String getMonthIncome() {
            return monthIncome != null ? monthIncome : Conversor.asCurrency(0);
        }

        void setMonthIncome(String monthIncome) {
            this.monthIncome = monthIncome;
        }
        String monthIncome;
    }
}
