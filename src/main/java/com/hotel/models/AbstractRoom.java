package com.hotel.models;

import java.io.Serializable;

public abstract class AbstractRoom implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected String roomNumber;
    protected RoomType roomType;
    protected double pricePerNight;
    protected boolean available;

    public AbstractRoom(String roomNumber, RoomType roomType, double pricePerNight, boolean available) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.available = available;
    }

    public abstract double calculateTariff(int nights);

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public RoomType getRoomType() { return roomType; }
    public void setRoomType(RoomType roomType) { this.roomType = roomType; }

    public double getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return roomNumber + " - " + roomType;
    }
}
