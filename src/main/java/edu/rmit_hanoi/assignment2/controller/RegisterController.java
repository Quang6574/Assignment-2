package edu.rmit_hanoi.assignment2.controller;
/**
 * @author Group 18
 */

import edu.rmit_hanoi.assignment2.Database.DatabaseConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    public ChoiceBox<String> accountDropList;
    public TextField regUsernameBox;
    public TextField regPasswordBox;
    public TextField regRePasswordBox;
    public TextField regFirstnameBox;
    public TextField regLastnameBox;
    public DatePicker regDobBox;
    public TextField regPhonenumBox;
    public TextField regEmailBox;
    public CheckBox confirmCheckbox;
    public Hyperlink termsConditionHyperlink;
    public Button toLoginSceneButton;
    public Button signUpbutton;

    private final String[] userTypes = {"Tenant", "Owner", "Host"};

    public void toLoginScene(ActionEvent event) throws IOException, SQLException {
        LoginController loginController = new LoginController();
        loginController.drawLoginScene(event);
    }

    public void drawRegisterScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/register/Register.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Lux Property Management System");
        stage.show();
    }

    public void signUp(ActionEvent event) throws SQLException {

        DatabaseConnector db = new DatabaseConnector();
        Statement statement = db.connect().createStatement();

        // Step 3: Execute a query
        if(regPhonenumBox.getText().matches("\\d+")){
            String query = "INSERT INTO public.user (username, password, fullname, dob, phone_num, email, role)" +
                    "VALUES ('" + regUsernameBox.getText() + "', '" + regPasswordBox.getText() + "', '" +
                    regFirstnameBox.getText() + "', '" + regDobBox.getValue() + "', '" +
                    regPhonenumBox.getText() + "', '" +
                    (regEmailBox.getText() == null ? "NULL" : regEmailBox.getText()) + "', '" +
                    accountDropList.getValue() + "')";
            statement.executeUpdate(query);
            System.out.println("User registered successfully");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        accountDropList.getItems().addAll(userTypes);
    }
}
