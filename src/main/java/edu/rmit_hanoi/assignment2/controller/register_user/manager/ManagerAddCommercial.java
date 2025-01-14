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

public class ManagerAddCommercial implements Initializable {
    private static int managerID;

    public TextField addressTextField;
    public ChoiceBox statusChoiceBox;
    public TextField feeTextField;
    public ChoiceBox periodChoiceBox;
    public TextField buisnessTypeTextField;
    public TextField parkSlotTextField;
    public TextField areaTextField;
    public TextField hostId;
    public TextField ownerId;
    public ImageView CommercialImageView;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] statusList = {"Available", "Unavailable", "Under Maintenance"};
        String[] periodList = {"Daily", "Weekly", "Monthly", "Yearly"};

        statusChoiceBox.getItems().addAll(statusList);
        periodChoiceBox.getItems().addAll(periodList);
    }

    public void toAddCommerical(ActionEvent event, int managerId) throws IOException {
        managerID = managerId;
        drawAddCommercialScene(event);
    }

    public void drawAddCommercialScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/manager/ManagerAddCommercialProp.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setTitle("Lux Property Management System");

        stage.setScene(scene);

        stage.show();
    }

    public void toAgreement(ActionEvent actionEvent) throws IOException {
        ManagerManageAgreement managerManageAgreement = new ManagerManageAgreement();
        managerManageAgreement.toManageAgreement(actionEvent, managerID);
    }

    public void switchToLoginMenu(ActionEvent actionEvent) throws IOException {
        LoginController loginController = new LoginController();
        loginController.drawLoginScene(actionEvent);
    }

    public void toPayment(ActionEvent actionEvent) throws IOException {
        ManagerManagePayment managePayment = new ManagerManagePayment();
        managePayment.toManagePayment(actionEvent, managerID);

    }

    public void toManageUser(ActionEvent actionEvent) throws IOException{
        ManagerManageUser manageUser = new ManagerManageUser();
        manageUser.toManageUser(actionEvent, managerID);
    }

    public void toStatistic(ActionEvent actionEvent) throws IOException {
        ManagerStatistic managerStatistic = new ManagerStatistic();
        managerStatistic.toManagerStatistic(actionEvent, managerID);
    }

    public void toManageCommercial(ActionEvent actionEvent) throws IOException{
        ManagerManageCommercial manageCommercial = new ManagerManageCommercial();
        manageCommercial.toManageCommercial(actionEvent, managerID);
    }

    public void toAddResident(ActionEvent actionEvent) throws IOException {
        ManagerAddResident managerAddResident = new ManagerAddResident();
        managerAddResident.toAddResident(actionEvent, managerID);
    }

    public void addCommercialProp(ActionEvent actionEvent) throws SQLException {

    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Add Property");
        alert.setHeaderText("Are you sure you want to add the property?");
        alert.setContentText("Press OK to confirm, or Cancel to return");
        if (alert.showAndWait().get() != ButtonType.OK) return;
        DatabaseConnector db = new DatabaseConnector();
        Statement statement = db.connect().createStatement();

        String query = "INSERT INTO public.property (owner_id, address, status, renting_fee, period)" +
                "VALUES (" + managerID + ", '" + addressTextField.getText() + "', '" +
                statusChoiceBox.getValue() + "', " +
                feeTextField.getText() + ", '" +
                periodChoiceBox.getValue() + "')";
        System.out.println(query);
        statement.executeUpdate(query);
        query = "SELECT property_id FROM public.property WHERE owner_id = " + managerID + " AND address = '" + addressTextField.getText() + "'";
        ResultSet propertyId = statement.executeQuery(query);
        propertyId.next();

        query = "INSERT INTO public.commercial_property (property_id, area, parking_num, business_type) " +
                "VALUES (" + propertyId.getInt("property_id") + ", " + areaTextField.getText() + ", " + parkSlotTextField.getText() + ", '" + buisnessTypeTextField.getText() + "')";
        System.out.println(query);
        statement.executeUpdate(query);
    }

    public void addCommercialImg(ActionEvent actionEvent) {
    }
}
