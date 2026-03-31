package com.hotel.services;

import com.hotel.models.Booking;
import com.hotel.models.Room;
import com.hotel.repository.DataStore;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class BookingService {

    private RoomService roomService = new RoomService();

    public List<Booking> getAllBookings() {
        return DataStore.getContainer().getBookings();
    }

    public boolean bookRoom(String roomNumber, String customerId, LocalDate checkIn, LocalDate checkOut) {
        if (checkOut.isBefore(checkIn)) {
            return false;
        }

        // Check if room exists and is available
        Room roomToBook = null;
        for (Room r : roomService.getAllRooms()) {
            if (r.getRoomNumber().equalsIgnoreCase(roomNumber) && r.isAvailable()) {
                roomToBook = r;
                break;
            }
        }

        if (roomToBook == null) {
            return false;
        }

        String bookingId = "BKG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Booking newBooking = new Booking(bookingId, roomNumber, customerId, checkIn, checkOut);
        
        getAllBookings().add(newBooking);
        
        // Mark room as unavailable
        roomService.updateAvailability(roomNumber, false);
        return true;
    }
}
