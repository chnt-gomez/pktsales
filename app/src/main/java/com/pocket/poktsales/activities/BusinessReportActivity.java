package com.pocket.poktsales.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.pocket.poktsales.R;
import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.interfaces.RequiredViewOps;
import com.pocket.poktsales.presenter.ReportPresenter;
import com.pocket.poktsales.utils.ChartValueFormatter;
import com.pocket.poktsales.utils.Conversor;
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
        presenter = new ReportPresenter(this);
        start();
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
    }

    @Override
    public void onLoadingComplete() {
        super.onLoadingComplete();
        tvMonthIncome.setText(activityAdapter.getMonthIncome());
        setPerformanceChart(activityAdapter.getMonthPerformanceEntries());
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

        int year;
        int month;

        public List<Entry> getMonthPerformanceEntries() {
            return monthPerformanceEntries;
        }

        void setMonthPerformanceEntries(List<Entry> monthPerformanceEntries) {
            if (this.monthPerformanceEntries!= null)
                this.monthPerformanceEntries.clear();
            this.monthPerformanceEntries = monthPerformanceEntries;
        }

        List<Entry> monthPerformanceEntries;

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
