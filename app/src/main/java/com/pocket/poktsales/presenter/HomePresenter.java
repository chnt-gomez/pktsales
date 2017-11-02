package com.pocket.poktsales.presenter;

import android.content.Context;

import com.pocket.poktsales.R;
import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.interfaces.RequiredViewOps;
import com.pocket.poktsales.model.Department;
import com.pocket.poktsales.model.Product;
import com.pocket.poktsales.model.Sale;
import com.pocket.poktsales.model.Ticket;

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import rx.Observable;

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
                Sale.class, "SELECT * from Sale s, Product p, Department d WHERE " +
                        "p.department = d.id AND d.id = ? AND s.product = p.id", String.valueOf(departmentId)
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
    public String getImprovement(Context context) {
        float overHand =  getDaySales() - getYesterdaySales();
        if (overHand > 0){
            float demiHand = overHand / getYesterdaySales();
            demiHand *= 100;
            return String.format(Locale.getDefault(), context.getString(R.string.improvement_positive), demiHand);
        }else{
            return String.format(Locale.getDefault(), context.getString(R.string.improvement_negative), overHand);
        }
    }

    @Override
    public List<Department> getAllDepartments() {
        return Department.find(Department.class, "department_status = ?", String.valueOf(Department.ACTIVE));
    }

    @Override
    public Observable<List<Department>> getDepartments() {
        Observable<List<Department>> listObservable = Observable.fromCallable(new Callable<List<Department>>() {
            @Override
            public List<Department> call() throws Exception {
                return Department.find(Department.class, "department_status = ?", String.valueOf(Department.ACTIVE));
            }
        });
        return listObservable;
    }
}
