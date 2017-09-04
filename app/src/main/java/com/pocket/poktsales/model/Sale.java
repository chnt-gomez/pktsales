package com.pocket.poktsales.model;

import com.orm.SugarRecord;

/**
 * Created by MAV1GA on 04/09/2017.
 */

public class Sale extends SugarRecord {

    public String saleConcept;
    public float productAmount;
    public float saleTotal;

    public Product product;
    public Ticket ticket;

    public String getSaleConcept() {
        return saleConcept;
    }

    public void setSaleConcept(String saleConcept) {
        this.saleConcept = saleConcept;
    }

    public float getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(float productAmount) {
        this.productAmount = productAmount;
    }

    public float getSaleTotal() {
        return saleTotal;
    }

    public void setSaleTotal(float saleTotal) {
        this.saleTotal = saleTotal;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}
