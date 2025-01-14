package edu.rmit_hanoi.assignment2.controller.register_user.host;
/**
 * @author Group 18
 */

import edu.rmit_hanoi.assignment2.Database.DatabaseConnector;
import edu.rmit_hanoi.assignment2.controller.LoginController;
import edu.rmit_hanoi.assignment2.model.payment.Payment;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import java.util.ResourceBundle;

public class HostUnpaid implements Initializable {
    public static int hostID;


    public TableView<Payment> unpaidTable;
    public TableColumn<Payment, Integer> rentAgreementIdColumn;
    public TableColumn<Payment, Integer> paymentIdColumn;
    public TableColumn<Payment, Integer> tenantIDColumn;
    public TableColumn<Payment, Double> rentAmountColumn;
    public Button notifyButton;
    public Button paidButton;

    public void initialize(URL url, ResourceBundle resourceBundle) {

        DatabaseConnector db = new DatabaseConnector();
        Statement statement = null;
        try {
            statement = db.connect().createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String query = "SELECT p.* " +
                "FROM public.payment p " +
                "JOIN public.rental_agreement ra ON p.agreement_id = ra.agreement_id " +
                "JOIN public.manage_property mp ON ra.property_id = mp.property_id " +
                "WHERE p.status = 'Unpaid' AND mp.host_id = " + hostID;
        System.out.println("Executed query: " + query);
        ResultSet matchedPayments = null;
        try {
            matchedPayments = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<Payment> unpaid_payment = FXCollections.observableArrayList();
        while (true) {
            try {
                if (!matchedPayments.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                unpaid_payment.add(new Payment(
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
        tenantIDColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTenantId()).asObject());
        rentAmountColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getAmount()).asObject());

        unpaidTable.setItems(unpaid_payment);
        unpaidTable.refresh();
    }

    public void toUnpaidPayment(ActionEvent event, int hostId) throws IOException {
        hostID = hostId;
        drawUnpaidPayment(event);
    }

    public void drawUnpaidPayment(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/host/HostUnpaid.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setTitle("Lux Property Management System");
        stage.setScene(scene);
        stage.show();
    }


    public void toStatistics(ActionEvent actionEvent) throws IOException {
        HostStatistic statisticsControl = new HostStatistic();
        statisticsControl.toStatistic(actionEvent, hostID);
    }


    public void switchToLoginMenu(ActionEvent actionEvent) throws IOException {
        LoginController loginControl = new LoginController();
        loginControl.drawLoginScene(actionEvent);
    }


    public void toManageCommercialProp(ActionEvent actionEvent) throws IOException {
        HostManageCommercial commercialControl = new HostManageCommercial();
        commercialControl.toManageCommercial(actionEvent, hostID);

    }

    public void toManageRentAgreement(ActionEvent actionEvent) throws IOException {
        HostUpdateRentAgreement updateRAControl = new HostUpdateRentAgreement();
        updateRAControl.toManageRentAgreement(actionEvent, hostID);
    }

    public void switchHostPersonalInfo(ActionEvent actionEvent) throws IOException {
        HostPersonInfo personInfoControl = new HostPersonInfo();
        personInfoControl.toPersonInfo(actionEvent, hostID);
    }

    public void rowSelected(MouseEvent mouseEvent) {
        try {
        int selectedRow = unpaidTable.getSelectionModel().getSelectedIndex();
        Payment selectedProp = unpaidTable.getItems().get(selectedRow);
        paymentId = selectedProp.getPaymentId();
        } catch (IndexOutOfBoundsException e) {
            System.out.print("e.printStackTrace()");
        }
    }

    private static int paymentId;

    public void notifyPayment(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Payment oNotified");
        alert.setHeaderText("The payment reminder has been sent to the corresponding main tenant");
        alert.setContentText("You can close this notification");

    }

    public void markPaid(ActionEvent actionEvent) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Mark Paid");
        alert.setHeaderText("Are you sure you want to mark the payment as paid?");
        alert.setContentText("Press OK to confirm, or Cancel to return");
        if (alert.showAndWait().get() != ButtonType.OK) return;

        DatabaseConnector db = new DatabaseConnector();
        Statement statement = db.connect().createStatement();

        String query = "UPDATE public.payment " +
                "SET status = 'Paid'" +
                "WHERE payment_id = " + paymentId;
        statement.executeUpdate(query);

        query = "SELECT p.* " +
                "FROM public.payment p " +
                "JOIN public.rental_agreement ra ON p.agreement_id = ra.agreement_id " +
                "JOIN public.manage_property mp ON ra.property_id = mp.property_id " +
                "WHERE p.status = 'Unpaid' AND mp.host_id = " + hostID;
        System.out.println("Executed query: " + query);
        ResultSet matchedPayments = null;
        try {
            matchedPayments = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<Payment> unpaid_payment = FXCollections.observableArrayList();
        while (true) {
            try {
                if (!matchedPayments.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                unpaid_payment.add(new Payment(
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
        tenantIDColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTenantId()).asObject());
        rentAmountColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getAmount()).asObject());

        unpaidTable.setItems(unpaid_payment);
        unpaidTable.refresh();
    }
}
