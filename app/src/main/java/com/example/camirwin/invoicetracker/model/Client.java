package com.example.camirwin.invoicetracker.model;

import java.util.Date;

public class Client {

    private Integer Id;
    private String Name;
    private String Location;
    private String ContactFirstName;
    private String ContactLastName;
    private String ContactEmail;
    private String ContactPhone;
    private Double OutstandingServices;
    private Double OutstandingDeliverables;
    private Double OutstandingExpenses;
    private Long LastInvoiceDate;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getContactFirstName() {
        return ContactFirstName;
    }

    public void setContactFirstName(String contactFirstName) {
        ContactFirstName = contactFirstName;
    }

    public String getContactLastName() {
        return ContactLastName;
    }

    public void setContactLastName(String contactLastName) {
        ContactLastName = contactLastName;
    }

    public String getContactEmail() {
        return ContactEmail;
    }

    public void setContactEmail(String contactEmail) {
        ContactEmail = contactEmail;
    }

    public String getContactPhone() {
        return ContactPhone;
    }

    public void setContactPhone(String contactPhone) {
        ContactPhone = contactPhone;
    }

    public Double getOutstandingServices() {
        return OutstandingServices;
    }

    public void setOutstandingServices(Double outstandingServices) {
        OutstandingServices = outstandingServices;
    }

    public Double getOutstandingDeliverables() {
        return OutstandingDeliverables;
    }

    public void setOutstandingDeliverables(Double outstandingDeliverables) {
        OutstandingDeliverables = outstandingDeliverables;
    }

    public Double getOutstandingExpenses() {
        return OutstandingExpenses;
    }

    public void setOutstandingExpenses(Double outstandingExpenses) {
        OutstandingExpenses = outstandingExpenses;
    }

    public Long getLastInvoiceDate() {
        return LastInvoiceDate;
    }

    public void setLastInvoiceDate(Long lastInvoiceDate) {
        LastInvoiceDate = lastInvoiceDate;
    }

    public Date getLastInvoiceAsDateObject() {
        return new Date(getLastInvoiceDate());
    }

    public Double getOutstandingBalance() { return OutstandingServices + OutstandingDeliverables + OutstandingExpenses; }

}
