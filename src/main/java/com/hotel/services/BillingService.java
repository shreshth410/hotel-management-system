package com.hotel.services;

import com.hotel.models.Bill;
import com.hotel.models.Booking;
import com.hotel.models.Customer;
import com.hotel.models.AbstractRoom;
import com.hotel.repository.DataStore;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

public class BillingService {

    private RoomService roomService = new RoomService();
    private BookingService bookingService = new BookingService();
    private CustomerService customerService = new CustomerService();

    public List<Bill> getAllBills() {
        return DataStore.getContainer().getBills();
    }

    public Bill generateBill(Booking booking) {
        // Calculate nights
        long daysBetween = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
        if (daysBetween <= 0) {
            daysBetween = 1; // Minimum 1 night charge
        }

        // Get Room price
        double price = 0.0;
        for (AbstractRoom r : roomService.getAllRooms()) {
            if (r.getRoomNumber().equalsIgnoreCase(booking.getRoomNumber())) {
                price = r.getPricePerNight();
                break;
            }
        }

        double totalAmount = (daysBetween * price) + booking.getExtraCost();

        // Get Customer Name
        String cName = booking.getCustomerId();
        for (Customer c : customerService.getAllCustomers()) {
            if (c.getCustomerId().equalsIgnoreCase(booking.getCustomerId())) {
                cName = c.getName();
                break;
            }
        }

        String billId = "INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Bill bill = new Bill(billId, booking.getBookingId(), cName, booking.getRoomNumber(), totalAmount, LocalDate.now(), daysBetween);
        
        getAllBills().add(bill);

        // Cleanup: remove booking as it's completed, and free room
        bookingService.getAllBookings().remove(booking);
        roomService.updateAvailability(booking.getRoomNumber(), true);

        DataStore.saveData();

        return bill;
    }
}
