package com.pocket.poktsales.model;

import com.pocket.poktsales.utils.Conversor;

import java.util.ArrayList;
import java.util.List;

public class MReport {

    public String ticketNo;
    public String businessName;
    public String saleTotal;
    public String businessAddress;
    public List<MSale> listSale;

    public MReport(){
        ticketNo="0";
        businessName="";
        saleTotal= Conversor.asCurrency(0);
        businessAddress="";
        listSale = new ArrayList<>();
    }

}
