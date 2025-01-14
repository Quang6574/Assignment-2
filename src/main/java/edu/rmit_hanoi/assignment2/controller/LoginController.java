package edu.rmit_hanoi.assignment2.controller;
/**
 * @author Group 18
 */

import edu.rmit_hanoi.assignment2.Database.DatabaseConnector;
import edu.rmit_hanoi.assignment2.controller.register_user.host.HostPersonInfo;
import edu.rmit_hanoi.assignment2.controller.register_user.manager.ManagerAddCommercial;
import edu.rmit_hanoi.assignment2.controller.register_user.owner.OwnerPersonInfo;
import edu.rmit_hanoi.assignment2.controller.register_user.tenant.TenantPersonInfo;
import edu.rmit_hanoi.assignment2.controller.visitor.VisitorController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;


public class LoginController {

    public int currUserId = 0;


    HostPersonInfo hostPersonInfoControll = new HostPersonInfo();//Person Info Host controller
    TenantPersonInfo tenantController = new TenantPersonInfo();//Persin Info Tenant controller
    VisitorController visitorController = new VisitorController();
    OwnerPersonInfo ownerController = new OwnerPersonInfo();
    ManagerAddCommercial managerController = new ManagerAddCommercial();

    public TextField usernameField;
    public TextField passwordField;
    public Button loginButton;
    public Button signupButton;

    public Button visitorButton;

    public void switchToVisitorScene(ActionEvent event) throws IOException {
        visitorController.switchToVisitorScene(event);
    }

    public void drawLoginScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/login/Login.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Lux Property Management System");
        stage.show();
    }

    public void switchToRegisterScene(ActionEvent event) throws IOException {
        RegisterController registerController = new RegisterController();
        registerController.drawRegisterScene(event);
    }

    public void logIn(ActionEvent event) throws SQLException, IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        DatabaseConnector db = new DatabaseConnector();
        Statement statement = db.connect().createStatement();

        String query = "SElECT * FROM public.user WHERE username = '" + usernameField.getText() + "' AND password = '" + passwordField.getText() + "'";
        System.out.println(query);
        ResultSet matchedUser = statement.executeQuery(query);
        if (!matchedUser.next()) {
            System.out.println("Invalid username or password");

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("");
            alert.setHeaderText("Incorrect Username or Password");
            alert.setContentText("Please try again");
            alert.showAndWait();

            return;
        }
        String account = matchedUser.getString(2);

        if (account == null) {
            System.out.println("Invalid username or password");

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("");
            alert.setHeaderText("Incorrect Username or Password");
            alert.setContentText("Please try again");
            alert.showAndWait();

            return;
        } else {
            currUserId = matchedUser.getInt("id");
            String role = matchedUser.getString("role");
            switch (role) {
                case "Visitor":
                    visitorController.switchToVisitorScene(event);
                    break;
                case "Tenant":
                    tenantController.toTenantPersonInfo(event, currUserId);
                    break;
                case "Host":
                    hostPersonInfoControll.toPersonInfo(event, currUserId);
                    break;
                case "Owner":
                    ownerController.toOwnerScene(event, currUserId);
                    break;
                case "Manager":
                    managerController.toAddCommerical(event, currUserId);
                    break;
                default:
                    System.out.println("Invalid role");
            }
        }
    }
}
