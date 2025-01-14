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
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class OwnerAddResident implements Initializable {

    private static int ownerID;

    public void setOwnerID(int ownerId) {
        ownerID = ownerId;
    }

    public void initialize(URL location, ResourceBundle resources) {
        String[] statusList = {"Available", "Unavailable", "Under Maintenance"};
        String[] periodList = {"Daily", "Weekly", "Monthly", "Yearly"};

        OAPRStatusChoiceBox.getItems().addAll(statusList);
        OAPRPeriodChoiceBox.getItems().addAll(periodList);
    }

    public void drawAddResidentialScene(ActionEvent event) throws IOException, SQLException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/owner/OwnerAddResidentialProperty.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setTitle("Lux Property Management System");

        stage.setScene(scene);

        stage.show();
    }

    public ImageView OAPRImageView;
    public ChoiceBox<String> OAPRStatusChoiceBox;
    public ChoiceBox<String> OAPRPeriodChoiceBox;

    //
    public TextField OAPRPropertyAddressTextBox;
    public TextField OAPRRentingFeeTextBox;
    public TextField OAPRBedroomTextBox;
    //
    public CheckBox OAPRGardenCheckBox;
    public CheckBox OAPRPetFriendlinessCheckBox;


    //switch to owner manage commercial property scene
    public void toOwnerManageCommercial(ActionEvent event) throws IOException, SQLException {
        OwnerUpdateCommercial manageCommercialControl = new OwnerUpdateCommercial();
        manageCommercialControl.setOwnerID(ownerID);
        manageCommercialControl.drawUpdateCommercialScene(event);
    }

    //switch to owner manage resident property scene
    public void toOwnerPersonInfo(ActionEvent event) throws IOException, SQLException {
        OwnerPersonInfo ownerControl = new OwnerPersonInfo();
        ownerControl.drawOwnerInfoScene(event);
    }

    //logout
    public void switchToLoginMenu(ActionEvent event) throws IOException {
        LoginController loginControl = new LoginController();
        loginControl.drawLoginScene(event);
    }

    //switch to adding commercial property scene
    public void OMPtoAddCommercial(ActionEvent event) throws IOException, SQLException {
        OwnerAddCommercial addCommercialControl = new OwnerAddCommercial();
        addCommercialControl.setOwnerID(ownerID);
        addCommercialControl.drawAddCommercialScene(event);
    }

    public void OAPRhandleAddImagesAction(ActionEvent actionEvent) {

    }

    public void addResidentProp(ActionEvent event) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Add Property");
        alert.setHeaderText("Are you sure you want to add the property?");
        alert.setContentText("Press OK to confirm, or Cancel to return.");
        if (alert.showAndWait().get() != ButtonType.OK) return;

        DatabaseConnector db = new DatabaseConnector();
        Statement statement = db.connect().createStatement();

        String query = "INSERT INTO public.property (owner_id, address, status, renting_fee, period)" +
                "VALUES (" + ownerID + ", '" + OAPRPropertyAddressTextBox.getText() + "', '" +
                OAPRStatusChoiceBox.getValue() + "', " +
                OAPRRentingFeeTextBox.getText() + ", '" +
                OAPRPeriodChoiceBox.getValue() + "')";
        statement.executeUpdate(query);

        query = "SELECT property_id FROM public.property WHERE owner_id = " + ownerID + " AND address = '" + OAPRPropertyAddressTextBox.getText() + "'";
        ResultSet propertyId = statement.executeQuery(query);
        propertyId.next();
        System.out.println(query);

        query = "INSERT INTO public.residential_property (property_id, bedroom_num, garden, pet_friendliness) " +
                "VALUES (" + propertyId.getInt("property_id") + ", " + OAPRBedroomTextBox.getText() + ", '" +
                (OAPRGardenCheckBox.isSelected() ? "TRUE" : "FALSE") + "', '" +
                (OAPRPetFriendlinessCheckBox.isSelected() ? "TRUE" : "FALSE") + "')";
        System.out.println(query);
        statement.executeUpdate(query);
    }
}
