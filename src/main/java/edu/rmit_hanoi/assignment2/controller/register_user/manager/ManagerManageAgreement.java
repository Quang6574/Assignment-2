package edu.rmit_hanoi.assignment2.controller.register_user.manager;
/**
 * @author Group 18
 */

import edu.rmit_hanoi.assignment2.Database.DatabaseConnector;
import edu.rmit_hanoi.assignment2.controller.LoginController;
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

public class ManagerManageAgreement implements Initializable {
    private static int managerID;

    public TableView<RentAgreement> RAtable;
    public TableColumn<RentAgreement, Integer> IdColumn;
    public TableColumn<RentAgreement, Integer> TenantColumn;
    public TableColumn<RentAgreement, Integer> OwnerColumn;
    public TableColumn<RentAgreement, Date> ContractDateColumn;
    public TableColumn<RentAgreement, Double> RentingFeeColumn;
    public TableColumn<RentAgreement, String> PeriodColumn;
    public TableColumn<RentAgreement, String> StatusColumn;
    public TextField idTextBox;
    public TextField TenantTextBox;
    public TextField OwnerIdTextBox;
    public DatePicker ContractDatePicker;
    public TextField RentingFeeTextBox;
    public ChoiceBox PeriodChoiceBox;
    public ChoiceBox StatusChoiceBox;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] statusList = {"New", "Active", "Completed"};
        String[] periodList = {"Daily", "Weekly", "Monthly", "Yearly"};

        StatusChoiceBox.getItems().addAll(statusList);
        PeriodChoiceBox.getItems().addAll(periodList);

        DatabaseConnector db = new DatabaseConnector();
        Statement statement = null;
        try {
            statement = db.connect().createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String query = "SELECT ra.*, p.period, p.renting_fee, p.owner_id " +
                "FROM property p " +
                "JOIN rental_agreement ra ON ra.property_id = p.property_id ";
        System.out.println("Executed query: " + query);
        ResultSet matchedAgreements = null;
        try {
            matchedAgreements = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<RentAgreement> all_agreements = FXCollections.observableArrayList();
        while (true) {
            try {
                if (!matchedAgreements.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                all_agreements.add(new RentAgreement(
                        matchedAgreements.getInt("agreement_id"),
                        matchedAgreements.getInt("property_id"),
                        matchedAgreements.getInt("owner_id"),
                        matchedAgreements.getInt("tenant_id"),
                        matchedAgreements.getDate("contract_date"),
                        matchedAgreements.getString("period"),
                        matchedAgreements.getDouble("renting_fee"),
                        matchedAgreements.getString("status"),
                        ""
                ));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        IdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAgreementId()).asObject());
        OwnerColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getOwnerId()).asObject());
        PeriodColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
        StatusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        RentingFeeColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getFee()).asObject());
        ContractDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<Date>(cellData.getValue().getContractDate()));
        TenantColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTenantId()).asObject());


        RAtable.setItems(all_agreements);
        RAtable.refresh();
    }

    public void toManageAgreement(ActionEvent actionEvent, int managerId) throws IOException {
        managerID = managerId;
        drawManageAgreement(actionEvent);
    }

    public void drawManageAgreement(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/manager/ManagerManageAgreement.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setTitle("Lux Property Management System");

        stage.setScene(scene);

        stage.show();
    }

    public void switchToLoginMenu(ActionEvent actionEvent) throws IOException {
        LoginController loginController = new LoginController();
        loginController.drawLoginScene(actionEvent);
    }

    public void toManagePayment(ActionEvent actionEvent) throws IOException {
        ManagerManagePayment managerManagePayment = new ManagerManagePayment();
        managerManagePayment.toManagePayment(actionEvent, managerID);
    }

    public void switchToManageUser(ActionEvent actionEvent) throws IOException {
        ManagerManageUser managerManageUser = new ManagerManageUser();
        managerManageUser.toManageUser(actionEvent, managerID);
    }

    public void toManageCommercial(ActionEvent actionEvent) throws IOException {
        ManagerManageCommercial managerManageCommercial = new ManagerManageCommercial();
        managerManageCommercial.toManageCommercial(actionEvent, managerID);
    }

    public void switchToViewStatistic(ActionEvent actionEvent) throws IOException {
        ManagerStatistic managerStatistic = new ManagerStatistic();
        managerStatistic.toManagerStatistic(actionEvent, managerID);
    }

    public void rowClicked(MouseEvent mouseEvent) {

        try {
            int selectedRow = RAtable.getSelectionModel().getSelectedIndex();
            RentAgreement selectedAgreement = RAtable.getItems().get(selectedRow);
            agreementId = selectedAgreement.getAgreementId();

            StatusChoiceBox.setValue(selectedAgreement.getStatus());
            PeriodChoiceBox.setValue(selectedAgreement.getPeriod());
            idTextBox.setText(String.valueOf(selectedAgreement.getAgreementId()));
            TenantTextBox.setText(String.valueOf(selectedAgreement.getTenantId()));
            OwnerIdTextBox.setText(String.valueOf(selectedAgreement.getOwnerId()));
            ContractDatePicker.setPromptText(selectedAgreement.getContractDate().toString());
            RentingFeeTextBox.setText(String.valueOf(selectedAgreement.getFee()));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("e.printStackTrace()");
        }
    }

    private static int agreementId;

    public void updateRentalAgreement(ActionEvent actionEvent) throws SQLException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Update Agreement");
        alert.setHeaderText("Are you sure you want to update this agreement?");
        alert.setContentText("Press OK to confirm, or Cancel to return and review the agreement information.");

        if (alert.showAndWait().get() != ButtonType.OK) return;
        DatabaseConnector db = new DatabaseConnector();
        Statement statement = db.connect().createStatement();

        // contract_date, renting_fee, period, status

        String query = "UPDATE public.rental_agreement " +
                "SET status = '" + StatusChoiceBox.getValue() + "'" +
                (ContractDatePicker.getValue() == null ? "" : (", contract_date = '" + ContractDatePicker.getValue() + "'")) +
                " WHERE agreement_id = " + agreementId +
                " RETURNING property_id";
        System.out.println(query);
        ResultSet rs = statement.executeQuery(query);
        int propertyId = 0;
        if (rs.next()) {
            propertyId = rs.getInt("property_id");
        }
        query = "UPDATE public.property " +
                "SET" + (RentingFeeTextBox.getText() == null ? "" : (" renting_fee = " + RentingFeeTextBox.getText())) +
                ", period = '" + PeriodChoiceBox.getValue() + "'" +
                " WHERE property_id = " + propertyId;
        System.out.println(query);
        statement.executeUpdate(query);

        query = "SELECT ra.*, p.period, p.renting_fee, p.owner_id " +
                "FROM property p " +
                "JOIN rental_agreement ra ON ra.property_id = p.property_id ";
        System.out.println("Executed query: " + query);
        ResultSet matchedAgreements = null;
        try {
            matchedAgreements = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<RentAgreement> all_agreements = FXCollections.observableArrayList();
        while (true) {
            try {
                if (!matchedAgreements.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                all_agreements.add(new RentAgreement(
                        matchedAgreements.getInt("agreement_id"),
                        matchedAgreements.getInt("property_id"),
                        matchedAgreements.getInt("owner_id"),
                        matchedAgreements.getInt("tenant_id"),
                        matchedAgreements.getDate("contract_date"),
                        matchedAgreements.getString("period"),
                        matchedAgreements.getDouble("renting_fee"),
                        matchedAgreements.getString("status"),
                        ""
                ));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        IdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAgreementId()).asObject());
        OwnerColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getOwnerId()).asObject());
        PeriodColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
        StatusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        RentingFeeColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getFee()).asObject());
        ContractDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<Date>(cellData.getValue().getContractDate()));
        TenantColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTenantId()).asObject());


        RAtable.setItems(all_agreements);
        RAtable.refresh();
    }

    public void deleteRentalAgreement(ActionEvent actionEvent) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Agreement");
        alert.setHeaderText("Are you sure you want to delete this agreement?");
        alert.setContentText("Press OK to confirm, or Cancel to return.");
        if (alert.showAndWait().get() != ButtonType.OK) return;

        DatabaseConnector db = new DatabaseConnector();
        Statement statement = null;

        statement = db.connect().createStatement();

        String query = "DELETE FROM public.rental_agreement WHERE agreement_id = " + agreementId + " RETURNING property_id";
        ResultSet propertyId = statement.executeQuery(query);
        propertyId.next();
        System.out.println(query);
        query = "UPDATE public.property SET status = 'Available' WHERE property_id = " + propertyId.getInt(1);
        statement.executeUpdate(query);

        query = "SELECT ra.*, p.period, p.renting_fee, p.owner_id " +
                "FROM property p " +
                "JOIN rental_agreement ra ON ra.property_id = p.property_id ";
        System.out.println("Executed query: " + query);
        ResultSet matchedAgreements = null;
        try {
            matchedAgreements = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<RentAgreement> all_agreements = FXCollections.observableArrayList();
        while (true) {
            try {
                if (!matchedAgreements.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                all_agreements.add(new RentAgreement(
                        matchedAgreements.getInt("agreement_id"),
                        matchedAgreements.getInt("property_id"),
                        matchedAgreements.getInt("owner_id"),
                        matchedAgreements.getInt("tenant_id"),
                        matchedAgreements.getDate("contract_date"),
                        matchedAgreements.getString("period"),
                        matchedAgreements.getDouble("renting_fee"),
                        matchedAgreements.getString("status"),
                        ""
                ));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        IdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAgreementId()).asObject());
        OwnerColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getOwnerId()).asObject());
        PeriodColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
        StatusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        RentingFeeColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getFee()).asObject());
        ContractDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<Date>(cellData.getValue().getContractDate()));
        TenantColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTenantId()).asObject());


        RAtable.setItems(all_agreements);
        RAtable.refresh();
    }
}
