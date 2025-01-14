package edu.rmit_hanoi.assignment2.controller.register_user.tenant;
/**
 * @author Group 18
 */

import edu.rmit_hanoi.assignment2.Database.DatabaseConnector;
import edu.rmit_hanoi.assignment2.controller.LoginController;
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

public class TenantBrowseAgreement implements Initializable {
    public static int tenantID;

    public TableView<RentAgreement> rentAgreementTable;
    public TableColumn<RentAgreement, Integer> idColumn;
    public TableColumn<RentAgreement, Integer> propertyIdColumn;
    public TableColumn<RentAgreement, String> contractDateColumn;
    public TableColumn<RentAgreement, String> periodColumn;
    public TableColumn<RentAgreement, Double> feeColumn;
    public TableColumn<RentAgreement, String> paymentStatusRow;
    public TableColumn<RentAgreement, Integer> ownerIdColumn;
    public ChoiceBox<String> periodChoiceBox;
    public ChoiceBox<String> payStatusChoiceBox;
    public DatePicker contractStartDate;
    public TextField minFee;
    public DatePicker contractEndDate;
    public TextField maxFee;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] periodList = {"Daily", "Weekly", "Monthly", "Yearly"};
        String[] payStatusList = {"Paid", "Unpaid"};

        periodChoiceBox.getItems().addAll(periodList);
        payStatusChoiceBox.getItems().addAll(payStatusList);

        DatabaseConnector db = new DatabaseConnector();
        Statement statement = null;
        try {
            statement = db.connect().createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String query = "SELECT ra.agreement_id, ra.property_id, ra.tenant_id, ra.contract_date, pro.period, pro.renting_fee, pro.owner_id, p.status " +
                "FROM public.property pro " +
                "JOIN public.rental_agreement ra ON ra.property_id = pro.property_id " +
                "JOIN public.payment p ON ra.agreement_id = p.agreement_id " +
                "WHERE ra.tenant_id = " + tenantID;
        System.out.println("Executed query: " + query);
        ResultSet matchedAgreements = null;
        try {
            matchedAgreements = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<RentAgreement> associated_agreements = FXCollections.observableArrayList();
        while (true) {
            try {
                if (!matchedAgreements.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                associated_agreements.add(new RentAgreement(
                        matchedAgreements.getInt("agreement_id"),
                        matchedAgreements.getInt("property_id"),
                        matchedAgreements.getInt("owner_id"),
                        matchedAgreements.getInt("tenant_id"),
                        matchedAgreements.getDate("contract_date"),
                        matchedAgreements.getString("period"),
                        matchedAgreements.getDouble("renting_fee"),
                        "",
                        matchedAgreements.getString("status")
                ));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAgreementId()).asObject());
        ownerIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getOwnerId()).asObject());
        propertyIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPropertyId()).asObject());
        periodColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
        feeColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getFee()).asObject());
        contractDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<Date>(cellData.getValue().getContractDate()).asString());
        paymentStatusRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaymentStatus()));

        rentAgreementTable.setItems(associated_agreements);
        rentAgreementTable.refresh();
    }

    public void switchToAgreement(ActionEvent actionEvent, int tenantId) throws IOException {
        tenantID = tenantId;
        drawBrowseAgreement(actionEvent);
    }

    public void drawBrowseAgreement(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/tenant/TenantBrowseRA.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setTitle("Lux Property Management System");

        stage.setScene(scene);

        stage.show();
    }
    public void switchToBrowseProperty(ActionEvent actionEvent) throws IOException {
        TenantRentCommercial tenantRentCommercial = new TenantRentCommercial();
        tenantRentCommercial.toBrowseCommercial(actionEvent, tenantID);
    }

    public void switchPersonInfo(ActionEvent actionEvent) throws IOException {
        TenantPersonInfo tenantPersonInfo = new TenantPersonInfo();
        tenantPersonInfo.toTenantPersonInfo(actionEvent, tenantID);
    }

    public void switchToLoginMenu(ActionEvent actionEvent) throws IOException {
        LoginController loginControl = new LoginController();
        loginControl.drawLoginScene(actionEvent);
    }

    public void searchAgreement(ActionEvent actionEvent) throws SQLException {
        DatabaseConnector db = new DatabaseConnector();
        Statement statement = db.connect().createStatement();

        String query = "SELECT ra.agreement_id, ra.property_id, ra.tenant_id, ra.contract_date, pro.period, pro.renting_fee, pro.owner_id, p.status " +
                "FROM public.property pro " +
                "JOIN public.rental_agreement ra ON ra.property_id = pro.property_id " +
                "JOIN public.payment p ON ra.agreement_id = p.agreement_id " +
                "WHERE ra.tenant_id = " + tenantID +
                (contractStartDate.getValue() == null ? "" : " AND ra.contract_date >= '" + contractStartDate.getValue() + "'") +
                (contractEndDate.getValue() == null ? "" : " AND ra.contract_date <= '" + contractEndDate.getValue() + "'") +
                (minFee.getText().isEmpty() ? "" : " AND pro.renting_fee >= " + minFee.getText()) +
                (maxFee.getText().isEmpty() ? "" : " AND pro.renting_fee <= " + maxFee.getText()) +
                (periodChoiceBox.getValue() == null ? "" : " AND pro.period = '" + periodChoiceBox.getValue() + "'") +
                (payStatusChoiceBox.getValue() == null ? "" : " AND p.status = '" + payStatusChoiceBox.getValue() + "'");

        System.out.println("Executed query: " + query);
        ResultSet matchedAgreements = statement.executeQuery(query);

        ObservableList<RentAgreement> associated_agreements = FXCollections.observableArrayList();
        while (true) {
            if (!matchedAgreements.next()) break;
            associated_agreements.add(new RentAgreement(
                    matchedAgreements.getInt("agreement_id"),
                    matchedAgreements.getInt("property_id"),
                    matchedAgreements.getInt("owner_id"),
                    matchedAgreements.getInt("tenant_id"),
                    matchedAgreements.getDate("contract_date"),
                    matchedAgreements.getString("period"),
                    matchedAgreements.getDouble("renting_fee"),
                    "",
                    matchedAgreements.getString("status")

            ));
        }

        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAgreementId()).asObject());
        ownerIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getOwnerId()).asObject());
        propertyIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPropertyId()).asObject());
        periodColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
        feeColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getFee()).asObject());
        contractDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<Date>(cellData.getValue().getContractDate()).asString());
        paymentStatusRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaymentStatus()));

        rentAgreementTable.setItems(associated_agreements);
        rentAgreementTable.refresh();
    }

    public void rowSelected (MouseEvent mouseEvent) {
        int selectedRow = rentAgreementTable.getSelectionModel().getSelectedIndex();
        RentAgreement selectedAgreement = rentAgreementTable.getItems().get(selectedRow);
        agreementId = selectedAgreement.getAgreementId();
    }

    private static int agreementId;

    public void payRA(ActionEvent actionEvent) throws SQLException {
        DatabaseConnector db = new DatabaseConnector();
        Statement statement = db.connect().createStatement();

        String query = "UPDATE public.rental_agreement " +
                "SET status = 'Active' " +
                "WHERE agreement_id = " + agreementId;
        statement.executeUpdate(query);

        query = "UPDATE public.payment " +
                "SET status = 'Paid', pay_date = CURRENT_DATE " +
                "WHERE agreement_id = " + agreementId;
        statement.executeUpdate(query);

        query = "SELECT ra.agreement_id, ra.property_id, ra.tenant_id, ra.contract_date, pro.period, pro.renting_fee, pro.owner_id, p.status " +
                "FROM public.property pro " +
                "JOIN public.rental_agreement ra ON ra.property_id = pro.property_id " +
                "JOIN public.payment p ON ra.agreement_id = p.agreement_id " +
                "WHERE ra.tenant_id = " + tenantID +
                (contractStartDate.getValue() == null ? "" : " AND ra.contract_date >= '" + contractStartDate.getValue() + "'") +
                (contractEndDate.getValue() == null ? "" : " AND ra.contract_date <= '" + contractEndDate.getValue() + "'") +
                (minFee.getText().isEmpty() ? "" : " AND pro.renting_fee >= " + minFee.getText()) +
                (maxFee.getText().isEmpty() ? "" : " AND pro.renting_fee <= " + maxFee.getText()) +
                (periodChoiceBox.getValue() == null ? "" : " AND pro.period = '" + periodChoiceBox.getValue() + "'") +
                (payStatusChoiceBox.getValue() == null ? "" : " AND p.status = '" + payStatusChoiceBox.getValue() + "'");

        System.out.println("Executed query: " + query);
        ResultSet matchedAgreements = statement.executeQuery(query);

        ObservableList<RentAgreement> associated_agreements = FXCollections.observableArrayList();
        while (true) {
            if (!matchedAgreements.next()) break;
            associated_agreements.add(new RentAgreement(
                    matchedAgreements.getInt("agreement_id"),
                    matchedAgreements.getInt("property_id"),
                    matchedAgreements.getInt("owner_id"),
                    matchedAgreements.getInt("tenant_id"),
                    matchedAgreements.getDate("contract_date"),
                    matchedAgreements.getString("period"),
                    matchedAgreements.getDouble("renting_fee"),
                    "",
                    matchedAgreements.getString("status")

            ));
        }

        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAgreementId()).asObject());
        ownerIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getOwnerId()).asObject());
        propertyIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPropertyId()).asObject());
        periodColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
        feeColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getFee()).asObject());
        contractDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<Date>(cellData.getValue().getContractDate()).asString());
        paymentStatusRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaymentStatus()));

        rentAgreementTable.setItems(associated_agreements);
        rentAgreementTable.refresh();
    }
}
