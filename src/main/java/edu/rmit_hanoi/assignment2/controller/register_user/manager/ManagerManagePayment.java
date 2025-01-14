package edu.rmit_hanoi.assignment2.controller.register_user.manager;
/**
 * @author Group 18
 */

import edu.rmit_hanoi.assignment2.Database.DatabaseConnector;
import edu.rmit_hanoi.assignment2.controller.LoginController;
import edu.rmit_hanoi.assignment2.model.payment.Payment;
import edu.rmit_hanoi.assignment2.model.property.CommercialProperty;
import edu.rmit_hanoi.assignment2.model.rent_agreement.RentAgreement;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.ResourceBundle;

public class ManagerManagePayment implements Initializable {
    private static int managerID;

    public TableColumn<Payment, Integer> rentAgreementIdColumn;
    public TableColumn<Payment, Integer> paymentIdColumn;
    public TableColumn<Payment, Integer> tenantIdColumn;
    public TableColumn<Payment, String> payMethodColumn;
    public TableView<Payment> PaymentTable;
    public TableColumn<Payment, Date> payDateColumn;
    public TableColumn<Payment, Double> rentPriceColumn;
    public TableColumn<Payment, String> statusColumn;
    public TextField amountTextBox;
    public ChoiceBox<String> statusChoiceBox;
    public DatePicker paymentDatePicker;
    public ChoiceBox<String> paymentMethodChoiceBox;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] paymentMethodList = {"Cash", "Banking", "Credit Card"};
        paymentMethodChoiceBox.getItems().addAll(paymentMethodList);
        String[] statusList = {"Paid", "Unpaid"};
        statusChoiceBox.getItems().addAll(statusList);

        DatabaseConnector db = new DatabaseConnector();
        Statement statement = null;
        try {
            statement = db.connect().createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String query = "SELECT * " +
                "FROM public.payment ";
        System.out.println("Executed query: " + query);
        ResultSet matchedPayments = null;
        try {
            matchedPayments = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<Payment> all_payment = FXCollections.observableArrayList();
        while (true) {
            try {
                if (!matchedPayments.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                all_payment.add(new Payment(
                        matchedPayments.getInt("payment_id"),
                        matchedPayments.getInt("tenant_id"),
                        matchedPayments.getInt("agreement_id"),
                        matchedPayments.getFloat("amount"),
                        matchedPayments.getDate("pay_date"),
                        matchedPayments.getString("payment_method"),
                        matchedPayments.getString("status")
                ));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        rentAgreementIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAgreementId()).asObject());
        paymentIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPaymentId()).asObject());
        tenantIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTenantId()).asObject());
        payMethodColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMethod()));
        payDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<Date>(cellData.getValue().getDate()));
        rentPriceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getAmount()).asObject());
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        PaymentTable.setItems(all_payment);
        PaymentTable.refresh();
    }

    public void toManagePayment(ActionEvent actionEvent, int managerId) throws IOException {
        managerID = managerId;
        drawManagePayment(actionEvent);
    }

    public void drawManagePayment(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/manager/ManagerManagePayment.fxml"));
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

    public void toManageUser(ActionEvent actionEvent) throws IOException {
        ManagerManageUser managerManageUser = new ManagerManageUser();
        managerManageUser.toManageUser(actionEvent, managerID);
    }

    public void toStatistic(ActionEvent actionEvent) throws IOException {
        ManagerStatistic managerStatistic = new ManagerStatistic();
        managerStatistic.toManagerStatistic(actionEvent, managerID);
    }

    public void toManageCommercial(ActionEvent actionEvent) throws IOException {
        ManagerManageCommercial managerMangeCommercial = new ManagerManageCommercial();
        managerMangeCommercial.toManageCommercial(actionEvent, managerID);
    }

    public void rowSelected(MouseEvent mouseEvent) {
        int selectedRow = PaymentTable.getSelectionModel().getSelectedIndex();
        Payment selectedPayment = PaymentTable.getItems().get(selectedRow);
        paymentId = selectedPayment.getPaymentId();
        amountTextBox.setText(String.valueOf(selectedPayment.getAmount()));
        statusChoiceBox.setValue(selectedPayment.getStatus());
        paymentDatePicker.setPromptText(selectedPayment.getDate() == null? "" : selectedPayment.getDate().toString());
        paymentMethodChoiceBox.setValue(selectedPayment.getMethod() == null? "" : selectedPayment.getMethod());
    }

    private static int paymentId;

    public void deletePayment(ActionEvent actionEvent) throws SQLException {
        DatabaseConnector db = new DatabaseConnector();
        Statement statement = db.connect().createStatement();

        String query = "DELETE FROM public.payment WHERE payment_id = " + paymentId;
        statement.executeUpdate(query);

        query = "SELECT * " +
                "FROM public.payment ";
        System.out.println("Executed query: " + query);
        ResultSet matchedPayments = null;
        try {
            matchedPayments = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<Payment> all_payment = FXCollections.observableArrayList();
        while (true) {
            try {
                if (!matchedPayments.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                all_payment.add(new Payment(
                        matchedPayments.getInt("payment_id"),
                        matchedPayments.getInt("tenant_id"),
                        matchedPayments.getInt("agreement_id"),
                        matchedPayments.getFloat("amount"),
                        matchedPayments.getDate("pay_date"),
                        matchedPayments.getString("payment_method"),
                        matchedPayments.getString("status")
                ));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        rentAgreementIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAgreementId()).asObject());
        paymentIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPaymentId()).asObject());
        tenantIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTenantId()).asObject());
        payMethodColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMethod()));
        payDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<Date>(cellData.getValue().getDate()));
        rentPriceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getAmount()).asObject());
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        PaymentTable.setItems(all_payment);
        PaymentTable.refresh();
    }

    public void updatePayment(ActionEvent actionEvent) throws SQLException {
        DatabaseConnector db = new DatabaseConnector();
        Statement statement = db.connect().createStatement();

        String query = "UPDATE public.payment SET amount = " +
                amountTextBox.getText() + ", status = '" +
                statusChoiceBox.getValue() + "'" +
                (paymentDatePicker.getValue() == null ? "" : (", pay_date = '" + paymentDatePicker.getValue() + "'")) +
                (paymentMethodChoiceBox.getValue() == null ? "" : (", payment_method = '" + paymentMethodChoiceBox.getValue() + "'")) +
                " WHERE payment_id = " + paymentId;
        statement.executeUpdate(query);

        query = "SELECT * " +
                "FROM public.payment ";
        System.out.println("Executed query: " + query);
        ResultSet matchedPayments = null;
        try {
            matchedPayments = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<Payment> all_payment = FXCollections.observableArrayList();
        while (true) {
            try {
                if (!matchedPayments.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                all_payment.add(new Payment(
                        matchedPayments.getInt("payment_id"),
                        matchedPayments.getInt("tenant_id"),
                        matchedPayments.getInt("agreement_id"),
                        matchedPayments.getFloat("amount"),
                        matchedPayments.getDate("pay_date"),
                        matchedPayments.getString("payment_method"),
                        matchedPayments.getString("status")
                ));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        rentAgreementIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAgreementId()).asObject());
        paymentIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPaymentId()).asObject());
        tenantIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTenantId()).asObject());
        payMethodColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMethod()));
        payDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<Date>(cellData.getValue().getDate()));
        rentPriceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getAmount()).asObject());
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        PaymentTable.setItems(all_payment);
        PaymentTable.refresh();
    }
}
