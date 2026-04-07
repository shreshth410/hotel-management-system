package com.hotel.services;

import com.hotel.models.AbstractRoom;
import com.hotel.models.StandardRoom;
import com.hotel.models.DeluxeRoom;
import com.hotel.models.SuiteRoom;
import com.hotel.repository.DataStore;

import java.util.Iterator;
import java.util.List;

public class RoomService {

    public List<AbstractRoom> getAllRooms() {
        return DataStore.getContainer().getRooms();
    }

    public boolean addRoom(String roomNumber, String type, double price) {
        Iterator<AbstractRoom> iterator = getAllRooms().iterator();
        while (iterator.hasNext()) {
            AbstractRoom r = iterator.next();
            if (r.getRoomNumber().equalsIgnoreCase(roomNumber)) {
                return false; // Already exists
            }
        }
        
        AbstractRoom newRoom;
        if (type.equalsIgnoreCase("Standard")) {
            newRoom = new StandardRoom(roomNumber, price, true);
        } else if (type.equalsIgnoreCase("Deluxe")) {
            newRoom = new DeluxeRoom(roomNumber, price, true);
        } else {
            newRoom = new SuiteRoom(roomNumber, price, true);
        }
        
        getAllRooms().add(newRoom);
        DataStore.saveData();
        return true;
    }

    public void updateRoom(AbstractRoom room, String newType, double newPrice) {
        // Type changing logic would go here
    }

    public void updateAvailability(String roomNumber, boolean available) {
        Iterator<AbstractRoom> iterator = getAllRooms().iterator();
        while (iterator.hasNext()) {
            AbstractRoom r = iterator.next();
            if (r.getRoomNumber().equalsIgnoreCase(roomNumber)) {
                r.setAvailable(available);
                DataStore.saveData();
                break;
            }
        }
    }
}
