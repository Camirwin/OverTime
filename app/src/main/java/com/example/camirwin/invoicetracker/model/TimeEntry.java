package com.example.camirwin.invoicetracker.model;

import java.util.Date;

public class TimeEntry {

    private Integer Id;
    private Integer ClientId;
    private Integer ServiceId;
    private Integer InvoiceId;
    private Long ClockInDate;
    private Long ClockOutDate;
    private Double Rate;

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

    public Integer getServiceId() {
        return ServiceId;
    }

    public void setServiceId(Integer serviceId) {
        ServiceId = serviceId;
    }

    public Integer getInvoiceId() {
        return InvoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        InvoiceId = invoiceId;
    }

    public Long getClockInDate() {
        return ClockInDate;
    }

    public void setClockInDate(Long clockInDate) {
        ClockInDate = clockInDate;
    }

    public Long getClockOutDate() {
        return ClockOutDate;
    }

    public void setClockOutDate(Long clockOutDate) {
        ClockOutDate = clockOutDate;
    }

    public Double getRate() {
        return Rate;
    }

    public void setRate(Double rate) {
        Rate = rate;
    }

    public Date getClockInAsDateObject() {
        return new Date(ClockInDate);
    }

    public Date getClockOutAsDateObject() {
        return new Date(ClockOutDate);
    }

    public Double getEarnedIncome() {
        if (!ClockOutDate.equals(Long.valueOf(0))) {
            return (Double.valueOf((ClockOutDate - ClockInDate) / 3600000.00)) * Rate;
        }
        return (Double.valueOf((System.currentTimeMillis() - ClockInDate) / 3600000.00)) * Rate;
    }
}
