package edu.rmit_hanoi.assignment2.controller;

import edu.rmit_hanoi.assignment2.model.database.Database;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.postgresql.util.PSQLException;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
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

    public void toLoginScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/login/Login.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void signUp(ActionEvent event) throws SQLException{

        String regEmail;
        if (regEmailBox.getText().trim().isEmpty()) {
            regEmail = "";
        } else {
            regEmail = regEmailBox.getText();
        }

        Database db = new Database();
        Statement statement = db.getConnection().createStatement();

        try {
            // Step 3: Execute a query
            String query = "DO $$\n" +
                    "DECLARE\n" +
                    "max_id INT;\n" +
                    "BEGIN\n" +
                    "SELECT COALESCE(MAX(id), 0) INTO max_id FROM public.user;\n" +
                    "INSERT INTO public.user (id, username, password, fullname, dob, phone_num, email, role)" +
                    "VALUES (max_id + 1, '" + regUsernameBox.getText() + "', '" + regPasswordBox.getText() + "', '" +
                    regFirstnameBox.getText() + "', '" + regDobBox.getValue() + "', '" +
                    regPhonenumBox.getText() + "', '" + regEmail + "', '" + accountDropList.getValue() + "');" +
                    "END $$;";

            statement.executeQuery(query);
            System.out.println("User registered successfully");
        } catch (PSQLException e) {
            System.out.println("No result was returned from the query.");
        }


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        accountDropList.getItems().addAll(userTypes);
    }
}
