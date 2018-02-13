package com.pocket.poktsales.presenter;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.interfaces.RequiredViewOps;
import com.pocket.poktsales.model.MDepartment;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MAV1GA on 08/01/2018.
 */

public class ReportPresenter extends BasePresenter implements RequiredPresenterOps.ReportsPresenterOps{

    private RequiredViewOps.BusinessReportViewOps view;

    public ReportPresenter(RequiredViewOps.BusinessReportViewOps view){
        this.view = view;
    }

    @Override
    public float getMonthSales(int monthOfYear, int year) {
        DateTime start = new DateTime(year, monthOfYear, 1, 0, 0);
        DateTime end = start.plusMonths(1);
        float total = 0;
        for (Ticket t : Ticket.find(Ticket.class, "date_time >= ? AND date_time < ? AND ticket_status = ?",
        String.valueOf(start.getMillis()),
        String.valueOf(end.getMillis()),
        String.valueOf(Ticket.TICKET_APPLIED))){
            total += t.getSaleTotal();
        }
        return total;
    }

    @Override
    public List<Entry> getMonthPerformance(int year, int monthOfYear) {
        DateTime date = new DateTime(year, monthOfYear,1, 0, 0);
        int max = date.dayOfMonth().getMaximumValue();
        List<Entry> entries = new ArrayList<>();
        for (int i=1; i<=max; i++){
            String args[] = {
                    String.valueOf(date.withDayOfMonth(i).withTimeAtStartOfDay().getMillis()),
                    String.valueOf(date.withDayOfMonth(i).plusDays(1).withTimeAtStartOfDay().getMillis()),
                    String.valueOf(Ticket.TICKET_APPLIED)
            } ;
            float total = 0;
            for (Ticket t : Ticket.find(Ticket.class, "date_time >= ? AND date_time < ? AND ticket_status = ?", args)){
                total += t.getSaleTotal();
            }
            entries.add(new Entry(i, total));
        }
        return entries;
    }

    @Override
    public List<MDepartment> getDepartments() {
        return fromDepartmentList(Department.find(Department.class, "department_status = ?", String.valueOf(Department.ACTIVE)));
    }

    @Override
    public float getSaleByDepartment(long departmentId, int monthOfYear, int year) {
        DateTime start = new DateTime(year, monthOfYear, 1, 0, 0);
        DateTime end = start.plusMonths(1);
        float total = 0f;
        List<Ticket> tickets = Ticket.find(Ticket.class, "date_time >= ? AND date_time < ?",
                String.valueOf(start.getMillis()), String.valueOf(end.getMillis()));

        List<Sale> sales = new ArrayList<>();
        for (Ticket t: tickets){
            sales.addAll(Sale.find(Sale.class, "ticket = ?", String.valueOf(t.getId())));
        }

        for (Sale s : sales){
            if (s.getProduct().getDepartment() != null){
                if (s.getProduct().getDepartment().getId() == departmentId){
                    total += s.getSaleTotal();
                }
            }
        }
        return total;
    }

    @Override
    public List<PieEntry> getMonthPerformanceByCategory(int monthOfYear, int year) {
        List<PieEntry> entries = new ArrayList<>();
        for (MDepartment m : getDepartments()){
            entries.add(new PieEntry(getSaleByDepartment(m.id, monthOfYear, year), m.departmentName));
        }
        return entries;
    }


}
