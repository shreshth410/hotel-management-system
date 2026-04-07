package com.hotel.models;

public class DeluxeRoom extends AbstractRoom {
    public DeluxeRoom(String roomNumber, double pricePerNight, boolean available) {
        super(roomNumber, RoomType.DELUXE, pricePerNight, available);
    }

    @Override
    public double calculateTariff(int nights) {
        return (nights * pricePerNight) + 50.0; // Flat extra charge
    }
}
