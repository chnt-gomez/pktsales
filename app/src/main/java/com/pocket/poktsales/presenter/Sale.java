package com.pocket.poktsales.presenter;

import com.orm.SugarRecord;

/**
 * Created by MAV1GA on 04/09/2017.
 */

class Sale extends SugarRecord {

    private String saleConcept;
    private float productAmount;
    private float saleTotal;
    private Product product;
    private Ticket ticket;

    public Sale(){}

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
