package edu.rmit_hanoi.assignment2.controller.register_user.owner;
/**
 * @author Group 18
 */

import edu.rmit_hanoi.assignment2.Database.DatabaseConnector;
import edu.rmit_hanoi.assignment2.controller.LoginController;
import edu.rmit_hanoi.assignment2.model.property.CommercialProperty;
import javafx.application.Platform;
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

public class OwnerUpdateCommercial implements Initializable {
    private static int ownerID;

    public void setOwnerID(int ownerId) {
        ownerID = ownerId;
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] statusList = {"Available", "Unavailable", "Under Maintenance"};
        String[] periodList = {"Daily", "Weekly", "Monthly", "Yearly"};

        OMPCStatusChoiceBox.getItems().addAll(statusList);
        OMPCPeriodChoiceBox.getItems().addAll(periodList);

        DatabaseConnector db = new DatabaseConnector();
        Statement statement = null;
        try {
            statement = db.connect().createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String query = "SELECT * " +
                "FROM public.property p " +
                "JOIN public.commercial_property cp ON p.property_id = cp.property_id " +
                "WHERE p.owner_id = " + ownerID;
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
        OMPCPropertyIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPropId()).asObject());
        OMPCPropertyAddressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        OMPCAreaColumn.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getArea()).asObject());
        OMPCParkingSpaceColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getParkingNum()).asObject());
        OMPCBusinessTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBusinessType()));
        OMPCPeriodColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
        OMPCRentingFeeColumn.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getFee()).asObject());

        OMPCTable.setItems(owned_commercials);
        OMPCTable.refresh();
    }

    public void drawUpdateCommercialScene(ActionEvent event) throws IOException, SQLException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/owner/OwnerManageCommercialProperty.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setTitle("Lux Property Management System");

        stage.setScene(scene);

        stage.show();

    }

    public void toOwnerAddCommercial(ActionEvent event) throws IOException, SQLException {
        OwnerAddCommercial addCommercialControl = new OwnerAddCommercial();
        addCommercialControl.setOwnerID(ownerID);
        addCommercialControl.drawAddCommercialScene(event);
    }

    public void toOwnerPersonInfo(ActionEvent event) throws IOException, SQLException {
        OwnerPersonInfo ownerControl = new OwnerPersonInfo();
        ownerControl.drawOwnerInfoScene(event);
    }

    public void switchToLoginMenu(ActionEvent event) throws IOException {
        LoginController loginControl = new LoginController();
        loginControl.drawLoginScene(event);
    }

    public void OMPRtoResidentTable(ActionEvent event) throws SQLException, IOException {
        OwnerUpdateResident updateResidentControl = new OwnerUpdateResident();
        updateResidentControl.setOwnerID(ownerID);
        updateResidentControl.drawUpdateResidentialScene(event);
    }

    public ChoiceBox<String> OMPCStatusChoiceBox;
    public ChoiceBox<String> OMPCPeriodChoiceBox;
    public TableColumn<CommercialProperty, Integer> OMPCPropertyIdColumn;
    public TableColumn<CommercialProperty, String> OMPCPropertyAddressColumn;
    public TableColumn<CommercialProperty, Float> OMPCRentingFeeColumn;
    public TableColumn<CommercialProperty, String> OMPCPeriodColumn;
    public TableColumn<CommercialProperty, String> OMPCBusinessTypeColumn;
    public TableColumn<CommercialProperty, Integer> OMPCParkingSpaceColumn;
    public TableColumn<CommercialProperty, Float> OMPCAreaColumn;
    public TableColumn<CommercialProperty, String> OMPCStatusColumn;

    //text field
    public TextField OMPCPropertyAddressTextBox;
    public TextField OMPCRentingFeeTextBox;
    public TextField OMPCBusinessTypeTextBox;
    public TextField OMPCParkingSpaceTextBox;
    public TextField OMPCAreaTextBox;

    public TableView<CommercialProperty> OMPCTable;

    public void rowClicked(MouseEvent event) {
        try {
            int selectedRow = OMPCTable.getSelectionModel().getSelectedIndex();
            CommercialProperty selectedProp = OMPCTable.getItems().get(selectedRow);
            propertyId = selectedProp.getPropId();

            OMPCPropertyAddressTextBox.setText(selectedProp.getAddress());
            OMPCStatusChoiceBox.setValue(selectedProp.getStatus());
            OMPCRentingFeeTextBox.setText(String.valueOf(selectedProp.getFee()));
            OMPCPeriodChoiceBox.setValue(selectedProp.getPeriod());
            OMPCBusinessTypeTextBox.setText(selectedProp.getBusinessType());
            OMPCParkingSpaceTextBox.setText(String.valueOf(selectedProp.getParkingNum()));
            OMPCAreaTextBox.setText(String.valueOf(selectedProp.getArea()));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("e.printStackTrace()");
        }
    }

    private static int propertyId;

    public void c_updateProp(ActionEvent event) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Update Property");
        alert.setHeaderText("Are you sure you want to update the property?");
        alert.setContentText("Press OK to confirm, or Cancel to return.");
        if (alert.showAndWait().get() != ButtonType.OK) return;
        
        DatabaseConnector db = new DatabaseConnector();
        Statement statement = db.connect().createStatement();

        String query = "UPDATE public.property " +
                "SET" + (OMPCPropertyAddressTextBox.getText().isEmpty() ? "" : (" address = '" + OMPCPropertyAddressTextBox.getText() + "'")) +
                (OMPCStatusChoiceBox.getValue() == null ? "" : (", status = '" + OMPCStatusChoiceBox.getValue() + "'")) +
                (OMPCRentingFeeTextBox.getText().isEmpty() ? "" : (", renting_fee = " + OMPCRentingFeeTextBox.getText())) +
                (OMPCPeriodChoiceBox.getValue() == null ? "" : (", period = '" + OMPCPeriodChoiceBox.getValue() + "'")) +
                " WHERE property_id = " + propertyId;
        System.out.println(query);
        statement.executeUpdate(query);

        query = "UPDATE public.commercial_property " +
                "SET" + (OMPCAreaTextBox.getText().isEmpty() ? "" : (" area = " + OMPCAreaTextBox.getText())) +
                (OMPCParkingSpaceTextBox.getText().isEmpty() ? "" : (", parking_num = " + OMPCParkingSpaceTextBox.getText())) +
                (OMPCBusinessTypeTextBox.getText().isEmpty() ? "" : (", business_type = '" + OMPCBusinessTypeTextBox.getText() + "'")) +
                " WHERE property_id = " + propertyId;
        System.out.println(query);
        statement.executeUpdate(query);

        query = "SELECT * " +
                "FROM public.property p " +
                "JOIN public.commercial_property cp ON p.property_id = cp.property_id " +
                "WHERE p.owner_id = " + ownerID;
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
        OMPCPropertyIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPropId()).asObject());
        OMPCPropertyAddressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        OMPCAreaColumn.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getArea()).asObject());
        OMPCParkingSpaceColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getParkingNum()).asObject());
        OMPCBusinessTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBusinessType()));
        OMPCPeriodColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
        OMPCRentingFeeColumn.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getFee()).asObject());

        OMPCTable.setItems(owned_commercials);
        OMPCTable.refresh();
    }
}
