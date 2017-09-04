package com.pocket.poktsales.model;

import com.orm.SugarRecord;

/**
 * Created by MAV1GA on 04/09/2017.
 */

public class Ticket extends SugarRecord {

    public long dateTime;

    public float saleTotal;

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public float getSaleTotal() {
        return saleTotal;
    }

    public void setSaleTotal(float saleTotal) {
        this.saleTotal = saleTotal;
    }
}
