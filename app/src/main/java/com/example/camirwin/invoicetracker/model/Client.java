package com.example.camirwin.invoicetracker.model;

public class Client {

    private int Id;
    private String Name;
    private String Location;
    private String ContactFirstName;
    private String ContactLastName;
    private String ContactEmail;
    private String ContactPhone;
    private double OutstandingBalance;
    private long LastInvoiceDate;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
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

    public double getOutstandingBalance() {
        return OutstandingBalance;
    }

    public void setOutstandingBalance(double outstandingBalance) {
        OutstandingBalance = outstandingBalance;
    }

    public long getLastInvoiceDate() {
        return LastInvoiceDate;
    }

    public void setLastInvoiceDate(long lastInvoiceDate) {
        LastInvoiceDate = lastInvoiceDate;
    }

}
