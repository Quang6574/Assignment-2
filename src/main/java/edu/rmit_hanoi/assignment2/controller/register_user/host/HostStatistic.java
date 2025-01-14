package edu.rmit_hanoi.assignment2.controller.register_user.host;
/**
 * @author Group 18
 */

import edu.rmit_hanoi.assignment2.Database.DatabaseConnector;
import edu.rmit_hanoi.assignment2.controller.LoginController;
import edu.rmit_hanoi.assignment2.model.property.Property;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.ResourceBundle;

public class HostStatistic implements Initializable {

    private static int hostID;

    public TextField rentedPropNum;
    public TextField unrentedPropNum;
    public TextField propUnderMaintenanceNum;
    public TextField totalResidentProp;
    public TextField totalCommercialProp;
    public TextField propPotentialProfit;

    public TableColumn<Property, Integer> ownerIdRow;
    public TableColumn<Property, String> addressRow;
    public TableColumn<Property, String> statusRow;
    public TableColumn<Property, Double> feeRow;
    public TableColumn<Property, String> periodRow;
    public TableView<Property> managedPropertyTable;
    public DatePicker startDate;
    public DatePicker endDate;
    public TextField commission;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        DatabaseConnector db = new DatabaseConnector();
        Statement statement = null;

        try {
            statement = db.connect().createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String query = "SELECT COUNT(p.*) " +
                "FROM public.property p " +
                "JOIN public.manage_property mp ON p.property_id = mp.property_id " +
                "WHERE mp.host_id = " + hostID + " AND p.status = 'Unavailable'";
        ResultSet rentedProperties = null;
        try {
            rentedProperties = statement.executeQuery(query);
            rentedProperties.next();
            rentedPropNum.setText(rentedProperties.getString(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        query = "SELECT COUNT(p.*) " +
                "FROM public.property p " +
                "JOIN public.manage_property mp ON p.property_id = mp.property_id " +
                "WHERE mp.host_id = " + hostID + " AND p.status = 'Available'";
        ResultSet unrentProperty = null;
        try {
            unrentProperty = statement.executeQuery(query);
            unrentProperty.next();
            unrentedPropNum.setText(unrentProperty.getString(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        query = "SELECT COUNT(p.*) " +
                "FROM public.property p " +
                "JOIN public.manage_property mp ON p.property_id = mp.property_id " +
                "WHERE mp.host_id = " + hostID + " AND p.status = 'Under Maintenance'";
        ResultSet underMaintenance = null;
        try {
            underMaintenance = statement.executeQuery(query);
            underMaintenance.next();
            propUnderMaintenanceNum.setText(underMaintenance.getString(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        query = "SELECT COUNT(cp.*) " +
                "FROM public.commercial_property cp " +
                "JOIN public.manage_property mp ON cp.property_id = mp.property_id " +
                "WHERE mp.host_id = " + hostID;
        ResultSet commercialProperties = null;
        try {
            commercialProperties = statement.executeQuery(query);
            commercialProperties.next();
            totalCommercialProp.setText(commercialProperties.getString(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        query = "SELECT COUNT(rp.*) " +
                "FROM public.residential_property rp " +
                "JOIN public.manage_property mp ON rp.property_id = mp.property_id " +
                "WHERE mp.host_id = " + hostID;
        ResultSet residentProperties = null;
        try {
            residentProperties = statement.executeQuery(query);
            residentProperties.next();
            totalResidentProp.setText(residentProperties.getString(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        query = "SELECT p.* " +
                "FROM public.manage_property mp " +
                "JOIN public.property p ON mp.property_id = p.property_id " +
                "WHERE mp.host_id = " + hostID;
        ResultSet matchedProperties = null;
        try {
            matchedProperties = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ObservableList<Property> managed_properties = FXCollections.observableArrayList();
        while (true) {
            try {
                if (!matchedProperties.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                managed_properties.add(new Property(
                        matchedProperties.getInt("property_id"),
                        matchedProperties.getInt("owner_id"),
                        matchedProperties.getString("address"),
                        matchedProperties.getString("status"),
                        matchedProperties.getString("period"),
                        matchedProperties.getDouble("renting_fee")
                ));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        ownerIdRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPropId()).asObject());
        addressRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        statusRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        feeRow.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getFee()).asObject());
        periodRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
        managedPropertyTable.setItems(managed_properties);
        managedPropertyTable.refresh();
    }

    public void toStatistic(ActionEvent actionEvent, int hostId) throws IOException {
        hostID = hostId;
        drawStatistic(actionEvent);
    }

    public void drawStatistic(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/host/HostStatistic.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setTitle("Lux Property Management System");
        stage.setScene(scene);
        stage.show();
    }

    public void switchHostPersonalInfo(ActionEvent actionEvent) throws IOException {
        HostPersonInfo hostPersonInfo = new HostPersonInfo();
        hostPersonInfo.toPersonInfo(actionEvent, hostID);
    }

    public void switchToLoginMenu(ActionEvent actionEvent) throws IOException {
        LoginController loginController = new LoginController();
        loginController.drawLoginScene(actionEvent);
    }

    public void toUnpaidPayment(ActionEvent actionEvent) throws IOException {
        HostUnpaid hostUnpaid = new HostUnpaid();
        hostUnpaid.toUnpaidPayment(actionEvent, hostID);
    }

    public void toManageCommercialProp(ActionEvent actionEvent) throws IOException {
        HostManageCommercial hostManageCommercial = new HostManageCommercial();
        hostManageCommercial.toManageCommercial(actionEvent, hostID);
    }

    public void toManageRentAgreement(ActionEvent actionEvent) throws IOException {
        HostUpdateRentAgreement hostUpdateRentAgreement = new HostUpdateRentAgreement();
        hostUpdateRentAgreement.toManageRentAgreement(actionEvent, hostID);
    }

    public void rowSelected(MouseEvent mouseEvent) throws SQLException {
        int selectedRow = managedPropertyTable.getSelectionModel().getSelectedIndex();
        Property selectedProp = managedPropertyTable.getItems().get(selectedRow);

        selectedPeriod = selectedProp.getPeriod();
        selectedRentingFee = selectedProp.getFee();
    }

    private static String selectedPeriod;
    private static double selectedRentingFee;

    public void calcPotentialProfit(ActionEvent actionEvent) throws SQLException {

        Date testStartDate = Date.from(startDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date testEndDate = Date.from(endDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        double testCommission = Double.parseDouble(commission.getText());

        LocalDate startLocalDate = testStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endLocalDate = testEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        long daysBetween = ChronoUnit.DAYS.between(startLocalDate, endLocalDate);
        testCommission /= 100;

        double potentialProfit = 0;
        switch(selectedPeriod){
            case "Daily":
                potentialProfit = selectedRentingFee * daysBetween * testCommission;
                break;
            case "Weekly":
                potentialProfit = selectedRentingFee * daysBetween / 7 * testCommission;
                break;
            case "Monthly":
                potentialProfit = selectedRentingFee * daysBetween / 30 * testCommission;
                break;
            case "Yearly":
                potentialProfit = selectedRentingFee * daysBetween / 365 * testCommission;
                break;
        }
        propPotentialProfit.setText(String.format("%.3f", potentialProfit));
    }
}
