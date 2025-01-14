package edu.rmit_hanoi.assignment2.controller.register_user.manager;
/**
 * @author Group 18
 */

import edu.rmit_hanoi.assignment2.Database.DatabaseConnector;
import edu.rmit_hanoi.assignment2.controller.LoginController;
import edu.rmit_hanoi.assignment2.model.property.ResidentProperty;
import javafx.beans.property.*;
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
import java.lang.ref.ReferenceQueue;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ManagerManageResident implements Initializable {
    private static int managerID;

    public TableView<ResidentProperty> filterResidentTable;
    public TableColumn<ResidentProperty, Integer> f_residentIdRow;
    public TableColumn<ResidentProperty, String> f_residentAddressRow;
    public TableColumn<ResidentProperty, Integer> f_residentBedRow;
    public TableColumn<ResidentProperty, Boolean> f_residentGardenRow;
    public TableColumn<ResidentProperty, Boolean> f_residentPetRow;
    public TableColumn<ResidentProperty, String> f_residentPeriodRow;
    public TableColumn<ResidentProperty, Double> f_residentFeeRow;
    public TableColumn<ResidentProperty, String> f_RsidentStatusrow;
    public TableColumn f_residentImage;
    public TextField addressTextBox;
    public ChoiceBox<String> statusChoiceBox;
    public TextField feeTextBox;
    public ChoiceBox<String> periodChoiceBox;
    public TextField bedroomTextBox;
    public CheckBox f_gardenCheckbox;
    public CheckBox f_petCheckbox;
    public ChoiceBox<String> f_periodChoiceBox;
    public CheckBox GardenCheckBox;
    public CheckBox PetFriendlyCheckBox;
    public TextField f_minBedField;
    public TextField f_maxBedField;
    public TextField f_minPriceBox;
    public TextField f_maxPriceBox;


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
                "JOIN public.residential_property rp ON p.property_id = rp.property_id";
        System.out.println("Executed query: " + query);
        ResultSet matchedProperties = null;
        try {
            matchedProperties = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<ResidentProperty> all_residents = FXCollections.observableArrayList();
        while (true) {
            try {
                if (!matchedProperties.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                all_residents.add(new ResidentProperty(
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

        f_residentIdRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPropId()).asObject());
        f_residentAddressRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        f_RsidentStatusrow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        f_residentBedRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBedroomNum()).asObject());
        f_residentGardenRow.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getGarden()).asObject());
        f_residentPetRow.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getPetFriendliness()).asObject());
        f_residentPeriodRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
        f_residentFeeRow.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getFee()).asObject());

        filterResidentTable.setItems(all_residents);
        filterResidentTable.refresh();
    }

    public void toManageResident(ActionEvent actionEvent, int managerId) throws IOException {
        managerID = managerId;
        drawManageResident(actionEvent);
    }

    public void drawManageResident(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/manager/ManagerManageResidentProp.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setTitle("Lux Property Management System");

        stage.setScene(scene);

        stage.show();
    }

    public void switchToLoginMenu(ActionEvent actionEvent) throws IOException {
        LoginController loginControl = new LoginController();
        loginControl.drawLoginScene(actionEvent);
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

    public void toManageCommercial(ActionEvent actionEvent) throws IOException {
        ManagerManageCommercial managerManageCommercial = new ManagerManageCommercial();
        managerManageCommercial.toManageCommercial(actionEvent, managerID);
    }

    public void toAgreementManage(ActionEvent actionEvent) throws IOException {
        ManagerManageAgreement managerManageAgreement = new ManagerManageAgreement();
        managerManageAgreement.toManageAgreement(actionEvent, managerID);
    }

    public void dispFilterResident(ActionEvent actionEvent) throws SQLException {
        DatabaseConnector db = new DatabaseConnector();
        Statement statement = db.connect().createStatement();

        String query = "SELECT * " +
                "FROM public.property p " +
                "JOIN public.residential_property rp ON p.property_id = rp.property_id " +
                "WHERE 1 = 1" +
                (f_minPriceBox.getText().isEmpty() ? "" : (" AND renting_fee >= " + f_minPriceBox.getText())) +
                (f_maxPriceBox.getText().isEmpty() ? "" : (" AND renting_fee <= " + f_maxPriceBox.getText())) +
                (f_minBedField.getText().isEmpty() ? "" : (" AND bedroom_num >= " + f_minBedField.getText())) +
                (f_maxBedField.getText().isEmpty() ? "" : (" AND bedroom_num <= " + f_maxBedField.getText())) +
                (f_periodChoiceBox.getValue() == null ? "" : (" AND period = '" + f_periodChoiceBox.getValue() + "'")) +
                (f_gardenCheckbox.isSelected() ? " AND garden = TRUE" : "") +
                (f_petCheckbox.isSelected() ? " AND pet_friendliness = TRUE" : "");
        System.out.println("Executed query: " + query);
        ResultSet matchedProperties = statement.executeQuery(query);

        ObservableList<ResidentProperty> f_resident = FXCollections.observableArrayList();
        while (matchedProperties.next()) {
            f_resident.add(new ResidentProperty(
                    matchedProperties.getInt("property_id"),
                    matchedProperties.getString("address"),
                    matchedProperties.getString("status"),
                    matchedProperties.getString("period"),
                    matchedProperties.getFloat("renting_fee"),
                    matchedProperties.getInt("bedroom_num"),
                    matchedProperties.getBoolean("garden"),
                    matchedProperties.getBoolean("pet_friendliness")
            ));
        }

        f_residentIdRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPropId()).asObject());
        f_residentAddressRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        f_residentBedRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBedroomNum()).asObject());
        f_residentGardenRow.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getGarden()).asObject());
        f_residentPetRow.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getPetFriendliness()).asObject());
        f_residentPeriodRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
        f_residentFeeRow.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getFee()).asObject());

        filterResidentTable.setItems(f_resident);
        filterResidentTable.refresh();
    }

    public void updateResidentProperty(ActionEvent actionEvent) throws SQLException {
        ResidentProperty selectedProperty = filterResidentTable.getSelectionModel().getSelectedItem();
        if (selectedProperty == null) return;

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


        query = "UPDATE public.residential_property " +
                "SET" + (bedroomTextBox.getText().isEmpty() ? "" : (" bedroom_num = " + bedroomTextBox.getText())) +
                (f_gardenCheckbox.isSelected() ? (", garden = 'TRUE'") : (", garden = 'FALSE'")) +
                (PetFriendlyCheckBox.isSelected() ? (", pet_friendliness = 'TRUE'") : (", pet_friendliness = 'FALSE'")) +
                " WHERE property_id = " + propertyId;
        System.out.println(query);
        statement.executeUpdate(query);

        query = "SELECT * " +
                "FROM public.property p " +
                "JOIN public.residential_property rp ON p.property_id = rp.property_id";
        System.out.println("Executed query: " + query);
        ResultSet matchedProperties = null;
        try {
            matchedProperties = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<ResidentProperty> all_residents = FXCollections.observableArrayList();
        while (true) {
            try {
                if (!matchedProperties.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                all_residents.add(new ResidentProperty(
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

        f_residentIdRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPropId()).asObject());
        f_residentAddressRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        f_RsidentStatusrow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        f_residentBedRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBedroomNum()).asObject());
        f_residentGardenRow.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getGarden()).asObject());
        f_residentPetRow.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getPetFriendliness()).asObject());
        f_residentPeriodRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
        f_residentFeeRow.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getFee()).asObject());

        filterResidentTable.setItems(all_residents);
        filterResidentTable.refresh();
    }

    public void toAddCommercial(ActionEvent actionEvent) throws IOException {
        ManagerAddCommercial managerAddCommercial = new ManagerAddCommercial();
        managerAddCommercial.toAddCommerical(actionEvent, managerID);
    }

    public void rowSelected(MouseEvent mouseEvent) {
        ResidentProperty selectedProperty = filterResidentTable.getSelectionModel().getSelectedItem();
        if (selectedProperty == null) return;
        try {
        propertyId = selectedProperty.getPropId();
        addressTextBox.setText(selectedProperty.getAddress());
        statusChoiceBox.setValue(selectedProperty.getStatus());
        periodChoiceBox.setValue(selectedProperty.getPeriod());
        feeTextBox.setText(String.valueOf(selectedProperty.getFee()));
        bedroomTextBox.setText(String.valueOf(selectedProperty.getBedroomNum()));
        GardenCheckBox.setSelected(selectedProperty.getGarden());
        PetFriendlyCheckBox.setSelected(selectedProperty.getPetFriendliness());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("e.printStackTrace()");
        }
    }

    private int propertyId;

    public void DeleteResidentProperty(ActionEvent actionEvent) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Proeprty");
        alert.setHeaderText("Are you sure you want to delete the property?");
        alert.setContentText("Press OK to confirm, or Cancel to return.");
        if (alert.showAndWait().get() != ButtonType.OK) return;

        DatabaseConnector db = new DatabaseConnector();
        Statement statement = db.connect().createStatement();

        String query = "DELETE FROM public.property WHERE property_id = " + propertyId;
        statement.executeUpdate(query);

        query = "SELECT * " +
                "FROM public.property p " +
                "JOIN public.residential_property rp ON p.property_id = rp.property_id";
        System.out.println("Executed query: " + query);
        ResultSet matchedProperties = null;
        try {
            matchedProperties = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<ResidentProperty> all_residents = FXCollections.observableArrayList();
        while (true) {
            try {
                if (!matchedProperties.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                all_residents.add(new ResidentProperty(
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

        f_residentIdRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPropId()).asObject());
        f_residentAddressRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        f_RsidentStatusrow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        f_residentBedRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBedroomNum()).asObject());
        f_residentGardenRow.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getGarden()).asObject());
        f_residentPetRow.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getPetFriendliness()).asObject());
        f_residentPeriodRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
        f_residentFeeRow.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getFee()).asObject());

        filterResidentTable.setItems(all_residents);
        filterResidentTable.refresh();
    }
}
