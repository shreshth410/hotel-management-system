package com.hotel.repository;

import com.hotel.models.Bill;
import com.hotel.models.Booking;
import com.hotel.models.Customer;
import com.hotel.models.AbstractRoom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataContainer implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<AbstractRoom> rooms = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();
    private List<Bill> bills = new ArrayList<>();

    public List<AbstractRoom> getRooms() { return rooms; }
    public List<Customer> getCustomers() { return customers; }
    public List<Booking> getBookings() { return bookings; }
    public List<Bill> getBills() { return bills; }
}
