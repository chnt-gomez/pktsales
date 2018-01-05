package com.pocket.poktsales.presenter;

import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.interfaces.RequiredViewOps;
import com.pocket.poktsales.model.MDepartment;
import com.pocket.poktsales.model.MTicket;
import com.pocket.poktsales.utils.Conversor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MAV1GA on 19/12/2017.
 */

public class DayReportPresenter extends BasePresenter implements RequiredPresenterOps.DayReportPresenterOps {

    RequiredViewOps.DayReportOps view;

    public DayReportPresenter(RequiredViewOps.DayReportOps view){
        this.view = view;
    }

    @Override
    public List<MTicket> getTickets(long dateTimeStart, long dateTimeEnd) {
        return fromTicketList(Ticket.find(Ticket.class, "date_time >= ? AND date_time < ?"
                , String.valueOf(dateTimeStart), String.valueOf(dateTimeEnd)));
    }

    @Override
    public List<MTicket> getTickets(long dateTime) {
        return null;
    }

    @Override
    public String getSaleOfTheDay(long dateTimeStart, long dateTimeEnd) {
        float total = 0;
        for (MTicket t : getTickets(dateTimeStart, dateTimeEnd))
            total += t.saleTotal;
        return Conversor.asCurrency(total);
    }

    @Override
    public String getSaleOfTheDay(long dateTime) {
        return null;
    }

    @Override
    public float geTotalSalesAtTime(long from, long to) {
        float total = 0f;
        for (Ticket t : Ticket.find(Ticket.class, "date_time >= ? AND date_time < ?", String.valueOf(from),
                String.valueOf(to))){
            total += t.getSaleTotal();
        }
        return total;
    }

    @Override
    public float getSalesFromDepartment(long departmentId, long from, long to) {
        float total = 0;
        for (Sale s : Sale.findWithQuery(
                Sale.class, "SELECT * from Sale s, Product p, Department d, Ticket t WHERE " +
                        "p.department = d.id AND d.id = ? AND s.product = p.id AND s.id = t.id AND t.date_time >= ? " +
                        "AND t.date_time < ?", String.valueOf(departmentId), String.valueOf(from), String.valueOf(to)
        )){
            total += s.getSaleTotal();
        }
        return total;
    }

    @Override
    public List<MDepartment> getAllActiveDepartments() {
        return fromDepartmentList(
                Department.find(Department.class, "department_status = ?", String.valueOf(Department.ACTIVE)));
    }
}
