package com.hotel.services;

import com.hotel.models.Room;
import com.hotel.repository.DataStore;

import java.util.List;

public class RoomService {

    public List<Room> getAllRooms() {
        return DataStore.getContainer().getRooms();
    }

    public boolean addRoom(String roomNumber, String type, double price) {
        for (Room r : getAllRooms()) {
            if (r.getRoomNumber().equalsIgnoreCase(roomNumber)) {
                return false; // Already exists
            }
        }
        Room newRoom = new Room(roomNumber, type, price, true);
        getAllRooms().add(newRoom);
        DataStore.saveData();
        return true;
    }

    public void updateRoom(Room room, String newType, double newPrice) {
        room.setRoomType(newType);
        room.setPricePerNight(newPrice);
        DataStore.saveData();
    }

    public void updateAvailability(String roomNumber, boolean available) {
        for (Room r : getAllRooms()) {
            if (r.getRoomNumber().equalsIgnoreCase(roomNumber)) {
                r.setAvailable(available);
                DataStore.saveData();
                break;
            }
        }
    }
}
