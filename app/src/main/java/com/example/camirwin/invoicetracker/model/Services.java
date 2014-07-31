package com.example.camirwin.invoicetracker.model;

import java.util.Date;

public class Services {

    private Integer Id;
    private Integer ClientId;
    private Integer InvoiceId;
    private String Name;
    private Double Rate;
    private Long LastWorkedDate;
    private Double OutstandingBalance;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getClientId() {
        return ClientId;
    }

    public void setClientId(Integer clientId) {
        ClientId = clientId;
    }

    public Integer getInvoiceId() {
        return InvoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        InvoiceId = invoiceId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Double getRate() {
        return Rate;
    }

    public void setRate(Double rate) {
        Rate = rate;
    }

    public Long getLastWorkedDate() {
        return LastWorkedDate;
    }

    public void setLastWorkedDate(Long lastWorkedDate) {
        LastWorkedDate = lastWorkedDate;
    }

    public Double getOutstandingBalance() {
        return OutstandingBalance;
    }

    public void setOutstandingBalance(Double outstandingBalance) {
        OutstandingBalance = outstandingBalance;
    }

    public Date getLastWorkedAsDateObject() {
        return new Date(getLastWorkedDate());
    }

}
