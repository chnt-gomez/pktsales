package com.pocket.poktsales.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;
import com.pocket.poktsales.R;
import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.interfaces.RequiredViewOps;
import com.pocket.poktsales.model.MTicket;
import com.pocket.poktsales.presenter.DayReportPresenter;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by MAV1GA on 19/12/2017.
 */

public class DayReportActivity extends BaseActivity implements RequiredViewOps.DayReportOps {

    @BindView(R.id.tv_performance)
    TextView tvPerformance;

    @BindView(R.id.tv_today_income)
    TextView tvIncome;

    RequiredPresenterOps.DayReportPresenterOps presenter;
    ActivityAdapter activityAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        layoutResourceId = R.layout.activity_day_report;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init() {
        super.init();
        presenter = new DayReportPresenter(this);
        loadingBar = (ProgressBar)findViewById(R.id.progressBar);
        if (getSupportActionBar()!= null)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        start();
    }

    @Override
    public void onLoadingPrepare() {
        super.onLoadingPrepare();
        if (activityAdapter == null){
            activityAdapter = new ActivityAdapter();
        }
    }

    @Override
    public void onLoading() {
        super.onLoading();
        activityAdapter.setAllTickets(presenter.getTickets(DateTime.now().withTimeAtStartOfDay().getMillis(),
                DateTime.now().withTime(23, 59, 59, 999).getMillis()));
        activityAdapter.setDayTotal(presenter.getSaleOfTheDay(DateTime.now().withTimeAtStartOfDay().getMillis(),
                DateTime.now().withTime(23, 59, 59, 999).getMillis()));
        activityAdapter.setDate(DateTime.now().toString("dd - MMMM"));
        for (int i=0; i<= 23; i++){
            long from = DateTime.now().withHourOfDay(i).getMillis();
            long to = DateTime.now().withHourOfDay(i).plusMinutes(59).plusSeconds(59).plusMillis(999).getMillis();
            presenter.geTotalSalesAtTime(from, to);
        }
    }

    @Override
    public void onLoadingComplete() {
        super.onLoadingComplete();
        tvIncome.setText(activityAdapter.getDayTotal());
        tvPerformance.setText(activityAdapter.getDate());
    }

    class ActivityAdapter {
        List<MTicket> allTickets;
        String dayTotal;
        String date;
        List<Entry> dayPerformance;

        String getDate() {
            return date;
        }

        void setDate(String date) {
            this.date = date;
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
