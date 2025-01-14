package edu.rmit_hanoi.assignment2.controller.register_user.owner;
/**
 * @author Group 18
 */

import edu.rmit_hanoi.assignment2.Database.DatabaseConnector;
import edu.rmit_hanoi.assignment2.controller.LoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;


public class OwnerAddCommercial implements Initializable {
    private static int ownerID;

    public void setOwnerID(int ownerId) {
        ownerID = ownerId;
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] statusList = {"Available", "Unavailable", "Under Maintenance"};
        String[] periodList = {"Daily", "Weekly", "Monthly", "Yearly"};

        OAPCStatusChoiceBox.getItems().addAll(statusList);
        OAPCPeriodChoiceBox.getItems().addAll(periodList);
    }

    public void drawAddCommercialScene(ActionEvent event) throws IOException, SQLException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/owner/OwnerAddCommercialProperty.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setResizable(false);

        stage.setTitle("Lux Property Management System");
        stage.setScene(scene);

        stage.show();
    }

    public void toOwnerManageCommercial(ActionEvent event) throws IOException, SQLException {
        OwnerUpdateCommercial manageCommercialControl = new OwnerUpdateCommercial();
        manageCommercialControl.setOwnerID(ownerID);
        manageCommercialControl.drawUpdateCommercialScene(event);
    }

    public void toOwnerPersonInfo(ActionEvent event) throws IOException, SQLException {
        OwnerPersonInfo ownerControl = new OwnerPersonInfo();
        ownerControl.drawOwnerInfoScene(event);
    }

    public void switchToLoginMenu(ActionEvent event) throws IOException, SQLException {
        LoginController loginControl = new LoginController();
        loginControl.drawLoginScene(event);
    }

    public void OMPtoAddResident(ActionEvent event) throws IOException, SQLException {
        OwnerAddResident addResidentControl = new OwnerAddResident();
        addResidentControl.setOwnerID(ownerID);
        addResidentControl.drawAddResidentialScene(event);
    }

    public void addCommercialImg(ActionEvent actionEvent) {
    }

    public ChoiceBox<String> OAPCPeriodChoiceBox;
    public ChoiceBox<String> OAPCStatusChoiceBox;
    public TextField OAPCPropertyAddressTextBox;
    public TextField OAPCRentingFeeTextBox;
    public TextField OAPCBusinessTypeTextBox;
    public TextField OAPCParkingSpaceTextBox;
    public TextField OAPCAreaTextBox;

    public void addCommercialProp(ActionEvent event) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Add Property");
        alert.setHeaderText("Are you sure you want to add the property?");
        alert.setContentText("Press OK to confirm, or Cancel to return.");
        if (alert.showAndWait().get() != ButtonType.OK) return;

        DatabaseConnector db = new DatabaseConnector();
        Statement statement = db.connect().createStatement();

        String query = "INSERT INTO public.property (owner_id, address, status, renting_fee, period)" +
                "VALUES (" + ownerID + ", '" + OAPCPropertyAddressTextBox.getText() + "', '" +
                OAPCStatusChoiceBox.getValue() + "', " +
                OAPCRentingFeeTextBox.getText() + ", '" +
                OAPCPeriodChoiceBox.getValue() + "')";
        System.out.println(query);
        statement.executeUpdate(query);
        query = "SELECT property_id FROM public.property WHERE owner_id = " + ownerID + " AND address = '" + OAPCPropertyAddressTextBox.getText() + "'";
        ResultSet propertyId = statement.executeQuery(query);
        propertyId.next();

        query = "INSERT INTO public.commercial_property (property_id, area, parking_num, business_type) " +
                "VALUES (" + propertyId.getInt("property_id") + ", " + OAPCAreaTextBox.getText() + ", " + OAPCParkingSpaceTextBox.getText() + ", '" + OAPCBusinessTypeTextBox.getText() + "')";
        System.out.println(query);
        statement.executeUpdate(query);
    }
}
