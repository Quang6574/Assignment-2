package edu.rmit_hanoi.assignment2.controller.register_user.host;
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

public class HostPersonInfo implements Initializable {

    public static int hostID;

    public TextField IdTextField;
    public TextField usernameTextField;
    public TextField passwordTextField;
    public TextField fullnameTextField;
    public TextField emailTextField;
    public TextField phoneNumTextField;
    public TextField roleTextField;
    public DatePicker dobChoiceBox;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        DatabaseConnector db = new DatabaseConnector();
        Statement statement = null;
        try {
            statement = db.connect().createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String query = "SELECT * FROM public.user WHERE id = " + hostID;
        ResultSet hostInformation;
        try {
            hostInformation = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            hostInformation.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            IdTextField.setText(Integer.toString(hostInformation.getInt("id")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            usernameTextField.setText(hostInformation.getString("username"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            passwordTextField.setText(hostInformation.getString("password"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            fullnameTextField.setPromptText(hostInformation.getString("fullname"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            dobChoiceBox.setPromptText(hostInformation.getString("dob"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            emailTextField.setPromptText(hostInformation.getString("email"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            phoneNumTextField.setPromptText(hostInformation.getString("phone_num"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            roleTextField.setPromptText(hostInformation.getString("role"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void toPersonInfo(ActionEvent event, int hostId) throws IOException {
        hostID = hostId;
        drawPersonInfo(event);
    }

    public void drawPersonInfo(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/host/HostPersonalInfo.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setTitle("Lux Property Management System");
        stage.setScene(scene);
        stage.show();

    }
    public void toManageCommercialProp(ActionEvent actionEvent) throws IOException {
        HostManageCommercial commercialControl = new HostManageCommercial();
        commercialControl.toManageCommercial(actionEvent, hostID);
    }

    public void toManageRentAgreement(ActionEvent actionEvent) throws IOException {
        HostUpdateRentAgreement updateRAControl = new HostUpdateRentAgreement();
        updateRAControl.toManageRentAgreement(actionEvent, hostID);
    }

    public void toStatistics(ActionEvent actionEvent) throws IOException {
        HostStatistic statisticsControl = new HostStatistic();
        statisticsControl.toStatistic(actionEvent, hostID);
    }

    public void switchToLoginMenu(ActionEvent actionEvent) throws IOException {
        LoginController loginControl = new LoginController();
        loginControl.drawLoginScene(actionEvent);
    }

    public void updateInformation(ActionEvent actionEvent) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Update Personal Information");
        alert.setHeaderText("Are you sure you want to update your personal information?");
        alert.setContentText("Press OK to confirm, or Cancel to return");
        if (alert.showAndWait().get() != ButtonType.OK) return;

        DatabaseConnector db = new DatabaseConnector();
        Statement statement = db.connect().createStatement();

        if(phoneNumTextField.getText().matches("\\d+")) {
            String query = "UPDATE public.user " +
                    "SET id = " + hostID +
                    (fullnameTextField.getText().isEmpty() ? "" : (", fullname = '" + fullnameTextField.getText() + "'")) +
                    (dobChoiceBox.getValue() == null ? "" : (",  dob = '" + dobChoiceBox.getValue() + "'")) +
                    (phoneNumTextField.getText().isEmpty() ? "" : (", phone_num = " + phoneNumTextField.getText())) +
                    (emailTextField.getText().isEmpty() ? "" : (", email = '" + emailTextField.getText() + "'")) +
                    " WHERE id = " + hostID;
            statement.executeUpdate(query);

            query = "SELECT * FROM public.user WHERE id = " + hostID;
            ResultSet hostInformation;
            try {
                hostInformation = statement.executeQuery(query);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                hostInformation.next();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                IdTextField.setText(Integer.toString(hostInformation.getInt("id")));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                usernameTextField.setText(hostInformation.getString("username"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                passwordTextField.setText(hostInformation.getString("password"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                fullnameTextField.setPromptText(hostInformation.getString("fullname"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                dobChoiceBox.setPromptText(hostInformation.getString("dob"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                emailTextField.setPromptText(hostInformation.getString("email"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                phoneNumTextField.setPromptText(hostInformation.getString("phone_num"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                roleTextField.setPromptText(hostInformation.getString("role"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }    
    }

    public void toUnpaidPayment(ActionEvent actionEvent) throws IOException {
        HostUnpaid unpaidControl = new HostUnpaid();
        unpaidControl.toUnpaidPayment(actionEvent, hostID);
    }
}
