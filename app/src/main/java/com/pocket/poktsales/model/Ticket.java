package com.pocket.poktsales.model;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.pocket.poktsales.R;

/**
 * Created by MAV1GA on 04/09/2017.
 */

public class Ticket extends SugarRecord {

    private long dateTime;
    private float saleTotal;
    private String ticketReference;

    public int getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(int ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    private int ticketStatus;

    @Ignore
    public static final int TICKET_APPLIED = 1;
    @Ignore
    public static final int TICKET_PENDING = 0;
    @Ignore
    public static final int TICKET_CANCELED = 0;

    public int getImageRes() {
        if (imageRes == 0)
            return R.drawable.ic_receipt;
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    private int imageRes;

    public String getTicketReference() {
        if (ticketReference == null)
            return "";
        return ticketReference;
    }

    public void setTicketReference(String ticketReference) {
        this.ticketReference = ticketReference;
    }

    public Ticket(){}

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
