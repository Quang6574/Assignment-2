package edu.rmit_hanoi.assignment2.controller.register_user.host;
/**
 * @author Group 18
 */

import edu.rmit_hanoi.assignment2.Database.DatabaseConnector;
import edu.rmit_hanoi.assignment2.controller.LoginController;
import edu.rmit_hanoi.assignment2.model.property.CommercialProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class HostManageCommercial implements Initializable {
    public static int hostID;

    public TableView<CommercialProperty> commercialTable;
    public TableColumn<CommercialProperty, Integer> IdColumn;
    public TableColumn<CommercialProperty, String> addressColumn;
    public TableColumn<CommercialProperty, String> businessTypeColumn;
    public TableColumn<CommercialProperty, Integer> parkSlotColumn;
    public TableColumn<CommercialProperty, Double> areaColumn;
    public TableColumn<CommercialProperty, String> periodColumn;
    public TableColumn<CommercialProperty, Double> priceColumn;
    public TableColumn<CommercialProperty, String> statusColumn;
    public TextField IdTextField;
    public TextField addressTextField;
    public TextField businessTypeTextField;
    public TextField parkSlotTextField;
    public TextField areaTextField;
    public TextField priceTextField;
    public ChoiceBox periodChoiceBox;
    public ChoiceBox statusChoiceBox;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] statusList = {"Available", "Unavailable", "Under Maintenance"};
        statusChoiceBox.getItems().addAll(statusList);

        DatabaseConnector db = new DatabaseConnector();
        Statement statement = null;
        try {
            statement = db.connect().createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String query = "SELECT p.*, cp.* " +
                "FROM public.manage_property mp " +
                "JOIN public.property p ON mp.property_id = p.property_id " +
                "JOIN public.commercial_property cp ON mp.property_id = cp.property_id " +
                "WHERE mp.host_id = " + hostID;
        ResultSet matchedProperties = null;
        try {
            matchedProperties = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ObservableList<CommercialProperty> managed_commercials = FXCollections.observableArrayList();
        while (true) {
            try {
                if (!matchedProperties.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                managed_commercials.add(new CommercialProperty(
                        matchedProperties.getInt("property_id"),
                        matchedProperties.getString("address"),
                        matchedProperties.getString("status"),
                        matchedProperties.getString("period"),
                        matchedProperties.getFloat("renting_fee"),
                        matchedProperties.getString("business_type"),
                        matchedProperties.getInt("parking_num"),
                        matchedProperties.getFloat("area")
                ));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        IdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPropId()).asObject());
        addressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        areaColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getArea()).asObject());
        parkSlotColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getParkingNum()).asObject());
        businessTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBusinessType()));
        periodColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
        priceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getFee()).asObject());
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        commercialTable.setItems(managed_commercials);
        commercialTable.refresh();
    }

    public void toManageCommercial(ActionEvent event, int hostId) throws IOException {
        hostID = hostId;
        drawManageCommercial(event);
    }

    public void drawManageCommercial(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/host/HostManageCommercialProperty.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setTitle("Lux Property Management System");
        stage.setScene(scene);
        stage.show();
    }

    public void toUnpaidPayment(ActionEvent actionEvent) throws IOException {
        HostUnpaid unpaidControl = new HostUnpaid();
        unpaidControl.toUnpaidPayment(actionEvent, hostID);
    }

    public void toMangeRentAgreement(ActionEvent actionEvent) throws IOException {
        HostUpdateRentAgreement updateRAControl = new HostUpdateRentAgreement();
        updateRAControl.toManageRentAgreement(actionEvent, hostID);
    }

    public void toStatistics(ActionEvent actionEvent) throws IOException {
        HostStatistic statisticsControl = new HostStatistic();
        statisticsControl.toStatistic(actionEvent, hostID);
    }

    public void toPersonInfo(ActionEvent actionEvent) throws IOException {
        HostPersonInfo personInfoControl = new HostPersonInfo();
        personInfoControl.toPersonInfo(actionEvent, hostID);
    }

    public void switchToLoginMenu(ActionEvent actionEvent) throws IOException {
        LoginController loginControl = new LoginController();
        loginControl.drawLoginScene(actionEvent);
    }

    public void rowSelected(MouseEvent mouseEvent) {

        try {
            int selectedRow = commercialTable.getSelectionModel().getSelectedIndex();
            CommercialProperty selectedProp = commercialTable.getItems().get(selectedRow);
            propertyId = selectedProp.getPropId();

            statusChoiceBox.setValue(selectedProp.getStatus());
            IdTextField.setText(String.valueOf(selectedProp.getPropId()));
            addressTextField.setText(selectedProp.getAddress());
            businessTypeTextField.setText(selectedProp.getBusinessType());
            parkSlotTextField.setText(String.valueOf(selectedProp.getParkingNum()));
            areaTextField.setText(String.valueOf(selectedProp.getArea()));
            priceTextField.setText(String.valueOf(selectedProp.getFee()));
            periodChoiceBox.setValue(selectedProp.getPeriod());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("e.printStackTrace()");
        }

    }

    private static int propertyId;

    public void toResidentTable(ActionEvent actionEvent) throws IOException {
        HostManageResident residentControl = new HostManageResident();
        residentControl.toManageResident(actionEvent, hostID);
    }

    public void updateProp(ActionEvent actionEvent) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Update Property");
        alert.setHeaderText("Are you sure you want to update the Property?");
        alert.setContentText("Press OK to confirm, or Cancel to return");
        if (alert.showAndWait().get() != ButtonType.OK) return;

        DatabaseConnector db = new DatabaseConnector();
        Statement statement = db.connect().createStatement();

        String query = "UPDATE public.property " +
                "SET status = '" + statusChoiceBox.getValue() + "' " +
                "WHERE property_id = " + propertyId;
        statement.executeUpdate(query);

        query = "SELECT p.*, cp.* " +
                "FROM public.manage_property mp " +
                "JOIN public.property p ON mp.property_id = p.property_id " +
                "JOIN public.commercial_property cp ON mp.property_id = cp.property_id " +
                "WHERE mp.host_id = " + hostID;
        ResultSet matchedProperties = null;
        try {
            matchedProperties = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ObservableList<CommercialProperty> managed_commercials = FXCollections.observableArrayList();
        while (true) {
            try {
                if (!matchedProperties.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                managed_commercials.add(new CommercialProperty(
                        matchedProperties.getInt("property_id"),
                        matchedProperties.getString("address"),
                        matchedProperties.getString("status"),
                        matchedProperties.getString("period"),
                        matchedProperties.getFloat("renting_fee"),
                        matchedProperties.getString("business_type"),
                        matchedProperties.getInt("parking_num"),
                        matchedProperties.getFloat("area")
                ));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        IdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPropId()).asObject());
        addressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        areaColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getArea()).asObject());
        parkSlotColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getParkingNum()).asObject());
        businessTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBusinessType()));
        periodColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
        priceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getFee()).asObject());
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        commercialTable.setItems(managed_commercials);
        commercialTable.refresh();
    }
}
