package com.hotel.services;

import com.hotel.models.Customer;
import com.hotel.repository.DataStore;

import java.util.List;

public class CustomerService {

    public List<Customer> getAllCustomers() {
        return DataStore.getContainer().getCustomers();
    }

    public boolean addCustomer(String customerId, String name, String phone) {
        for (Customer c : getAllCustomers()) {
            if (c.getCustomerId().equalsIgnoreCase(customerId)) {
                return false;
            }
        }
        Customer customer = new Customer(customerId, name, phone);
        getAllCustomers().add(customer);
        DataStore.saveData();
        return true;
    }
}
