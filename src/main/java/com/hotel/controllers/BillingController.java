package com.hotel.controllers;

import com.hotel.models.Bill;
import com.hotel.models.Pair;
import com.hotel.models.AbstractRoom;
import com.hotel.models.RoomType;
import com.hotel.repository.DataStore;
import com.hotel.services.BillingService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BillingController {

    @FXML private Label totalBillsLbl;
    @FXML private Label totalRevLbl;
    @FXML private Label thisMonthRevLbl;
    @FXML private Label avgStayLbl;

    @FXML private ProgressBar progStandard;
    @FXML private ProgressBar progDeluxe;
    @FXML private ProgressBar progSuite;
    @FXML private Label lblPctStandard;
    @FXML private Label lblPctDeluxe;
    @FXML private Label lblPctSuite;

    @FXML private TableView<Pair<String, Double>> recentGuestsTable;
    @FXML private TableColumn<Pair<String, Double>, String> colGuestId;
    @FXML private TableColumn<Pair<String, Double>, String> colGuestRev;

    @FXML private BarChart<String, Number> monthlyRevChart;

    @FXML private TableView<Bill> billTable;
    @FXML private TableColumn<Bill, String> colBillId;
    @FXML private TableColumn<Bill, String> colBookingId;
    @FXML private TableColumn<Bill, String> colCustomerName;
    @FXML private TableColumn<Bill, String> colRoomNumber;
    @FXML private TableColumn<Bill, String> colAmount;
    @FXML private TableColumn<Bill, String> colDate; 

    private BillingService billingService = new BillingService();

    @FXML
    public void initialize() {
        if(colBillId != null) colBillId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBillId()));
        if(colBookingId != null) colBookingId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBookingId()));
        if(colCustomerName != null) colCustomerName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCustomerName()));
        if(colRoomNumber != null) colRoomNumber.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRoomNumber()));
        if(colAmount != null) colAmount.setCellValueFactory(data -> new SimpleStringProperty(String.format("₹%.2f", data.getValue().getTotalAmount())));
        if(colDate != null) colDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBillDate().toString()));

        if (colGuestId != null) colGuestId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKey()));
        if (colGuestRev != null) colGuestRev.setCellValueFactory(data -> new SimpleStringProperty(String.format("₹%.2f", data.getValue().getValue())));

        loadBills();
    }

    private void loadBills() {
        List<Bill> bills = billingService.getAllBills();
        if (billTable != null) {
            billTable.setItems(FXCollections.observableArrayList(bills));
        }
        updateAnalytics(bills);
    }

    private void updateAnalytics(List<Bill> bills) {
        if (bills == null || bills.isEmpty()) return;

        double totalRevs = bills.stream().mapToDouble(Bill::getTotalAmount).sum();
        double monthRevs = bills.stream()
                .filter(b -> b.getBillDate().getMonth() == LocalDate.now().getMonth() && b.getBillDate().getYear() == LocalDate.now().getYear())
                .mapToDouble(Bill::getTotalAmount).sum();
        double avgStay = bills.stream().mapToLong(Bill::getStayDuration).average().orElse(0.0);

        if (totalBillsLbl != null) totalBillsLbl.setText(String.valueOf(bills.size()));
        if (totalRevLbl != null) totalRevLbl.setText(String.format("₹%.2f", totalRevs));
        if (thisMonthRevLbl != null) thisMonthRevLbl.setText(String.format("₹%.2f", monthRevs));
        if (avgStayLbl != null) avgStayLbl.setText(String.format("%.1f days", avgStay));

        if (progStandard != null) {
            List<AbstractRoom> rooms = DataStore.getContainer().getRooms();
            double stdRev = 0, delRev = 0, suiteRev = 0;

            for (Bill b : bills) {
                AbstractRoom matched = rooms.stream().filter(r -> r.getRoomNumber().equalsIgnoreCase(b.getRoomNumber())).findFirst().orElse(null);
                if (matched != null) {
                    if (matched.getRoomType() == RoomType.STANDARD) stdRev += b.getTotalAmount();
                    else if (matched.getRoomType() == RoomType.DELUXE) delRev += b.getTotalAmount();
                    else if (matched.getRoomType() == RoomType.SUITE) suiteRev += b.getTotalAmount();
                }
            }

            if (totalRevs > 0) {
                progStandard.setProgress(stdRev / totalRevs);
                progDeluxe.setProgress(delRev / totalRevs);
                progSuite.setProgress(suiteRev / totalRevs);
                
                lblPctStandard.setText(String.format("%.1f%%", (stdRev / totalRevs) * 100));
                lblPctDeluxe.setText(String.format("%.1f%%", (delRev / totalRevs) * 100));
                lblPctSuite.setText(String.format("%.1f%%", (suiteRev / totalRevs) * 100));
            }
        }

        if (recentGuestsTable != null) {
            int size = bills.size();
            List<Bill> recent = bills.subList(Math.max(0, size - 5), size);
            List<Pair<String, Double>> guestPairs = recent.stream().map(b -> new Pair<>(b.getCustomerName(), b.getTotalAmount())).collect(Collectors.toList());
            recentGuestsTable.setItems(FXCollections.observableArrayList(guestPairs));
        }

        if (monthlyRevChart != null) {
            monthlyRevChart.getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Revenue");
            Map<Integer, Double> monthly = bills.stream()
                .filter(b -> b.getBillDate().getYear() == LocalDate.now().getYear())
                .collect(Collectors.groupingBy(b -> b.getBillDate().getMonthValue(), Collectors.summingDouble(Bill::getTotalAmount)));
            
            String[] monthNames = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
            for (int i=1; i<=12; i++) {
                series.getData().add(new XYChart.Data<>(monthNames[i-1], monthly.getOrDefault(i, 0.0)));
            }
            monthlyRevChart.getData().add(series);
        }
    }
}
