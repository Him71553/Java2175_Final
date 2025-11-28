package com.mybank.model;

public class Bank {
    public final String name;
    public final String id;
    private double transferFee;
    private double exchangeFee;

    public Bank(String name, String id, double transferFee, double exchangeFee) {
        this.name = name;
        this.id = id;
        this.transferFee = transferFee;
        this.exchangeFee = exchangeFee;
    }

    public void setTransferFee(double newFee) {
        this.transferFee = newFee;
    }

    public void setExchangeFee(double newFee) {
        this.exchangeFee = newFee;
    }

    public double getTransferFee() {
        return transferFee;
    }

    public double getExchangeFee() {
        return exchangeFee;
    }
}
