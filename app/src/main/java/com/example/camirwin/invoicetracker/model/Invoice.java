package com.example.camirwin.invoicetracker.model;

public class Invoice {

    private int Id;
    private int ClientId;
    private long CreatedDate;
    private long SentDate;
    private long PayedDate;

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

    public long getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(long createdDate) {
        CreatedDate = createdDate;
    }

    public long getSentDate() {
        return SentDate;
    }

    public void setSentDate(long sentDate) {
        SentDate = sentDate;
    }

    public long getPayedDate() {
        return PayedDate;
    }

    public void setPayedDate(long payedDate) {
        PayedDate = payedDate;
    }

}
