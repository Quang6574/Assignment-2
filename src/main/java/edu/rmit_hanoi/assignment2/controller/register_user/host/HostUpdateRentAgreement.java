package edu.rmit_hanoi.assignment2.controller.register_user.host;
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

public class HostUpdateRentAgreement implements Initializable {
    public static int hostID;

    public TableView<RentAgreement> rentAgreementTable;
    public TableColumn<RentAgreement, Integer> HMRAgreementIdColumn;
    public TableColumn<RentAgreement, Integer> HMRAPropertyIdColumn;
    public TableColumn<RentAgreement, Integer> HMRAOwnerColumn;
    public TableColumn<RentAgreement, Integer> HMRATenantColumn;
    public TableColumn<RentAgreement, Date> HRMAContractDateColumn;
    public TableColumn<RentAgreement, String> HMRAPeriodColumn;
    public TableColumn<RentAgreement, Double> HMRARentingFeeColumn;
    public TableColumn<RentAgreement, String> HMRAStatusColumn;
    public TextField HMRAidTextBox;
    public TextField HMRtenantIdTextBox;
    public DatePicker HMRContractDateTextBox;
    public TextField HMROwnerIdTextBox;
    public TextField HMRrentingFeeTextBox;
    public TextField HMRAPropertyIdTextBox;
    public ChoiceBox HMRAPeriodChoiceBox;
    public ChoiceBox HMRAStatusChoiceBox;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] statusList = {"New", "Active", "Completed"};
        String[] periodList = {"Daily", "Weekly", "Monthly", "Yearly"};

        HMRAStatusChoiceBox.getItems().addAll(statusList);
        HMRAPeriodChoiceBox.getItems().addAll(periodList);

        DatabaseConnector db = new DatabaseConnector();
        Statement statement = null;
        try {
            statement = db.connect().createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String query = "SELECT ra.*, p.period, p.renting_fee, p.owner_id " +
                "FROM public.manage_property mp " +
                "JOIN public.property p ON p.property_id = mp.property_id " +
                "JOIN public.rental_agreement ra ON ra.property_id = mp.property_id " +
                "WHERE mp.host_id = " + hostID;
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
                        matchedAgreements.getString("status"),
                        ""
                ));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        HMRAgreementIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAgreementId()).asObject());
        HMRAPropertyIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPropertyId()).asObject());
        HMRAOwnerColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getOwnerId()).asObject());
        HMRAPeriodColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
        HMRAStatusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        HMRARentingFeeColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getFee()).asObject());
        HRMAContractDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<Date>(cellData.getValue().getContractDate()));
        HMRATenantColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTenantId()).asObject());


        rentAgreementTable.setItems(associated_agreements);
        rentAgreementTable.refresh();
    }

    public void toManageRentAgreement(ActionEvent event, int hostId) throws IOException {
        hostID = hostId;
        drawUpdateAgreement(event);
    }

    public void drawUpdateAgreement(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/host/HostUpdateRentalAgreement.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setTitle("Lux Property Management System");
        stage.setScene(scene);
        stage.show();
    }

    public void switchToUnpaidPayment(ActionEvent event) throws IOException {
        HostUnpaid unpaidControl = new HostUnpaid();
        unpaidControl.toUnpaidPayment(event, hostID);
    }

    public void toStatistics(ActionEvent actionEvent) throws IOException {
        HostStatistic statisticsControl = new HostStatistic();
        statisticsControl.toStatistic(actionEvent, hostID);
    }

    public void toPersonInfo(ActionEvent event) throws IOException {
        HostPersonInfo personInfoControl = new HostPersonInfo();
        personInfoControl.toPersonInfo(event, hostID);
    }

    public void switchToLoginMenu(ActionEvent event) throws IOException {
        LoginController loginControl = new LoginController();
        loginControl.drawLoginScene(event);
    }

    public void toManageCommercialProp(ActionEvent actionEvent) throws IOException {
        HostManageCommercial commercialControl = new HostManageCommercial();
        commercialControl.toManageCommercial(actionEvent, hostID);
    }

    public void rowClicked(MouseEvent mouseEvent) {
        try {
        int selectedRow = rentAgreementTable.getSelectionModel().getSelectedIndex();
        RentAgreement selectedAgreement = rentAgreementTable.getItems().get(selectedRow);
        agreementId = selectedAgreement.getAgreementId();

        HMRAStatusChoiceBox.setValue(selectedAgreement.getStatus());
        HMRAPeriodChoiceBox.setValue(selectedAgreement.getPeriod());
        HMRAPropertyIdTextBox.setText(String.valueOf(selectedAgreement.getPropertyId()));
        HMRAidTextBox.setText(String.valueOf(selectedAgreement.getAgreementId()));
        HMRtenantIdTextBox.setText(String.valueOf(selectedAgreement.getTenantId()));
        HMRContractDateTextBox.setPromptText(selectedAgreement.getContractDate().toString());
        HMRrentingFeeTextBox.setText(String.valueOf(selectedAgreement.getFee()));
        HMROwnerIdTextBox.setText(String.valueOf(selectedAgreement.getOwnerId()));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("e.printStackTrace()");
        }
    }

    private static int agreementId;

    public void HMRAupdateRA(ActionEvent actionEvent) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Update Agreement");
        alert.setHeaderText("Are you sure you want to update the agreement");
        alert.setContentText("Press OK to confirm, or Cancel to return.");
        if (alert.showAndWait().get() != ButtonType.OK) return;

        DatabaseConnector db = new DatabaseConnector();
        Statement statement = db.connect().createStatement();

        // contract_date, renting_fee, period, status

        String query = "UPDATE public.rental_agreement " +
                "SET status = '" + HMRAStatusChoiceBox.getValue() + "'" +
                (HMRContractDateTextBox.getValue() == null ? "" : (", contract_date = '" + HMRContractDateTextBox.getValue() + "'")) +
                " WHERE agreement_id = " + agreementId +
                " RETURNING property_id";
        System.out.println(query);
        ResultSet rs = statement.executeQuery(query);
        int propertyId = 0;
        if (rs.next()) {
            propertyId = rs.getInt("property_id");
        }
        query = "UPDATE public.property " +
                "SET" + (HMRrentingFeeTextBox.getText() == null ? "" : (" renting_fee = " + HMRrentingFeeTextBox.getText())) +
                ", period = '" + HMRAPeriodChoiceBox.getValue() + "'" +
                " WHERE property_id = " + propertyId;
        System.out.println(query);
        statement.executeUpdate(query);

        query = "SELECT ra.*, p.period, p.renting_fee, p.owner_id " +
                "FROM public.manage_property mp " +
                "JOIN public.property p ON p.property_id = mp.property_id " +
                "JOIN public.rental_agreement ra ON ra.property_id = mp.property_id " +
                "WHERE mp.host_id = " + hostID;
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
                        matchedAgreements.getString("status"),
                        ""
                ));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        HMRAgreementIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAgreementId()).asObject());
        HMRAPropertyIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPropertyId()).asObject());
        HMRAOwnerColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getOwnerId()).asObject());
        HMRAPeriodColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
        HMRAStatusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        HMRARentingFeeColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getFee()).asObject());
        HRMAContractDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<Date>(cellData.getValue().getContractDate()));
        HMRATenantColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTenantId()).asObject());


        rentAgreementTable.setItems(associated_agreements);
        rentAgreementTable.refresh();
    }
}
