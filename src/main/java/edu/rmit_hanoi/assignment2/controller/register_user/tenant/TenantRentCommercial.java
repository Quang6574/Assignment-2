package edu.rmit_hanoi.assignment2.controller.register_user.tenant;
/**
 * @author Group 18
 */

import edu.rmit_hanoi.assignment2.Database.DatabaseConnector;
import edu.rmit_hanoi.assignment2.controller.LoginController;
import edu.rmit_hanoi.assignment2.model.property.CommercialProperty;
import edu.rmit_hanoi.assignment2.model.rent_agreement.RentAgreement;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class TenantRentCommercial implements Initializable {
    public static int tenantID;

    public TextField businessTypeTextBox;
    public TextField parkMinBox;
    public TextField parkMaxBox;
    public TextField areaMinBox;
    public TextField areaMaxBox;
    public TextField priceMinTextBox;
    public TextField priceMaxTextBox;
    public TableView<CommercialProperty> commercialTable;
    public TableColumn<CommercialProperty, Integer> idRow;
    public TableColumn<CommercialProperty, String> addressRow;
    public TableColumn<CommercialProperty, String> businessTypeRow;
    public TableColumn<CommercialProperty, Integer> parkSlotRow;
    public TableColumn<CommercialProperty, Float> areaRow;
    public TableColumn<CommercialProperty, String> periodRow;
    public TableColumn<CommercialProperty, Float> priceRow;
    public TableColumn imageRow;
    public ChoiceBox<String> periodChoiceBox;
    public ChoiceBox<String> paymentMethodChoiceBox;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] periodList = {"Daily", "Weekly", "Monthly", "Yearly"};
        periodChoiceBox.getItems().addAll(periodList);
        String[] paymentMethodList = {"Cash", "Credit Card", "Banking"};
        paymentMethodChoiceBox.getItems().addAll(paymentMethodList);

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
                "WHERE status = 'Available'";
        System.out.println("Executed query: " + query);
        ResultSet matchedProperties = null;
        try {
            matchedProperties = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<CommercialProperty> f_commercial = FXCollections.observableArrayList();
        while (true) {
            try {
                if (!matchedProperties.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
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
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        idRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPropId()).asObject());
        addressRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        areaRow.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getArea()).asObject());
        parkSlotRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getParkingNum()).asObject());
        businessTypeRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBusinessType()));
        periodRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
        priceRow.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getFee()).asObject());

        commercialTable.setItems(f_commercial);
        commercialTable.refresh();
    }

    public void toBrowseCommercial(ActionEvent actionEvent, int tenantId) throws IOException {
        tenantID = tenantId;
        drawBrowseCommercial(actionEvent);
    }

    public void drawBrowseCommercial(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/tenant/TenantBrowseCommercialProp.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Lux Property Management System");
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

    public void toBrowseResident(ActionEvent actionEvent) throws IOException {
        TenantRentResident tenantRentResident = new TenantRentResident();
        tenantRentResident.toBrowseResident(actionEvent, tenantID);
    }

    public void filterTable(ActionEvent actionEvent) throws SQLException {
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
                "WHERE status = 'Available'" +
                (parkMinBox.getText().isEmpty() ? "" : (" AND parking_num >= '" + parkMinBox.getText() + "'")) +
                (parkMaxBox.getText().isEmpty() ? "" : (" AND parking_num <= '" + parkMaxBox.getText() + "'")) +
                (priceMinTextBox.getText().isEmpty() ? "" : (" AND renting_fee >= " + priceMinTextBox.getText())) +
                (priceMaxTextBox.getText().isEmpty() ? "" : (" AND renting_fee <= " + priceMaxTextBox.getText())) +
                (areaMinBox.getText().isEmpty() ? "" : (" AND area >= " + areaMinBox.getText())) +
                (areaMaxBox.getText().isEmpty() ? "" : (" AND area <= " + areaMaxBox.getText())) +
                (periodChoiceBox.getValue() == null ? "" : (" AND period = '" + periodChoiceBox.getValue() + "'")) +
                (businessTypeTextBox.getText().isEmpty() ? "" : (" AND business_type = '" + businessTypeTextBox.getText() + "'"));
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
        areaRow.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getArea()).asObject());
        parkSlotRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getParkingNum()).asObject());
        businessTypeRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBusinessType()));
        periodRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
        priceRow.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getFee()).asObject());

        commercialTable.setItems(f_commercial);
        commercialTable.refresh();
    }

    public void rowSelected(MouseEvent mouseEvent) {
        try {
            int selectedRow = commercialTable.getSelectionModel().getSelectedIndex();
            CommercialProperty selectedProperty = commercialTable.getItems().get(selectedRow);
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

        if (paymentMethodChoiceBox.getValue() != null) {
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
                    "JOIN public.commercial_property cp ON p.property_id = cp.property_id " +
                    "WHERE status = 'Available'" +
                    (parkMinBox.getText().isEmpty() ? "" : (" AND parking_num >= '" + parkMinBox.getText() + "'")) +
                    (parkMaxBox.getText().isEmpty() ? "" : (" AND parking_num <= '" + parkMaxBox.getText() + "'")) +
                    (priceMinTextBox.getText().isEmpty() ? "" : (" AND renting_fee >= " + priceMinTextBox.getText())) +
                    (priceMaxTextBox.getText().isEmpty() ? "" : (" AND renting_fee <= " + priceMaxTextBox.getText())) +
                    (areaMinBox.getText().isEmpty() ? "" : (" AND area >= " + areaMinBox.getText())) +
                    (areaMaxBox.getText().isEmpty() ? "" : (" AND area <= " + areaMaxBox.getText())) +
                    (periodChoiceBox.getValue() == null ? "" : (" AND period = '" + periodChoiceBox.getValue() + "'")) +
                    (businessTypeTextBox.getText().isEmpty() ? "" : (" AND business_type = '" + businessTypeTextBox.getText() + "'"));
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
            areaRow.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getArea()).asObject());
            parkSlotRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getParkingNum()).asObject());
            businessTypeRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBusinessType()));
            periodRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
            priceRow.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getFee()).asObject());

            commercialTable.setItems(f_commercial);
            commercialTable.refresh();
        }
    }

    public void toBrowseAgreement(ActionEvent actionEvent) throws IOException {
        TenantBrowseAgreement tenantBrowseAgreement = new TenantBrowseAgreement();
        tenantBrowseAgreement.switchToAgreement(actionEvent, tenantID);
    }
}
