package edu.rmit_hanoi.assignment2.controller.register_user.tenant;
/**
 * @author Group 18
 */

import edu.rmit_hanoi.assignment2.Database.DatabaseConnector;
import edu.rmit_hanoi.assignment2.controller.LoginController;
import edu.rmit_hanoi.assignment2.model.property.CommercialProperty;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class TenantRentResident implements Initializable {

    public static int tenantID;

    public VBox residentFilterOption;
    public TextField minBedField;
    public TextField maxBedField;
    public CheckBox gardenCheckbox;
    public CheckBox petCheckbox;
    public TextField priceMinTextBox;
    public TextField priceMaxTextBox;
    public TableView<ResidentProperty> residentTable;
    public TableColumn<ResidentProperty, Integer> idRow;
    public TableColumn<ResidentProperty, String> addressRow;
    public TableColumn<ResidentProperty, Integer> bedroomRow;
    public TableColumn<ResidentProperty, Boolean> gardenRow;
    public TableColumn<ResidentProperty, Boolean> petRow;
    public TableColumn<ResidentProperty, String> periodRow;
    public TableColumn<ResidentProperty, Float> priceRow;
    public TableColumn imageRow;
    public ChoiceBox<String> paymentMethodChoiceBox;
    public ChoiceBox<String> periodChoiceBox;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] paymentMethods = {"Cash", "Credit Card", "Banking"};
        String[] periods = {"Daily", "Weekly", "Monthly", "Yearly"};
        periodChoiceBox.getItems().addAll(periods);
        paymentMethodChoiceBox.getItems().addAll(paymentMethods);

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
                "WHERE status = 'Available'";
        System.out.println("Executed query: " + query);
        ResultSet matchedProperties = null;
        try {
            matchedProperties = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<ResidentProperty> f_resident = FXCollections.observableArrayList();
        while (true) {
            try {
                if (!matchedProperties.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
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
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        idRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPropId()).asObject());
        addressRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        bedroomRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBedroomNum()).asObject());
        gardenRow.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getGarden()).asObject());
        petRow.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getPetFriendliness()).asObject());
        periodRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
        priceRow.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getFee()).asObject());

        residentTable.setItems(f_resident);
        residentTable.refresh();
    }

    public void toBrowseResident(ActionEvent actionEvent, int tenantId) throws IOException {
        tenantID = tenantId;
        drawBrowseResident(actionEvent);

    }

    public void drawBrowseResident(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/tenant/TenantBrowseResidentProp.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setTitle("Lux Property Management System");

        stage.setScene(scene);

        stage.show();

    }

    public void switchPersonInfo(ActionEvent actionEvent) throws IOException {
        TenantPersonInfo tenantPersonInfo = new TenantPersonInfo();
        tenantPersonInfo.toTenantPersonInfo(actionEvent, tenantID);
    }

    public void switchToLoginMenu(ActionEvent actionEvent) throws IOException {
        LoginController loginController = new LoginController();
        loginController.drawLoginScene(actionEvent);
    }

    public void toBrowseCommercial(ActionEvent actionEvent) throws IOException {
        TenantRentCommercial tenantRentCommercial = new TenantRentCommercial();
        tenantRentCommercial.toBrowseCommercial(actionEvent, tenantID);
    }

    public void filterTable(ActionEvent actionEvent) throws SQLException {
        DatabaseConnector db = new DatabaseConnector();
        Statement statement = db.connect().createStatement();


        String query = "SELECT * " +
                "FROM public.property p " +
                "JOIN public.residential_property rp ON p.property_id = rp.property_id " +
                "WHERE status = 'Available'" +
                (priceMinTextBox.getText().isEmpty() ? "" : (" AND renting_fee >= " + priceMinTextBox.getText())) +
                (priceMaxTextBox.getText().isEmpty() ? "" : (" AND renting_fee <= " + priceMaxTextBox.getText())) +
                (minBedField.getText().isEmpty() ? "" : (" AND bedroom_num >= " + minBedField.getText())) +
                (maxBedField.getText().isEmpty() ? "" : (" AND bedroom_num <= " + maxBedField.getText())) +
                (periodChoiceBox.getValue() == null ? "" : (" AND period = '" + periodChoiceBox.getValue() + "'")) +
                (gardenCheckbox.isSelected() ? " AND garden = TRUE" : "") +
                (petCheckbox.isSelected() ? " AND pet_friendliness = TRUE" : "");
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

        idRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPropId()).asObject());
        addressRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        bedroomRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBedroomNum()).asObject());
        gardenRow.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getGarden()).asObject());
        petRow.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getPetFriendliness()).asObject());
        periodRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
        priceRow.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getFee()).asObject());

        residentTable.setItems(f_resident);
        residentTable.refresh();
    }

    public void rowClicked(MouseEvent mouseEvent) {
        try {
            int selectedRow = residentTable.getSelectionModel().getSelectedIndex();
            ResidentProperty selectedProperty = residentTable.getItems().get(selectedRow);
            propertyId = selectedProperty.getPropId();
        } catch (Exception e) {
            System.out.println("No row selected");
        }
    }

    private static int propertyId;

    public void rentProperty(ActionEvent actionEvent) throws SQLException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Rent Property");
        alert.setHeaderText("Are you sure you want to rent the property?");
        alert.setContentText("Press OK to confirm, or Cancel to return");
        if (alert.showAndWait().get() != ButtonType.OK) return;
        DatabaseConnector db = new DatabaseConnector();
        Statement statement = db.connect().createStatement();

        if(paymentMethodChoiceBox.getValue() != null){
            String query = "INSERT INTO public.rental_agreement (property_id, tenant_id, contract_date, status) " +
                    "VALUES (" + propertyId + ", " + tenantID + ", CURRENT_DATE, 'New')";
            statement.executeUpdate(query);
            query = "INSERT INTO public.payment (tenant_id, agreement_id, payment_method, amount, status) " +
                    "VALUES (" + tenantID + ", (SELECT agreement_id FROM public.rental_agreement WHERE property_id = " + propertyId + " AND tenant_id = " + tenantID + "), '" + paymentMethodChoiceBox.getValue() + "', (SELECT renting_fee FROM public.property WHERE property_id = " + propertyId + "), 'Unpaid')";
            System.out.println(query);
            statement.executeUpdate(query);
            query = "UPDATE public.property SET status = 'Unavailable' WHERE property_id = " + propertyId;
            statement.executeUpdate(query);

            query = "SELECT * " +
                    "FROM public.property p " +
                    "JOIN public.residential_property rp ON p.property_id = rp.property_id " +
                    "WHERE status = 'Available'" +
                    (priceMinTextBox.getText().isEmpty() ? "" : (" AND renting_fee >= " + priceMinTextBox.getText())) +
                    (priceMaxTextBox.getText().isEmpty() ? "" : (" AND renting_fee <= " + priceMaxTextBox.getText())) +
                    (minBedField.getText().isEmpty() ? "" : (" AND bedroom_num >= " + minBedField.getText())) +
                    (maxBedField.getText().isEmpty() ? "" : (" AND bedroom_num <= " + maxBedField.getText())) +
                    (periodChoiceBox.getValue() == null ? "" : (" AND period = '" + periodChoiceBox.getValue() + "'")) +
                    (gardenCheckbox.isSelected() ? " AND garden = TRUE" : "") +
                    (petCheckbox.isSelected() ? " AND pet_friendliness = TRUE" : "");
            System.out.println("Executed query: " + query);
            ResultSet matchedProperties = null;
            try {
                matchedProperties = statement.executeQuery(query);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            ObservableList<ResidentProperty> f_resident = FXCollections.observableArrayList();
            while (true) {
                try {
                    if (!matchedProperties.next()) break;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
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
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            idRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPropId()).asObject());
            addressRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
            bedroomRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBedroomNum()).asObject());
            gardenRow.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getGarden()).asObject());
            petRow.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getPetFriendliness()).asObject());
            periodRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
            priceRow.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getFee()).asObject());

            residentTable.setItems(f_resident);
            residentTable.refresh();
        }
    }

    public void toBrowseAgreement(ActionEvent actionEvent) throws IOException {
        TenantBrowseAgreement tenantBrowseAgreement = new TenantBrowseAgreement();
        tenantBrowseAgreement.switchToAgreement(actionEvent, tenantID);
    }
}
