package com.mybank.model;

public class Bank {
    protected String name;
    protected String id;
    protected double transferfee;
    protected double exchangefee;
    public Bank(String name,String id){
        this.name = name;
        this.id = id;
    }
    public void setTransferFee(double transferfee){
        this.transferfee = transferfee;
    }
    public void setExchangeFee(double exchangefee){
        this.exchangefee = exchangefee;
    }
    public double getTransferfee(){
        return transferfee;
    }
    public double getExchangefee(){
        return exchangefee;
    }
    public String getID(){
        return id;
    }
}
