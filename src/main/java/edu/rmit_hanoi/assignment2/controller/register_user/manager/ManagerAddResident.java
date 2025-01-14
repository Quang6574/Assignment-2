package edu.rmit_hanoi.assignment2.controller.register_user.manager;
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

public class ManagerAddResident implements Initializable {
    private static int managerID;

    public TextField PropertyAddressTextBox;
    public ChoiceBox StatusChoiceBox;
    public TextField RentingFeeTextBox;
    public ChoiceBox PeriodChoiceBox;
    public TextField BedroomTextBox;
    public ImageView ResidentImageView;
    public TextField HostTextBox;
    public TextField OwnerTextBox;
    public CheckBox GardenCheckBox;
    public CheckBox petFriendlyCheckBox;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] statusList = {"Available", "Unavailable", "Under Maintenance"};
        String[] periodList = {"Daily", "Weekly", "Monthly", "Yearly"};

        StatusChoiceBox.getItems().addAll(statusList);
        PeriodChoiceBox.getItems().addAll(periodList);
    }

    public void toAddResident(ActionEvent actionEvent, int managerId) throws IOException {
        managerID = managerId;
        drawAddResidentialScene(actionEvent);
    }

    public void drawAddResidentialScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/manager/ManagerAddResidentProp.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setTitle("Lux Property Management System");

        stage.setScene(scene);

        stage.show();
    }

    public void toManageAgreement(ActionEvent actionEvent) throws IOException {
        ManagerManageAgreement managerManageAgreement = new ManagerManageAgreement();
        managerManageAgreement.toManageAgreement(actionEvent, managerID);
    }

    public void switchToLoginMenu(ActionEvent actionEvent) throws IOException {
        LoginController loginController = new LoginController();
        loginController.drawLoginScene(actionEvent);
    }

    public void toManagePayment(ActionEvent actionEvent) throws IOException {
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

    public void toAddCommercial(ActionEvent actionEvent) throws IOException {
        ManagerAddCommercial managerAddCommercial = new ManagerAddCommercial();
        managerAddCommercial.toAddCommerical(actionEvent, managerID);
    }

    public void createResidentProperty(ActionEvent actionEvent) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Add property");
        alert.setHeaderText("Are you sure you want to add the property?");
        alert.setContentText("Press OK to confirm, or Cancel to return.");
        if (alert.showAndWait().get() != ButtonType.OK) return;

        DatabaseConnector db = new DatabaseConnector();
        Statement statement = db.connect().createStatement();

        String query = "INSERT INTO public.property (owner_id, address, status, renting_fee, period)" +
                "VALUES (" + managerID + ", '" + PropertyAddressTextBox.getText() + "', '" +
                StatusChoiceBox.getValue() + "', " +
                RentingFeeTextBox.getText() + ", '" +
                PeriodChoiceBox.getValue() + "')";
        statement.executeUpdate(query);

        query = "SELECT property_id FROM public.property WHERE owner_id = " + managerID + " AND address = '" + PropertyAddressTextBox.getText() + "'";
        ResultSet propertyId = statement.executeQuery(query);
        propertyId.next();
        System.out.println(query);

        query = "INSERT INTO public.residential_property (property_id, bedroom_num, garden, pet_friendliness) " +
                "VALUES (" + propertyId.getInt("property_id") + ", " + BedroomTextBox.getText() + ", '" +
                (GardenCheckBox.isSelected() ? "TRUE" : "FALSE") + "', '" +
                (petFriendlyCheckBox.isSelected() ? "TRUE" : "FALSE") + "')";
        System.out.println(query);
        statement.executeUpdate(query);
    }

    public void addResidentImg(ActionEvent actionEvent) {
    }
}
