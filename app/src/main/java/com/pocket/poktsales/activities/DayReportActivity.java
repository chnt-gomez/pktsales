package com.pocket.poktsales.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.pocket.poktsales.R;
import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.interfaces.RequiredViewOps;
import com.pocket.poktsales.model.MDepartment;
import com.pocket.poktsales.model.MTicket;
import com.pocket.poktsales.presenter.DayReportPresenter;
import com.pocket.poktsales.utils.ChartValueFormatter;
import com.pocket.poktsales.utils.Conversor;
import com.pocket.poktsales.utils.DayPerformanceMarker;
import com.pocket.poktsales.utils.DialogBuilder;

import org.joda.time.DateTime;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MAV1GA on 19/12/2017.
 */



public class DayReportActivity extends BaseActivity implements RequiredViewOps.DayReportOps {

    @BindView(R.id.tv_performance)
    TextView tvPerformance;

    @BindView(R.id.tv_today_income)
    TextView tvIncome;

    @BindView(R.id.chart_day_performance)
    BarChart dayChart;

    @BindView(R.id.chart_day_department_sales)
    PieChart categoryChart;

    @BindView(R.id.fab)
    FloatingActionButton fabDate;

    RequiredPresenterOps.DayReportPresenterOps presenter;
    ActivityAdapter activityAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_report);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        init();
    }

    @Override
    protected void init() {
        super.init();
        presenter = new DayReportPresenter(this);
        loadingBar = (ProgressBar)findViewById(R.id.progressBar);
        if (getSupportActionBar()!= null)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        start();
        fabDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });
    }

    private void pickDate() {
        DialogBuilder.newDatePickerDialog(this, new DialogBuilder.DialogInteractionListener.OnDateSelected() {
            @Override
            public void onDateSelected(DateTime date) {
                activityAdapter.setDate(date);
                start();
            }
        }).show();
    }

    @Override
    public void onLoadingPrepare() {
        super.onLoadingPrepare();
        if (activityAdapter == null){
            activityAdapter = new ActivityAdapter();
        }
        dayChart.clear();
        categoryChart.clear();
        categoryChart.setCenterText("");
        if (activityAdapter.categorySales != null)
            activityAdapter.categorySales.clear();
        if (activityAdapter.dayPerformance != null)
            activityAdapter.dayPerformance.clear();
    }

    @Override
    public void onLoading() {
        super.onLoading();
        activityAdapter.setAllTickets(presenter.getTickets(activityAdapter.getDateTime().withTimeAtStartOfDay().getMillis(),
                activityAdapter.getDateTime().plusDays(1).withTimeAtStartOfDay().getMillis()));
        activityAdapter.setDayTotal(presenter.getSaleOfTheDay(activityAdapter.getDateTime().withTimeAtStartOfDay().getMillis(),
                activityAdapter.getDateTime().plusDays(1).withTimeAtStartOfDay().getMillis()));
        for (int i=0; i<= 23; i++){
            DateTime now = activityAdapter.getDateTime().withTimeAtStartOfDay().withTime(i, 0, 0, 0);
            DateTime nowPlus = now.plusMinutes(59).plusSeconds(59).plusMillis(999);
            activityAdapter.addTimeSale(new BarEntry(i, presenter.geTotalSalesAtTime(now.getMillis(), nowPlus.getMillis())));
        }
        for (MDepartment d : presenter.getAllActiveDepartments()){
            activityAdapter.addToCategorySales(new PieEntry(presenter.getSalesFromDepartment(d.id,
                    activityAdapter.getDateTime().withTimeAtStartOfDay().getMillis(),
                   activityAdapter.getDateTime().withTimeAtStartOfDay().plusDays(1).getMillis()), d.departmentName));
        }
    }

    @Override
    public void onLoadingComplete() {
        super.onLoadingComplete();
        tvIncome.setText(activityAdapter.getDayTotal());
        tvPerformance.setText(activityAdapter.getDateTime().toString("dd - MMMM"));
        setPerformanceChart(activityAdapter.getDayPerformance());
        setCategorySalesChart(activityAdapter.getCategorySales());
    }

    private void setPerformanceChart( List<BarEntry> entries ){
        BarDataSet dataSet = new BarDataSet(entries, null);
        dataSet.setColors(new int[]{R.color.colorAccent}, getApplicationContext());
        dataSet.setHighlightEnabled(true);
        BarData barData = new BarData(dataSet);
        dayChart.getXAxis().setDrawLabels(false);
        dayChart.getLegend().setEnabled(false);
        dayChart.setDescription(null);
        dayChart.setData(barData);
        dayChart.setTouchEnabled(true);
        dayChart.getData().setValueFormatter(new ChartValueFormatter());
        dayChart.getData().setDrawValues(false);
        dayChart.invalidate();
        DayPerformanceMarker mv = new DayPerformanceMarker(this, R.layout.layout_marker);
        dayChart.setDragEnabled(true);
        dayChart.setMarkerView(mv);
    }

    private void setCategorySalesChart(List<PieEntry> entries){
        PieDataSet set = new PieDataSet(entries, null);
        List<MDepartment> deps = presenter.getAllActiveDepartments();
        int[] colors = new int[deps.size()];
        for (int i=0; i<deps.size(); i++){
            colors[i] = deps.get(i).colorResource;
        }
        set.setColors(colors);
        set.setDrawValues(false);
        PieData data = new PieData(set);
        data.setValueFormatter(new ChartValueFormatter());
        categoryChart.setData(data);
        categoryChart.getLegend().setEnabled(false);
        categoryChart.setDescription(null);
        categoryChart.setDrawEntryLabels(false);
        categoryChart.setCenterTextColor(getResources().getColor(R.color.colorPrimary));
        categoryChart.setCenterTextSize(16);
        categoryChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry pe = (PieEntry)e;
                categoryChart.setCenterText(String.format("%s \n %s", pe.getLabel(), Conversor.asCurrency(pe.getValue())));
            }

            @Override
            public void onNothingSelected() {
                categoryChart.setCenterText("");
            }
        });
        categoryChart.invalidate();
    }

    class ActivityAdapter {
        List<MTicket> allTickets;
        DateTime dateTime;
        List<PieEntry> categorySales;
        String dayTotal;

        void setDate(DateTime date) {
            this.dateTime = date;
        }

        DateTime getDateTime(){
            new DateTime();
            return dateTime != null ? dateTime : DateTime.now();
        }

        public void setCategorySales(List<PieEntry> categorySales) {
            this.categorySales = categorySales;
        }

        List<PieEntry> getCategorySales() {
            return categorySales;
        }

        void addToCategorySales(PieEntry entry) {
            if (categorySales == null)
                    categorySales = new ArrayList<>();
            categorySales.add(entry);
        }

        public void setDayPerformance(List<BarEntry> dayPerformance) {
            this.dayPerformance = dayPerformance;
        }

        List<BarEntry> getDayPerformance() {
            return dayPerformance!= null ? dayPerformance : new ArrayList<BarEntry>();
        }

        List<BarEntry> dayPerformance;

        void addTimeSale(BarEntry a){
            if (dayPerformance == null)
                dayPerformance = new ArrayList<>();
            dayPerformance.add(a);
        }

        public List<MTicket> getAllTickets() {
            return allTickets;
        }

        void setAllTickets(List<MTicket> allTickets) {
            this.allTickets = allTickets;
        }

        String getDayTotal() {
            return dayTotal;
        }

        void setDayTotal(String dayTotal) {
            this.dayTotal = dayTotal;
        }

    }
}
