package com.hotel.models;

import java.io.Serializable;
import java.time.LocalDate;

public class Bill implements Serializable {
    private static final long serialVersionUID = 1L;

    private String billId;
    private String bookingId;
    private String customerName;
    private String roomNumber;
    private double totalAmount;
    private LocalDate billDate;

    public Bill(String billId, String bookingId, String customerName, String roomNumber, double totalAmount, LocalDate billDate) {
        this.billId = billId;
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.roomNumber = roomNumber;
        this.totalAmount = totalAmount;
        this.billDate = billDate;
    }

    public String getBillId() { return billId; }
    public String getBookingId() { return bookingId; }
    public String getCustomerName() { return customerName; }
    public String getRoomNumber() { return roomNumber; }
    public double getTotalAmount() { return totalAmount; }
    public LocalDate getBillDate() { return billDate; }
}
