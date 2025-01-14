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

public class OwnerPersonInfo implements Initializable {

    public void initialize(URL url, ResourceBundle resourceBundle) {

        DatabaseConnector db = new DatabaseConnector();
        Statement statement = null;
        try {
            statement = db.connect().createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String query = "SELECT * FROM public.user WHERE id = " + ownerID;
        ResultSet managerInformation = null;
        try {
            managerInformation = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            managerInformation.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            OPIdTextBox.setText(Integer.toString(managerInformation.getInt("id")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            OPIUsernameTextBox.setText(managerInformation.getString("username"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            OPIPasswordTextBox.setText(managerInformation.getString("password"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            OPIFullnameTextBox.setPromptText(managerInformation.getString("fullname"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            OPIDobTextBox.setPromptText(managerInformation.getString("dob"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            OPIEmailTextBox.setPromptText(managerInformation.getString("email"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            OPIPhoneNumTextBox.setPromptText(managerInformation.getString("phone_num"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            roleTextField.setPromptText(managerInformation.getString("role"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //owner id passed from login controller
    private static int ownerID;

    //personal info variable
    public TextField OPIdTextBox;
    public TextField OPIUsernameTextBox;
    public TextField OPIPasswordTextBox;
    public TextField OPIFullnameTextBox;
    public DatePicker OPIDobTextBox;
    public TextField OPIEmailTextBox;
    public TextField OPIPhoneNumTextBox;
    public TextField roleTextField;


    public void toOwnerScene(ActionEvent event, int ownerId) throws IOException, SQLException {
        ownerID = ownerId;
        drawOwnerInfoScene(event);
    }

    public void drawOwnerInfoScene(ActionEvent event) throws IOException, SQLException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/owner/OwnerPersonalInfo.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setTitle("Lux Property Management System");

        stage.setScene(scene);
        stage.show();
    }

    //switch to owner manage commercial property scene
    public void toOwnerManageCommercial(ActionEvent event) throws IOException, SQLException {
        OwnerUpdateCommercial manageCommercialControl = new OwnerUpdateCommercial();
        manageCommercialControl.setOwnerID(ownerID);
        manageCommercialControl.drawUpdateCommercialScene(event);
    }


    //logout
    public void switchToLoginMenu(ActionEvent event) throws IOException {
        LoginController loginControl = new LoginController();
        loginControl.drawLoginScene(event);
    }

    public void updateInformation(ActionEvent actionEvent) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Update Information");
        alert.setHeaderText("Are you sure you want to update your personal information?");
        alert.setContentText("Press OK to confirm, or Cancel to return.");
        if (alert.showAndWait().get() != ButtonType.OK) return;

        DatabaseConnector db = new DatabaseConnector();
        Statement statement = db.connect().createStatement();

        if(OPIPhoneNumTextBox.getText().matches("\\d+")) {
            String query = "UPDATE public.user " +
                    "SET id = " + ownerID +
                    (OPIFullnameTextBox.getText().isEmpty() ? "" : (", fullname = '" + OPIFullnameTextBox.getText() + "'")) +
                    (OPIDobTextBox.getValue() == null ? "" : (", dob = '" + OPIDobTextBox.getValue() + "'")) +
                    (OPIPhoneNumTextBox.getText().isEmpty() ? "" : (", phone_num = " + OPIPhoneNumTextBox.getText())) +
                    (OPIEmailTextBox.getText().isEmpty() ? "" : (", email = '" + OPIEmailTextBox.getText() + "'")) +
                    " WHERE id = " + ownerID;
            System.out.println(query);
            statement.executeUpdate(query);

            query = "SELECT * FROM public.user WHERE id = " + ownerID;
            ResultSet managerInformation = null;
            try {
                managerInformation = statement.executeQuery(query);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                managerInformation.next();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                OPIdTextBox.setText(Integer.toString(managerInformation.getInt("id")));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                OPIUsernameTextBox.setText(managerInformation.getString("username"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                OPIPasswordTextBox.setText(managerInformation.getString("password"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                OPIFullnameTextBox.setPromptText(managerInformation.getString("fullname"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                OPIDobTextBox.setPromptText(managerInformation.getString("dob"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                OPIEmailTextBox.setPromptText(managerInformation.getString("email"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                OPIPhoneNumTextBox.setPromptText(managerInformation.getString("phone_num"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                roleTextField.setPromptText(managerInformation.getString("role"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }    
    }

    public void toOwnerAddCommercial(ActionEvent event) throws IOException, SQLException {
        OwnerAddCommercial addCommercialControl = new OwnerAddCommercial();
        addCommercialControl.setOwnerID(ownerID);
        addCommercialControl.drawAddCommercialScene(event);
    }

}