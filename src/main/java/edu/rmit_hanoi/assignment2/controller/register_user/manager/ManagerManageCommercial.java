package edu.rmit_hanoi.assignment2.controller.register_user.manager;
/**
 * @author Group 18
 */

import edu.rmit_hanoi.assignment2.Database.DatabaseConnector;
import edu.rmit_hanoi.assignment2.controller.LoginController;
import edu.rmit_hanoi.assignment2.model.property.CommercialProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
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

public class ManagerManageCommercial implements Initializable {

    private static int managerID;

    public TableView<CommercialProperty> filterCommercialTable;
    public TableColumn<CommercialProperty, Integer> idRow;
    public TableColumn<CommercialProperty, String> addressRow;
    public TableColumn<CommercialProperty, String> businessTypeRow;
    public TableColumn<CommercialProperty, Integer> parkSlotRow;
    public TableColumn<CommercialProperty, Double> areaRow;
    public TableColumn<CommercialProperty, String> periodRow;
    public TableColumn<CommercialProperty, Double> priceRow;
    public TableColumn<CommercialProperty, String> f_CommercialStatusRow;
    public TableColumn f_commercialImage;
    public TextField f_priceMinBox;
    public TextField f_priceMaxBox;
    public TextField addressTextBox;
    public ChoiceBox<String> statusChoiceBox;
    public TextField feeTextBox;
    public ChoiceBox<String> periodChoiceBox;
    public TextField businessTypeChoiceBox;
    public TextField parkSlotTextBox;
    public TextField areaTextBox;
    public TextField f_businessTextBox;
    public TextField f_parkMinBox;
    public TextField f_parkMaxBox;
    public TextField f_areaMinBox;
    public TextField f_areaMaxBox;
    public ChoiceBox<String> f_periodChoiceBox;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] statusList = {"Available", "Unavailable", "Under Maintenance"};
        String[] periodList = {"Daily", "Weekly", "Monthly", "Yearly"};

        statusChoiceBox.getItems().addAll(statusList);
        periodChoiceBox.getItems().addAll(periodList);
        f_periodChoiceBox.getItems().addAll(periodList);

        DatabaseConnector db = new DatabaseConnector();
        Statement statement = null;
        try {
            statement = db.connect().createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String query = "SELECT * " +
                "FROM public.property p " +
                "JOIN public.commercial_property cp ON p.property_id = cp.property_id ";
        System.out.println("Executed query: " + query);
        ResultSet matchedProperties = null;
        try {
            matchedProperties = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<CommercialProperty> owned_commercials = FXCollections.observableArrayList();
        while (true) {
            try {
                if (!matchedProperties.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                owned_commercials.add(new CommercialProperty(
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
        idRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPropId()).asObject());
        addressRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        f_CommercialStatusRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        areaRow.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getArea()).asObject());
        parkSlotRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getParkingNum()).asObject());
        businessTypeRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBusinessType()));
        periodRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
        priceRow.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getFee()).asObject());
        filterCommercialTable.setItems(owned_commercials);
        filterCommercialTable.refresh();
    }

    public void toManageCommercial(ActionEvent actionEvent, int managerId) throws IOException {
        managerID = managerId;
        drawManageCommercial(actionEvent);
    }

    public void drawManageCommercial(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/manager/ManagerManageCommercialProp.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setTitle("Lux Property Management System");

        stage.setScene(scene);

        stage.show();
    }

    public void switchToLoginMenu(ActionEvent actionEvent) throws IOException {
        LoginController loginController = new LoginController();
        loginController.drawLoginScene(actionEvent);
    }

    public void toPayment(ActionEvent actionEvent) throws IOException {
        ManagerManagePayment managerManagePayment = new ManagerManagePayment();
        managerManagePayment.toManagePayment(actionEvent, managerID);
    }

    public void toManageUser(ActionEvent actionEvent) throws IOException {
        ManagerManageUser managerManageUser = new ManagerManageUser();
        managerManageUser.toManageUser(actionEvent, managerID);
    }

    public void toStatistic(ActionEvent actionEvent) throws IOException {
        ManagerStatistic managerStatistic = new ManagerStatistic();
        managerStatistic.toManagerStatistic(actionEvent, managerID);
    }

    public void dispFilterCommercial(ActionEvent actionEvent) throws SQLException {
        DatabaseConnector db = new DatabaseConnector();
        Statement statement = db.connect().createStatement();

        String query = "SELECT * " +
                "FROM public.property p " +
                "JOIN public.commercial_property cp ON p.property_id = cp.property_id " +
                "WHERE 1 = 1 " +
                (f_parkMinBox.getText().isEmpty() ? "" : (" AND parking_num >= '" + f_parkMinBox.getText() + "'")) +
                (f_parkMaxBox.getText().isEmpty() ? "" : (" AND parking_num <= '" + f_parkMaxBox.getText() + "'")) +
                (f_priceMinBox.getText().isEmpty() ? "" : (" AND renting_fee >= " + f_priceMaxBox.getText())) +
                (f_priceMaxBox.getText().isEmpty() ? "" : (" AND renting_fee <= " + f_priceMaxBox.getText())) +
                (f_areaMinBox.getText().isEmpty() ? "" : (" AND area >= " + f_areaMinBox.getText())) +
                (f_areaMaxBox.getText().isEmpty() ? "" : (" AND area <= " + f_areaMaxBox.getText())) +
                (f_periodChoiceBox.getValue() == null ? "" : (" AND period = '" + f_periodChoiceBox.getValue() + "'")) +
                (f_businessTextBox.getText().isEmpty() ? "" : (" AND business_type = '" + f_businessTextBox.getText() + "'"));
        System.out.println("Executed query: " + query);
        ResultSet matchedProperties = null;
        matchedProperties = statement.executeQuery(query);

        ObservableList<CommercialProperty> f_commercial = FXCollections.observableArrayList();
        while (true) {
            if (!matchedProperties.next()) break;
            f_commercial.add(new CommercialProperty(
                    matchedProperties.getInt("property_id"),
                    matchedProperties.getString("address"),
                    matchedProperties.getString("status"),
                    matchedProperties.getString("period"),
                    matchedProperties.getFloat("renting_fee"),
                    matchedProperties.getString("business_type"),
                    matchedProperties.getInt("parking_num"),
                    matchedProperties.getFloat("area")
            ));
        }
        idRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPropId()).asObject());
        addressRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        f_CommercialStatusRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        areaRow.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getArea()).asObject());
        parkSlotRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getParkingNum()).asObject());
        businessTypeRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBusinessType()));
        periodRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
        priceRow.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getFee()).asObject());

        filterCommercialTable.setItems(f_commercial);
        filterCommercialTable.refresh();
    }

    public void toAddCommercialProp(ActionEvent actionEvent) throws IOException {
        ManagerAddCommercial managerAddCommercial = new ManagerAddCommercial();
        managerAddCommercial.toAddCommerical(actionEvent, managerID);
    }

    public void toResidentManage(ActionEvent actionEvent) throws IOException {
        ManagerManageResident managerManageResident = new ManagerManageResident();
        managerManageResident.toManageResident(actionEvent, managerID);
    }

    public void rowSelected(MouseEvent event) {

        try {
            int selectedRow = filterCommercialTable.getSelectionModel().getSelectedIndex();
            CommercialProperty selectedProp = filterCommercialTable.getItems().get(selectedRow);

            propertyId = selectedProp.getPropId();
            addressTextBox.setText(selectedProp.getAddress());
            statusChoiceBox.setValue(selectedProp.getStatus());
            feeTextBox.setText(String.valueOf(selectedProp.getFee()));
            periodChoiceBox.setValue(selectedProp.getPeriod());
            businessTypeChoiceBox.setText(selectedProp.getBusinessType());
            parkSlotTextBox.setText(String.valueOf(selectedProp.getParkingNum()));
            areaTextBox.setText(String.valueOf(selectedProp.getArea()));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("e.printStackTrace()");
        }
    }

    private static int propertyId;

    public void updateProp(ActionEvent actionEvent) throws SQLException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Update Property");
        alert.setHeaderText("Are you sure you want to update the property?");
        alert.setContentText("Press OK to confirm, or Cancel to return.");
        if (alert.showAndWait().get() != ButtonType.OK) return;

        DatabaseConnector db = new DatabaseConnector();
        Statement statement = db.connect().createStatement();

        String query = "UPDATE public.property " +
                "SET" + (addressTextBox.getText().isEmpty() ? "" : (" address = '" + addressTextBox.getText() + "'")) +
                (statusChoiceBox.getValue() == null ? "" : (", status = '" + statusChoiceBox.getValue() + "'")) +
                (feeTextBox.getText().isEmpty() ? "" : (", renting_fee = " + feeTextBox.getText())) +
                (periodChoiceBox.getValue() == null ? "" : (", period = '" + periodChoiceBox.getValue() + "'")) +
                " WHERE property_id = " + propertyId;
        System.out.println(query);
        statement.executeUpdate(query);

        query = "UPDATE public.commercial_property " +
                "SET" + (areaTextBox.getText().isEmpty() ? "" : (" area = " + areaTextBox.getText())) +
                (parkSlotTextBox.getText().isEmpty() ? "" : (", parking_num = " + parkSlotTextBox.getText())) +
                (businessTypeChoiceBox.getText().isEmpty() ? "" : (", business_type = '" + businessTypeChoiceBox.getText() + "'")) +
                " WHERE property_id = " + propertyId;
        System.out.println(query);
        statement.executeUpdate(query);

        query = "SELECT * " +
                "FROM public.property p " +
                "JOIN public.commercial_property cp ON p.property_id = cp.property_id " +
                "WHERE 1 = 1 " +
                (f_parkMinBox.getText().isEmpty() ? "" : (" AND parking_num >= '" + f_parkMinBox.getText() + "'")) +
                (f_parkMaxBox.getText().isEmpty() ? "" : (" AND parking_num <= '" + f_parkMaxBox.getText() + "'")) +
                (f_priceMinBox.getText().isEmpty() ? "" : (" AND renting_fee >= " + f_priceMaxBox.getText())) +
                (f_priceMaxBox.getText().isEmpty() ? "" : (" AND renting_fee <= " + f_priceMaxBox.getText())) +
                (f_areaMinBox.getText().isEmpty() ? "" : (" AND area >= " + f_areaMinBox.getText())) +
                (f_areaMaxBox.getText().isEmpty() ? "" : (" AND area <= " + f_areaMaxBox.getText())) +
                (f_periodChoiceBox.getValue() == null ? "" : (" AND period = '" + f_periodChoiceBox.getValue() + "'")) +
                (f_businessTextBox.getText().isEmpty() ? "" : (" AND business_type = '" + f_businessTextBox.getText() + "'"));
        System.out.println("Executed query: " + query);
        ResultSet matchedProperties = null;
        matchedProperties = statement.executeQuery(query);

        ObservableList<CommercialProperty> f_commercial = FXCollections.observableArrayList();
        while (true) {
            if (!matchedProperties.next()) break;
            f_commercial.add(new CommercialProperty(
                    matchedProperties.getInt("property_id"),
                    matchedProperties.getString("address"),
                    matchedProperties.getString("status"),
                    matchedProperties.getString("period"),
                    matchedProperties.getFloat("renting_fee"),
                    matchedProperties.getString("business_type"),
                    matchedProperties.getInt("parking_num"),
                    matchedProperties.getFloat("area")
            ));
        }
        idRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPropId()).asObject());
        addressRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        f_CommercialStatusRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        areaRow.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getArea()).asObject());
        parkSlotRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getParkingNum()).asObject());
        businessTypeRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBusinessType()));
        periodRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
        priceRow.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getFee()).asObject());

        filterCommercialTable.setItems(f_commercial);
        filterCommercialTable.refresh();
    }

    public void deleteProp(ActionEvent actionEvent) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete property");
        alert.setHeaderText("Are you sure you want to delete the property?");
        alert.setContentText("Press OK to confirm, or Cancel to return.");
        if (alert.showAndWait().get() != ButtonType.OK) return;

        DatabaseConnector db = new DatabaseConnector();
        Statement statement = db.connect().createStatement();

        String query = "DELETE FROM public.property WHERE property_id = " + propertyId;
        statement.executeUpdate(query);

        query = "SELECT * " +
                "FROM public.property p " +
                "JOIN public.commercial_property cp ON p.property_id = cp.property_id " +
                "WHERE 1 = 1 " +
                (f_parkMinBox.getText().isEmpty() ? "" : (" AND parking_num >= '" + f_parkMinBox.getText() + "'")) +
                (f_parkMaxBox.getText().isEmpty() ? "" : (" AND parking_num <= '" + f_parkMaxBox.getText() + "'")) +
                (f_priceMinBox.getText().isEmpty() ? "" : (" AND renting_fee >= " + f_priceMaxBox.getText())) +
                (f_priceMaxBox.getText().isEmpty() ? "" : (" AND renting_fee <= " + f_priceMaxBox.getText())) +
                (f_areaMinBox.getText().isEmpty() ? "" : (" AND area >= " + f_areaMinBox.getText())) +
                (f_areaMaxBox.getText().isEmpty() ? "" : (" AND area <= " + f_areaMaxBox.getText())) +
                (f_periodChoiceBox.getValue() == null ? "" : (" AND period = '" + f_periodChoiceBox.getValue() + "'")) +
                (f_businessTextBox.getText().isEmpty() ? "" : (" AND business_type = '" + f_businessTextBox.getText() + "'"));
        System.out.println("Executed query: " + query);
        ResultSet matchedProperties = null;
        matchedProperties = statement.executeQuery(query);

        ObservableList<CommercialProperty> f_commercial = FXCollections.observableArrayList();
        while (true) {
            if (!matchedProperties.next()) break;
            f_commercial.add(new CommercialProperty(
                    matchedProperties.getInt("property_id"),
                    matchedProperties.getString("address"),
                    matchedProperties.getString("status"),
                    matchedProperties.getString("period"),
                    matchedProperties.getFloat("renting_fee"),
                    matchedProperties.getString("business_type"),
                    matchedProperties.getInt("parking_num"),
                    matchedProperties.getFloat("area")
            ));
        }
        idRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPropId()).asObject());
        addressRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        f_CommercialStatusRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        areaRow.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getArea()).asObject());
        parkSlotRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getParkingNum()).asObject());
        businessTypeRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBusinessType()));
        periodRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
        priceRow.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getFee()).asObject());

        filterCommercialTable.setItems(f_commercial);
        filterCommercialTable.refresh();
    }

    public void toManageResident(ActionEvent actionEvent) throws IOException {
        ManagerManageResident managerManageResident = new ManagerManageResident();
        managerManageResident.toManageResident(actionEvent, managerID);
    }
}
