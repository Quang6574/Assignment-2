package edu.rmit_hanoi.assignment2.controller.register_user.tenant;
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

public class TenantPersonInfo implements Initializable {
    public static int tenantID;

    public TextField idTextField;
    public TextField usernameTextField;
    public TextField passwordTextField;
    public TextField fullnameTextField;
    public TextField emailTextField;
    public TextField phoneNumTextField;
    public DatePicker dobDatePicker;
    public TextField RoleTextFIed;

    public void initialize(URL url, ResourceBundle resourceBundle) {

        DatabaseConnector db = new DatabaseConnector();
        Statement statement = null;
        try {
            statement = db.connect().createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String query = "SELECT * FROM public.user WHERE id = " + tenantID;
        ResultSet tenantInformation = null;
        try {
            tenantInformation = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            tenantInformation.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            idTextField.setText(Integer.toString(tenantInformation.getInt("id")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            usernameTextField.setText(tenantInformation.getString("username"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            passwordTextField.setText(tenantInformation.getString("password"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            fullnameTextField.setPromptText(tenantInformation.getString("fullname"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            dobDatePicker.setPromptText(tenantInformation.getString("dob"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            emailTextField.setPromptText(tenantInformation.getString("email"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            phoneNumTextField.setPromptText(tenantInformation.getString("phone_num"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            RoleTextFIed.setPromptText(tenantInformation.getString("role"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void toTenantPersonInfo(ActionEvent event, int tenantId) throws IOException {
        tenantID = tenantId;
        drawTenantPersonInfo(event);
    }

    public void drawTenantPersonInfo(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/tenant/TenantPersonalInfo.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setTitle("Lux Property Management System");

        stage.setScene(scene);

        stage.show();
    }

    public void toBrowseProperty(ActionEvent event) throws IOException {
        TenantRentCommercial tenantRentCommercial = new TenantRentCommercial();
        tenantRentCommercial.toBrowseCommercial(event, tenantID);
    }

    public void toBrowseAgreement(ActionEvent event) throws IOException {
        TenantBrowseAgreement tenantBrowseAgreement = new TenantBrowseAgreement();
        tenantBrowseAgreement.switchToAgreement(event, tenantID);
    }

    public void switchToLoginMenu(ActionEvent event) throws IOException {
        LoginController loginControl = new LoginController();
        loginControl.drawLoginScene(event);
    }

    public void updateInformation(ActionEvent event) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Update Personal Information");
        alert.setHeaderText("Are you sure you want to update your personal information?");
        alert.setContentText("Press OK to confirm, or Cancel to return");
        if (alert.showAndWait().get() != ButtonType.OK) return;
        DatabaseConnector db = new DatabaseConnector();
        Statement statement = db.connect().createStatement();

        if(phoneNumTextField.getText().matches("\\d+")) {
            String query = "UPDATE public.user " +
                    "SET id = " + tenantID +
                    (fullnameTextField.getText().isEmpty() ? "" : (", fullname = '" + fullnameTextField.getText() + "'")) +
                    (dobDatePicker.getValue() == null ? "" : (",  dob = '" + dobDatePicker.getValue() + "'")) +
                    (phoneNumTextField.getText().isEmpty() ? "" : (", phone_num = " + phoneNumTextField.getText())) +
                    (emailTextField.getText().isEmpty() ? "" : (", email = '" + emailTextField.getText() + "'")) +
                    " WHERE id = " + tenantID;
            statement.executeUpdate(query);

            query = "SELECT * FROM public.user WHERE id = " + tenantID;
            ResultSet tenantInformation = statement.executeQuery(query);
            tenantInformation.next();
            idTextField.setText(Integer.toString(tenantInformation.getInt("id")));
            usernameTextField.setText(tenantInformation.getString("username"));
            passwordTextField.setText(tenantInformation.getString("password"));
            fullnameTextField.setPromptText(tenantInformation.getString("fullname"));
            dobDatePicker.setPromptText(tenantInformation.getString("dob"));
            emailTextField.setPromptText(tenantInformation.getString("email"));
            phoneNumTextField.setPromptText(tenantInformation.getString("phone_num"));
            RoleTextFIed.setPromptText(tenantInformation.getString("role"));
        }
    }
}
