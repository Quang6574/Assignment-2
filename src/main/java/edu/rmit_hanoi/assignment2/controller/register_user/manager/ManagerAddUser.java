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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ManagerAddUser implements Initializable {
    private static int managerID;
    public TextField usernameTextField;
    public TextField passwordField;
    public TextField fullnameField;
    public DatePicker dobDatePicker;
    public TextField emailField;
    public TextField phoneNumTextField;
    public ChoiceBox<String> roleChoiceBox;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] userTypes = {"Tenant", "Owner", "Host"};
        roleChoiceBox.getItems().addAll(userTypes);
    }

    public void toAddUser(ActionEvent actionEvent, int managerId) throws IOException {
        managerID = managerId;
        drawAddUserScene(actionEvent);
    }

    public void drawAddUserScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/manager/ManagerCreateUser.fxml"));
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

    public void toPayment(ActionEvent actionEvent) throws IOException {
        ManagerManagePayment managerManagePayment = new ManagerManagePayment();
        managerManagePayment.toManagePayment(actionEvent, managerID);
    }

    public void toManageCommercial(ActionEvent actionEvent) throws IOException {
        ManagerManageCommercial managerManageCommercial = new ManagerManageCommercial();
        managerManageCommercial.toManageCommercial(actionEvent, managerID);
    }

    public void toStatistic(ActionEvent actionEvent) throws IOException {
        ManagerStatistic managerStatistic = new ManagerStatistic();
        managerStatistic.toManagerStatistic(actionEvent, managerID);
    }

    public void createUser(ActionEvent actionEvent) throws SQLException {
        DatabaseConnector db = new DatabaseConnector();
        Statement statement = db.connect().createStatement();
        if(phoneNumTextField.getText().matches("\\d+")) {
            String query = "INSERT INTO public.user (username, password, fullname, dob, email, phone_num, role) " +
                    "VALUES ('" + usernameTextField.getText() + "', '" +
                    passwordField.getText() + "', '" +
                    fullnameField.getText() + "', '" +
                    dobDatePicker.getValue() + "', '" +
                    emailField.getText() + "', '" +
                    phoneNumTextField.getText() + "', '" +
                    roleChoiceBox.getValue() + "')";
            statement.executeUpdate(query);
        }
    }
}
