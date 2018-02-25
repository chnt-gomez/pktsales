package com.pocket.poktsales.presenter;

import android.content.Context;

import com.pocket.poktsales.R;
import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.interfaces.RequiredViewOps;
import com.pocket.poktsales.model.MDepartment;
import com.pocket.poktsales.model.MSale;
import com.pocket.poktsales.model.MTicket;
import com.pocket.poktsales.utils.Conversor;

import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by MAV1GA on 02/11/2017.
 */

public class HomePresenter extends BasePresenter implements RequiredPresenterOps.HomePresenterOps{

    private RequiredViewOps view;

    public HomePresenter (RequiredViewOps view){
        this.view = view;
    }

    @Override
    public float getDaySales() {
        String todayStart  = String.valueOf(new DateTime().withTimeAtStartOfDay().getMillis());
        String todayEnd = String.valueOf(new DateTime().withTime(23,59,59,999));
        float total = 0F;
        for (Ticket t : Ticket.find(Ticket.class, "date_time >= ? AND date_time < ?",
                todayStart, todayEnd)) {
            total += t.getSaleTotal();
        }
        return total;
    }

    @Override
    public MTicket getTicket(long tickedId) {
        return fromTicket(Ticket.findById(Ticket.class, tickedId));
    }

    @Override
    public List<MSale> getSalesFromTicket(long ticketId) {
        return fromSaleList(Sale.find(Sale.class, "ticket = ?", String.valueOf(ticketId)),
                view.getResString(R.string.unknown));
    }

    @Override
    public String getBestSellerOfTheDay() {
        List<Product> results =
                Product.findWithQuery(Product.class, "SELECT *, COUNT(*) as products FROM Product p JOIN Sale s ON s.product = p.id JOIN " +
                                "Ticket t on t.id = s.ticket AND t.date_time >= ? AND t.date_time < ? GROUP BY product_name ORDER BY products DESC, product_name DESC", String.valueOf(getTodayStart()),
                        String.valueOf(getTodayEnd()));
        return (results.size() >= 1 ? results.get(0).getProductName() : "N/A");
    }

    @Override
    public float getSalesFromDay(int dayOfMonth) {
        float total = 0;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH)+1;
        long start = new DateTime(year, month, dayOfMonth, 0,0,0).getMillis();
        long end = new DateTime(year, month, dayOfMonth, 23,59,59).getMillis();
        String args[] = {String.valueOf(start), String.valueOf(end)};
        for (Ticket t : Ticket.find(Ticket.class, "date_time >= ? and date_time <= ?", args, null, null, null)){
            total += t.getSaleTotal();
        }
        return total;
    }



    @Override
    public float getSaleFromDepartment(long departmentId) {
        float total = 0;
        for (Sale s : Sale.findWithQuery(
                Sale.class, "SELECT * from Sale s, Product p, Department d, Ticket t WHERE " +
                        "p.department = d.id AND d.id = ? AND s.product = p.id AND s.ticket = t.id AND t.ticket_status = ?",
                        String.valueOf(departmentId),
                        String.valueOf(Ticket.TICKET_APPLIED)
        )){
            total += s.getSaleTotal();
        }
        return total;
    }

    @Override
    public float getYesterdaySales() {
        String yesterdayStart  = String.valueOf(new DateTime().minusDays(1).withTimeAtStartOfDay().getMillis());
        String yesterdayEnd = String.valueOf(new DateTime().minusDays(1).withTime(23,59,59,999).getMillis());
        float total = 0F;
        for (Ticket t : Ticket.find(Ticket.class, "date_time >= ? AND date_time < ?",
                yesterdayStart, yesterdayEnd)) {
            total += t.getSaleTotal();
        }
        return total;
    }

    @Override
    public List<MTicket> getRecentSales() {
        String[] query = {String.valueOf(DateTime.now().withTimeAtStartOfDay().getMillis()),
                String.valueOf(DateTime.now().plusDays(1).withTimeAtStartOfDay().getMillis())
        };
        return fromTicketList(Ticket.find(Ticket.class, "date_time >= ? and date_time < ?",
                query, null, "date_time DESC", "10"));
    }

    @Override
    public String getImprovement(Context context) {
        float overHand =  getDaySales() - getYesterdaySales();
        if (overHand >= 0){
            if (getYesterdaySales() > 0){
                float demiHand = overHand / getYesterdaySales();
                return String.format("%s %s", Conversor.asPerc(demiHand), context.getString(R.string.improvement_positive));
            }else{
                return context.getString(R.string.good_luck);
            }
        }else{
            return String.format("%s %s", Conversor.asCurrency(overHand*-1),context.getString(R.string.improvement_negative));
        }
    }

    @Override
    public List<MDepartment> getAllDepartments() {
        return fromDepartmentList(
                Department.find(Department.class, "department_status = ?", String.valueOf(Department.ACTIVE)));
    }
}
