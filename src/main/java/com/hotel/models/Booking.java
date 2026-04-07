package com.hotel.models;

import java.io.Serializable;
import java.time.LocalDate;

public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;

    private String bookingId;
    private String roomNumber;
    private String customerId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String amenities;
    private double extraCost;

    public Booking(String bookingId, String roomNumber, String customerId, LocalDate checkInDate, LocalDate checkOutDate) {
        this.bookingId = bookingId;
        this.roomNumber = roomNumber;
        this.customerId = customerId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.amenities = "";
        this.extraCost = 0.0;
    }

    public Booking(String bookingId, String roomNumber, String customerId, LocalDate checkInDate, LocalDate checkOutDate, String amenities, double extraCost) {
        this.bookingId = bookingId;
        this.roomNumber = roomNumber;
        this.customerId = customerId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.amenities = amenities;
        this.extraCost = extraCost;
    }

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public LocalDate getCheckInDate() { return checkInDate; }
    public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }

    public LocalDate getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(LocalDate checkOutDate) { this.checkOutDate = checkOutDate; }

    public String getAmenities() { return amenities; }
    public void setAmenities(String amenities) { this.amenities = amenities; }

    public double getExtraCost() { return extraCost; }
    public void setExtraCost(double extraCost) { this.extraCost = extraCost; }
}
