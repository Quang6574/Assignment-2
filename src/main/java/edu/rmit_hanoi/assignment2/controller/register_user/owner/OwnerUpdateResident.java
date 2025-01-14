package edu.rmit_hanoi.assignment2.controller.register_user.owner;
/**
 * @author Group 18
 */

import edu.rmit_hanoi.assignment2.Database.DatabaseConnector;
import edu.rmit_hanoi.assignment2.controller.LoginController;
import edu.rmit_hanoi.assignment2.model.property.ResidentProperty;
import javafx.beans.property.SimpleBooleanProperty;
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

public class OwnerUpdateResident implements Initializable {

    public ChoiceBox<String> OMPRStatusChoiceBox;
    public ChoiceBox<String> OMPRPeriodChoiceBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] statusList = {"Available", "Unavailable", "Under Maintenance"};
        String[] periodList = {"Daily", "Weekly", "Monthly", "Yearly"};

        OMPRStatusChoiceBox.getItems().addAll(statusList);
        OMPRPeriodChoiceBox.getItems().addAll(periodList);


        DatabaseConnector db = new DatabaseConnector();
        Statement statement = null;
        try {
            statement = db.connect().createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String query = "SELECT * " +
                "FROM public.property p " +
                "JOIN public.residential_property rp ON p.property_id = rp.property_id " +
                "WHERE p.owner_id = " + ownerID;
        System.out.println("Executed query: " + query);
        ResultSet matchedProperties = null;
        try {
            matchedProperties = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<ResidentProperty> owned_residents = FXCollections.observableArrayList();
        while (true) {
            try {
                if (!matchedProperties.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                owned_residents.add(new ResidentProperty(
                        matchedProperties.getInt("property_id"),
                        matchedProperties.getString("address"),
                        matchedProperties.getString("status"),
                        matchedProperties.getString("period"),
                        matchedProperties.getFloat("renting_fee"),
                        matchedProperties.getInt("bedroom_num"),
                        matchedProperties.getBoolean("garden"),
                        matchedProperties.getBoolean("pet_friendliness")
                ));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        OMPRPropertyIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPropId()).asObject());
        OMPRPropertyAddressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        OMPRStatusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        OMPRBedroomColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBedroomNum()).asObject());
        OMPRGardenColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getGarden()).asObject());
        OMPRPetFriendlinessColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getPetFriendliness()).asObject());
        OMPRPeriodColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
        OMPRentingFeeColumn.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getFee()).asObject());

        OMPRTable.setItems(owned_residents);
        OMPRTable.refresh();
    }

    private static int ownerID;

    public void setOwnerID(int ownerId) {
        ownerID = ownerId;
    }

    public void drawUpdateResidentialScene(ActionEvent event) throws IOException, SQLException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/owner/OwnerManageResidentProperty.fxml"));
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

    public void OMPRtoCommercialTable(ActionEvent actionEvent) throws SQLException, IOException {
        OwnerUpdateCommercial updateCommercialScene = new OwnerUpdateCommercial();
        updateCommercialScene.setOwnerID(ownerID);
        updateCommercialScene.drawUpdateCommercialScene(actionEvent);
    }

    public TableView<ResidentProperty> OMPRTable;
    public TableColumn<ResidentProperty, Integer> OMPRPropertyIdColumn;
    public TableColumn<ResidentProperty, String> OMPRPropertyAddressColumn;
    public TableColumn<ResidentProperty, String> OMPRStatusColumn;
    public TableColumn<ResidentProperty, Float> OMPRentingFeeColumn;
    public TableColumn<ResidentProperty, String> OMPRPeriodColumn;
    public TableColumn<ResidentProperty, Integer> OMPRBedroomColumn;
    public TableColumn<ResidentProperty, Boolean> OMPRGardenColumn;
    public TableColumn<ResidentProperty, Boolean> OMPRPetFriendlinessColumn;

    //text field
    public TextField OMPRPropertyAddressTextBox;
    public TextField OMPRRentingFeeTextBox;
    public TextField OMPRBedroomTextBox;
    public CheckBox OMPRGardenCheckBox;
    public CheckBox OMPRPetFriendlinessCheckBox;

    public void rowClicked(MouseEvent event) {
        try {
            int selectedRow = OMPRTable.getSelectionModel().getSelectedIndex();
            ResidentProperty selectedProp = OMPRTable.getItems().get(selectedRow);
            propertyId = selectedProp.getPropId();

            OMPRPropertyAddressTextBox.setText(selectedProp.getAddress());
            OMPRStatusChoiceBox.setValue(selectedProp.getStatus());
            OMPRRentingFeeTextBox.setText(String.valueOf(selectedProp.getFee()));
            OMPRPeriodChoiceBox.setValue(selectedProp.getPeriod());
            OMPRBedroomTextBox.setText(String.valueOf(selectedProp.getBedroomNum()));
            OMPRGardenCheckBox.setSelected(selectedProp.getGarden());
            OMPRPetFriendlinessCheckBox.setSelected(selectedProp.getPetFriendliness());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("e.printStackTrace()");
        }
    }

    public static int propertyId;

    public void r_updateProp(ActionEvent event) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Update Property");
        alert.setHeaderText("Are you sure you want to update the property?");
        alert.setContentText("Press OK to confirm, or Cancel to return.");
        if (alert.showAndWait().get() != ButtonType.OK) return;

        DatabaseConnector db = new DatabaseConnector();
        Statement statement = db.connect().createStatement();

        String query = "UPDATE public.property " +
                "SET" + (OMPRPropertyAddressTextBox.getText().isEmpty() ? "" : (" address = '" + OMPRPropertyAddressTextBox.getText() + "'")) +
                (OMPRStatusChoiceBox.getValue() == null ? "" : (", status = '" + OMPRStatusChoiceBox.getValue() + "'")) +
                (OMPRRentingFeeTextBox.getText().isEmpty() ? "" : (", renting_fee = " + OMPRRentingFeeTextBox.getText())) +
                (OMPRPeriodChoiceBox.getValue() == null ? "" : (", period = '" + OMPRPeriodChoiceBox.getValue() + "'")) +
                " WHERE property_id = " + propertyId;
        System.out.println(query);
        statement.executeUpdate(query);


        query = "UPDATE public.residential_property " +
                "SET" + (OMPRBedroomTextBox.getText().isEmpty() ? "" : (" bedroom_num = " + OMPRBedroomTextBox.getText())) +
                (OMPRGardenCheckBox.isSelected() ? (", garden = 'TRUE'") : (", garden = 'FALSE'")) +
                (OMPRPetFriendlinessCheckBox.isSelected() ? (", pet_friendliness = 'TRUE'") : (", pet_friendliness = 'FALSE'")) +
                " WHERE property_id = " + propertyId;
        System.out.println(query);
        statement.executeUpdate(query);

        query = "SELECT * " +
                "FROM public.property p " +
                "JOIN public.residential_property rp ON p.property_id = rp.property_id " +
                "WHERE p.owner_id = " + ownerID;
        System.out.println("Executed query: " + query);
        ResultSet matchedProperties = null;
        try {
            matchedProperties = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<ResidentProperty> owned_residents = FXCollections.observableArrayList();
        while (true) {
            try {
                if (!matchedProperties.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                owned_residents.add(new ResidentProperty(
                        matchedProperties.getInt("property_id"),
                        matchedProperties.getString("address"),
                        matchedProperties.getString("status"),
                        matchedProperties.getString("period"),
                        matchedProperties.getFloat("renting_fee"),
                        matchedProperties.getInt("bedroom_num"),
                        matchedProperties.getBoolean("garden"),
                        matchedProperties.getBoolean("pet_friendliness")
                ));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        OMPRPropertyIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPropId()).asObject());
        OMPRPropertyAddressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        OMPRStatusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        OMPRBedroomColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBedroomNum()).asObject());
        OMPRGardenColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getGarden()).asObject());
        OMPRPetFriendlinessColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getPetFriendliness()).asObject());
        OMPRPeriodColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
        OMPRentingFeeColumn.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getFee()).asObject());

        OMPRTable.setItems(owned_residents);
        OMPRTable.refresh();
    }
}
