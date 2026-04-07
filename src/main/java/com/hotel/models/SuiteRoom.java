package com.hotel.models;

public class SuiteRoom extends AbstractRoom {
    public SuiteRoom(String roomNumber, double pricePerNight, boolean available) {
        super(roomNumber, RoomType.SUITE, pricePerNight, available);
    }

    @Override
    public double calculateTariff(int nights) {
        return (nights * pricePerNight) + 150.0; // Premium surcharge
    }
}
