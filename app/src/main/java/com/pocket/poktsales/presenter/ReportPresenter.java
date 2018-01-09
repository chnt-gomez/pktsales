package com.pocket.poktsales.presenter;

import com.github.mikephil.charting.data.Entry;
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
        float total = 0;
        for (Sale s : Sale.findWithQuery(
                Sale.class, "SELECT * from Sale s, Product p, Department d, Ticket t WHERE " +
                        "p.department = d.id AND d.id = ? AND s.product = p.id AND s.id = t.id AND t.date_time >= ? " +
                        "AND t.date_time < ?", String.valueOf(departmentId), String.valueOf(start.getMillis()), String.valueOf(end)
        )){
            total += s.getSaleTotal();
        }
        return total;
    }



}
