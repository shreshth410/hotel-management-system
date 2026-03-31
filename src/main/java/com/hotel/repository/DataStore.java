package com.hotel.repository;

import java.io.*;

public class DataStore {
    private static final String FILE_NAME = "hotel_data.dat";
    private static DataContainer container = new DataContainer();

    static {
        loadData();
    }

    public static DataContainer getContainer() {
        return container;
    }

    public static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(container);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return; // keep container empty initially
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            container = (DataContainer) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
