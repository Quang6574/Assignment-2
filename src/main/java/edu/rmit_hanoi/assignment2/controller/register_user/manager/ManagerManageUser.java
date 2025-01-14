package edu.rmit_hanoi.assignment2.controller.register_user.manager;
/**
 * @author Group 18
 */

import edu.rmit_hanoi.assignment2.Database.DatabaseConnector;
import edu.rmit_hanoi.assignment2.controller.LoginController;
import edu.rmit_hanoi.assignment2.model.person.User;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.Date;
import java.util.ResourceBundle;

public class ManagerManageUser implements Initializable {
    private static int managerID;
    public TableColumn<User, Integer> idRow;
    public TableColumn<User, String> usernameRow;
    public TableColumn<User, String> passwordRow;
    public TableColumn<User, String> fullnameRow;
    public TableColumn<User, Date> dobRow;
    public TableColumn<User, String> emailRow;
    public TableColumn<User, String> phonenumRow;
    public TableColumn<User, String> roleRow;
    public TextField idTextField;
    public TextField usernameTextField;
    public TextField passwordField;
    public TextField fullnameField;
    public DatePicker dobDatePicker;
    public TextField emailField;
    public TextField phoneNumTextField;
    public ChoiceBox<String> roleChoiceBox;
    public TableView<User> allUserTable;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] userTypes = {"Tenant", "Owner", "Host"};
        roleChoiceBox.getItems().addAll(userTypes);

        DatabaseConnector db = new DatabaseConnector();
        Statement statement = null;
        try {
            statement = db.connect().createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String query = "SELECT * FROM public.user ";
        ResultSet users = null;
        try {
            users = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<User> all_user = FXCollections.observableArrayList();
        while (true) {
            try {
                if (!users.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                all_user.add(new User(
                        users.getInt("id"),
                        users.getString("username"),
                        users.getString("password"),
                        users.getString("fullname"),
                        users.getDate("dob"),
                        users.getString("phone_num"),
                        users.getString("email"),
                        users.getString("role")
                ));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        idRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        usernameRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        passwordRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPassword()));
        fullnameRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        dobRow.setCellValueFactory(cellData -> new SimpleObjectProperty<Date>(cellData.getValue().getDob()));
        phonenumRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhoneNum()));
        emailRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        roleRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole()));

        allUserTable.setItems(all_user);
        allUserTable.refresh();
    }

    public void toManageUser(ActionEvent actionEvent, int managerId) throws IOException {
        managerID = managerId;
        drawManageUser(actionEvent);
    }

    public void drawManageUser(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/manager/ManagerManageUser.fxml"));
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

    public void toStatistic(ActionEvent actionEvent) throws IOException {
        ManagerStatistic managerStatistic = new ManagerStatistic();
        managerStatistic.toManagerStatistic(actionEvent, managerID);
    }

    public void toManageCommercial(ActionEvent actionEvent) throws IOException {
        ManagerManageCommercial managerManageCommercial = new ManagerManageCommercial();
        managerManageCommercial.toManageCommercial(actionEvent, managerID);
    }

    public void deleteUser(ActionEvent actionEvent) throws SQLException {
        DatabaseConnector db = new DatabaseConnector();
        Statement statement = db.connect().createStatement();
        String query = "DELETE FROM public.user WHERE id = " + userId;
        statement.executeUpdate(query);

        query = "SELECT * FROM public.user ";
        ResultSet users = null;
        try {
            users = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<User> all_user = FXCollections.observableArrayList();
        while (true) {
            try {
                if (!users.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                all_user.add(new User(
                        users.getInt("id"),
                        users.getString("username"),
                        users.getString("password"),
                        users.getString("fullname"),
                        users.getDate("dob"),
                        users.getString("phone_num"),
                        users.getString("email"),
                        users.getString("role")
                ));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        idRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        usernameRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        passwordRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPassword()));
        fullnameRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        dobRow.setCellValueFactory(cellData -> new SimpleObjectProperty<Date>(cellData.getValue().getDob()));
        emailRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        phonenumRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhoneNum()));
        roleRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole()));

        allUserTable.setItems(all_user);
        allUserTable.refresh();
    }

    public void rowSelected() {
        User selectedUser = allUserTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) return;

        try {
        userId = selectedUser.getId();
        idTextField.setText(String.valueOf(selectedUser.getId()));
        usernameTextField.setText(selectedUser.getUsername());
        passwordField.setText(selectedUser.getPassword());
        fullnameField.setText(selectedUser.getName());
        dobDatePicker.setPromptText(selectedUser.getDob().toString());
        emailField.setText(selectedUser.getEmail());
        phoneNumTextField.setText(String.valueOf(selectedUser.getPhoneNum()));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("IndexOutOfBoundsException occur when the user try to click on the table without data");
        }
    }

    public static int userId;

    public void updateUser(javafx.event.ActionEvent actionEvent) throws SQLException {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Mark Paid");
        alert.setHeaderText("Are you sure you want to update the user information?");
        alert.setContentText("Press OK to confirm, or Cancel to return");
        if (alert.showAndWait().get() != ButtonType.OK) return;
        DatabaseConnector db = new DatabaseConnector();
        Statement statement = db.connect().createStatement();

        if(phoneNumTextField.getText().matches("\\d+")) {
            String query = "UPDATE public.user SET username = '" + usernameTextField.getText() + "', " +
                    "password = '" + passwordField.getText() + "', " +
                    "fullname = '" + fullnameField.getText() + "', " +
                    "dob = '" + dobDatePicker.getValue() + "', " +
                    (emailField.getText() == null ? null : emailField.getText()) + "', '" +
                    "phone_num = '" + phoneNumTextField.getText() + "', " +
                    "role = '" + roleChoiceBox.getValue() + "' " +
                    "WHERE id = " + userId;
            statement.executeUpdate(query);

            query = "SELECT * FROM public.user ";
            ResultSet users = null;
            try {
                users = statement.executeQuery(query);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            ObservableList<User> all_user = FXCollections.observableArrayList();
            while (true) {
                try {
                    if (!users.next()) break;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    all_user.add(new User(
                            users.getInt("id"),
                            users.getString("username"),
                            users.getString("password"),
                            users.getString("fullname"),
                            users.getDate("dob"),
                            users.getString("phone_num"),
                            users.getString("email"),
                            users.getString("role")
                    ));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            idRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
            usernameRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
            passwordRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPassword()));
            fullnameRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
            dobRow.setCellValueFactory(cellData -> new SimpleObjectProperty<Date>(cellData.getValue().getDob()));
            emailRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
            phonenumRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhoneNum()));
            roleRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole()));

            allUserTable.setItems(all_user);
            allUserTable.refresh();
        }
    }

    public void toCreateUser(ActionEvent actionEvent) throws IOException {
        ManagerAddUser managerAddUser = new ManagerAddUser();
        managerAddUser.toAddUser(actionEvent, managerID);
    }
}
