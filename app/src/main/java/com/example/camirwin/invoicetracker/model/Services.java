package com.example.camirwin.invoicetracker.model;

public class Services {

    private int Id;
    private int ClientId;
    private int InvoiceId;
    private String Name;
    private double Rate;
    private long LastWorkedDate;
    private double OutstandingBalance;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getClientId() {
        return ClientId;
    }

    public void setClientId(int clientId) {
        ClientId = clientId;
    }

    public int getInvoiceId() {
        return InvoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        InvoiceId = invoiceId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getRate() {
        return Rate;
    }

    public void setRate(double rate) {
        Rate = rate;
    }

    public long getLastWorkedDate() {
        return LastWorkedDate;
    }

    public void setLastWorkedDate(long lastWorkedDate) {
        LastWorkedDate = lastWorkedDate;
    }

    public double getOutstandingBalance() {
        return OutstandingBalance;
    }

    public void setOutstandingBalance(double outstandingBalance) {
        OutstandingBalance = outstandingBalance;
    }

}
