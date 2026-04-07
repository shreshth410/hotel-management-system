package com.hotel.models;

public class StandardRoom extends AbstractRoom {
    public StandardRoom(String roomNumber, double pricePerNight, boolean available) {
        super(roomNumber, RoomType.STANDARD, pricePerNight, available);
    }

    @Override
    public double calculateTariff(int nights) {
        return nights * pricePerNight;
    }
}
