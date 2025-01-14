package edu.rmit_hanoi.assignment2.controller.register_user.host;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class HostManageResident implements Initializable {

    public static int hostID;

    public TextField IdTextField;
    public TextField addressTextField;
    public TextField bedroomTextField;
    public TextField priceTextField;
    public ChoiceBox periodChoiceBox;
    public ChoiceBox statusChoiceBox;

    public CheckBox gardenCheckBox;
    public CheckBox petCheckBox;

    public TableView<ResidentProperty> residentTable;
    public TableColumn<ResidentProperty, Integer> IdColumn;
    public TableColumn<ResidentProperty, String> addressColumn;
    public TableColumn<ResidentProperty, Integer> bedroomColumn;
    public TableColumn<ResidentProperty, Boolean> gardenColumn;
    public TableColumn<ResidentProperty, Boolean> petColumn;
    public TableColumn<ResidentProperty, String> periodColumn;
    public TableColumn<ResidentProperty, Double> priceColumn;
    public TableColumn<ResidentProperty, String> statusColumn;
    public Text hostNameText;

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

        String query = "SELECT p.*, rp.* " +
                "FROM public.manage_property mp " +
                "JOIN public.property p ON mp.property_id = p.property_id " +
                "JOIN public.residential_property rp ON mp.property_id = rp.property_id " +
                "WHERE mp.host_id = " + hostID;
        System.out.println("Executed query: " + query);
        ResultSet matchedProperties = null;
        try {
            matchedProperties = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<ResidentProperty> managed_residents = FXCollections.observableArrayList();
        while (true) {
            try {
                if (!matchedProperties.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                managed_residents.add(new ResidentProperty(
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

        IdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPropId()).asObject());
        addressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        bedroomColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBedroomNum()).asObject());
        gardenColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getGarden()).asObject());
        petColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getPetFriendliness()).asObject());
        periodColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
        priceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getFee()).asObject());

        residentTable.setItems(managed_residents);
        residentTable.refresh();
    }
    
    public void toManageResident(ActionEvent event, int hostId) throws IOException {
        hostID = hostId;
        drawManageResident(event);
    }

    public void drawManageResident(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/host/HostManageResidentProperty.fxml"));
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

    public void toCommercialTable(ActionEvent actionEvent) throws IOException {
        HostManageCommercial commercialControl = new HostManageCommercial();
        commercialControl.toManageCommercial(actionEvent, hostID);
    }

    public void rowSelected(MouseEvent mouseEvent) {
        try {
        int selectedRow = residentTable.getSelectionModel().getSelectedIndex();
        ResidentProperty selectedProp = residentTable.getItems().get(selectedRow);
        propertyId = selectedProp.getPropId();

        statusChoiceBox.setValue(selectedProp.getStatus());
        periodChoiceBox.setValue(selectedProp.getPeriod());
        IdTextField.setText(String.valueOf(selectedProp.getPropId()));
        addressTextField.setText(selectedProp.getAddress());
        bedroomTextField.setText(String.valueOf(selectedProp.getBedroomNum()));
        priceTextField.setText(String.valueOf(selectedProp.getFee()));
        gardenCheckBox.setSelected(selectedProp.getGarden());
        petCheckBox.setSelected(selectedProp.getPetFriendliness());
        } catch (IndexOutOfBoundsException e) {
                    System.out.println("e.printStackTrace()");
        }
    }
    
    private static int propertyId;

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

        query = "SELECT p.*, rp.* " +
                "FROM public.manage_property mp " +
                "JOIN public.property p ON mp.property_id = p.property_id " +
                "JOIN public.residential_property rp ON mp.property_id = rp.property_id " +
                "WHERE mp.host_id = " + hostID;
        System.out.println("Executed query: " + query);
        ResultSet matchedProperties = null;
        try {
            matchedProperties = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<ResidentProperty> managed_residents = FXCollections.observableArrayList();
        while (true) {
            try {
                if (!matchedProperties.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                managed_residents.add(new ResidentProperty(
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

        IdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPropId()).asObject());
        addressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        bedroomColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBedroomNum()).asObject());
        gardenColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getGarden()).asObject());
        petColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getPetFriendliness()).asObject());
        periodColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
        priceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getFee()).asObject());

        residentTable.setItems(managed_residents);
        residentTable.refresh();
    }
}
