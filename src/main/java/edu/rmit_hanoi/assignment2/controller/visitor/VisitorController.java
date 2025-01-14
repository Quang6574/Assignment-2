package edu.rmit_hanoi.assignment2.controller.visitor;
/**
 * @author Group 18
 */

import edu.rmit_hanoi.assignment2.Database.DatabaseConnector;
import edu.rmit_hanoi.assignment2.controller.LoginController;
import edu.rmit_hanoi.assignment2.model.property.CommercialProperty;
import edu.rmit_hanoi.assignment2.model.property.ResidentProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class VisitorController {

    /*Start of all prop scene value*/

    //all resident prop table
    public TableView<ResidentProperty> allResidentTable;
    public TableColumn<ResidentProperty, Integer> a_residentIdRow;
    public TableColumn<ResidentProperty, String> a_residentIdAddress;
    public TableColumn<ResidentProperty, Integer> a_residentBedroomRow;
    public TableColumn<ResidentProperty, Boolean> a_residentGardenRow;
    public TableColumn<ResidentProperty, Boolean> a_residentPetRow;
    public TableColumn<ResidentProperty, String> a_residentPeriodRow;
    public TableColumn<ResidentProperty, Float> a_residentPriceRow;

    //all commercial prop table
    public TableView<CommercialProperty> allCommercialTable;
    public TableColumn<CommercialProperty, Integer> a_commercialIdRow;
    public TableColumn<CommercialProperty, String> a_commercialAddressRow;
    public TableColumn<CommercialProperty, String> a_commercialTypeRow;
    public TableColumn<CommercialProperty, Integer> a_commercialParkSlotRow;
    public TableColumn<CommercialProperty, Float> a_commercialAreaRow;
    public TableColumn<CommercialProperty, String> a_commercialPeriodRow;
    public TableColumn<CommercialProperty, Float> a_commercialPriceRow;

    //all prop button
    public Button allResidentButton;
    public Button allCommercialButton;

    /*End of all prop scene value*/


    /*Start of filter prop scene value*/

    //filtered resident table
    public TableView<ResidentProperty> filterResidentTable;
    public TableColumn<ResidentProperty, Integer> f_residentIdRow;
    public TableColumn<ResidentProperty, String> f_residentAddressRow;
    public TableColumn<ResidentProperty, Integer> f_residentBedRow;
    public TableColumn<ResidentProperty, Boolean> f_residentGardenRow;
    public TableColumn<ResidentProperty, Boolean> f_residentPetRow;
    public TableColumn f_residentImage;

    //resident filter option
    public VBox residentFilterOption;//container for resident filter option
    public TextField minBedField;
    public TextField maxBedField;
    public CheckBox gardenCheckbox;
    public CheckBox petCheckbox;
    public TextField residentPeriodBox;
    public TextField residentMinPriceBox;
    public TextField residentMaxPriceBox;

    //filtered commercial table
    public TableView<CommercialProperty> filterCommercialTable;
    public TableColumn<CommercialProperty, Integer> f_commercialIdRow;
    public TableColumn<CommercialProperty, String> f_commercialAddressRow;
    public TableColumn<CommercialProperty, String> f_commercialBusinessRow;
    public TableColumn<CommercialProperty, Integer> f_commercialParkRow;
    public TableColumn<CommercialProperty, Float> f_commercialAreaRow;
    public TableColumn f_residentPeriodBox;
    public TableColumn f_residentFeeBox;
    public TableColumn f_commercialImage;

    //commercial filter option
    public VBox commercialFilterOption;//container for commercial filter option
    public TextField commercialBusinessBox;
    public TextField parkMinBox ;
    public TextField parkMaxBox;
    public TextField areaMinBox;
    public TextField areaMaxBox;
    public TextField commercialPeriodBox;
    public TextField commercialPriceMinBox;
    public TextField commercialPriceMaxBox;


    //prop type filter button
    public Button filterResidentButton;
    public Button filterCommercialButton;

    //search button
    public Button searchButton;
    public TableColumn<ResidentProperty, String> f_residentPeriodRow;
    public TableColumn<ResidentProperty, Float> f_residentFeeRow;
    public TableColumn<CommercialProperty, String> f_commercialPeriodRow;
    public TableColumn<CommercialProperty, Float> f_commercialPeriodPrice;

    /*End of filter prop scene value*/


    public void switchToVisitorScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/visitor/VisitorHelp.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Lux Property Management System");
        stage.show();
    }

    public void showAllProp(ActionEvent event) throws IOException, SQLException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/visitor/VisitorAllProp.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public boolean logout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("Press OK to logout, or Cancel to stay on the current page.");

        return alert.showAndWait().get() == ButtonType.OK;

    }

    public void switchToLoginMenu(ActionEvent event) throws IOException {
        if (!logout(event)) return;

        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/login/Login.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public void dispAllCommercial(ActionEvent actionEvent) throws SQLException {
        //disable commercialButton
        allCommercialButton.setDisable(true);
        //enable commercialTable
        allCommercialTable.setDisable(false);
        allCommercialTable.setVisible(true);

        //disable residentButton
        allResidentButton.setDisable(false);
        //disable residentTable
        allResidentTable.setDisable(true);
        allResidentTable.setVisible(false);

        DatabaseConnector db = new DatabaseConnector();
        Statement statement = db.connect().createStatement();

        String query = "SELECT * " +
                "FROM public.property p " +
                "JOIN public.commercial_property cp ON p.property_id = cp.property_id " +
                "WHERE status = 'Available'";
        System.out.println("Executed query: " + query);

        ResultSet matchedProperties = statement.executeQuery(query);

        ObservableList<CommercialProperty> all_commercial = FXCollections.observableArrayList();
        while (matchedProperties.next()) {
            all_commercial.add(new CommercialProperty(
                    matchedProperties.getInt("property_id"),
                    matchedProperties.getString("address"),
                    matchedProperties.getString("status"),
                    matchedProperties.getString("period"),
                    matchedProperties.getFloat("renting_fee"),
                    matchedProperties.getString("business_type"),
                    matchedProperties.getInt("parking_num"),
                    matchedProperties.getFloat("area")
            ));
        }
        a_commercialIdRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPropId()).asObject());
        a_commercialAddressRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        a_commercialAreaRow.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getArea()).asObject());
        a_commercialParkSlotRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getParkingNum()).asObject());
        a_commercialTypeRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBusinessType()));
        a_commercialPeriodRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
        a_commercialPriceRow.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getFee()).asObject());

        allCommercialTable.setItems(all_commercial);
        allCommercialTable.refresh();
    }

    public void dispAllResident(ActionEvent actionEvent) throws IOException, SQLException {
        //disable residentButton
        allResidentButton.setDisable(true);
        //enable residentTable
        allResidentTable.setDisable(false);
        allResidentTable.setVisible(true);

        //disable commercialButton
        allCommercialButton.setDisable(false);
        //disable commercialTable
        allCommercialTable.setDisable(true);
        allCommercialTable.setVisible(false);

        //tableview implementation
        DatabaseConnector db = new DatabaseConnector();
        Statement statement = db.connect().createStatement();

        String query = "SELECT * " +
                "FROM public.property p " +
                "JOIN public.residential_property rp ON p.property_id = rp.property_id " +
                "WHERE status = 'Available'";
        System.out.println("Executed query: " + query);

        ResultSet matchedProperties = statement.executeQuery(query);

        ObservableList<ResidentProperty> all_resident = FXCollections.observableArrayList();
        while (matchedProperties.next()) {
            all_resident.add(new ResidentProperty(
            matchedProperties.getInt("property_id"),
            matchedProperties.getString("address"),
            matchedProperties.getString("status"),
            matchedProperties.getString("period"),
            matchedProperties.getFloat("renting_fee"),
            matchedProperties.getInt("bedroom_num"),
            matchedProperties.getBoolean("garden"),
            matchedProperties.getBoolean("pet_friendliness")
            ));
        }

        a_residentIdRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPropId()).asObject());
        a_residentIdAddress.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        a_residentBedroomRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBedroomNum()).asObject());
        a_residentGardenRow.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getGarden()).asObject());
        a_residentPetRow.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getPetFriendliness()).asObject());
        a_residentPeriodRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
        a_residentPriceRow.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getFee()).asObject());

        allResidentTable.setItems(all_resident);
        allResidentTable.refresh();
}


    public void showHelp(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/visitor/VisitorHelp.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void filterPropScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/visitor/VisitorFilter.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void filterTable(ActionEvent event) throws SQLException, IOException {
        //if resident prop is currently selected, execute the resident version
        //or else, execute the commercial version
        if (!filterCommercialButton.isDisabled()) {
            //tableview implementation
            DatabaseConnector db = new DatabaseConnector();
            Statement statement = db.connect().createStatement();


            String query = "SELECT * " +
                "FROM public.property p " +
                "JOIN public.residential_property rp ON p.property_id = rp.property_id " +
                "WHERE status = 'Available'" +
                (residentMinPriceBox.getText().isEmpty() ? "" : (" AND renting_fee >= " + residentMinPriceBox.getText())) +
                (residentMaxPriceBox.getText().isEmpty() ? "" : (" AND renting_fee <= " + residentMaxPriceBox.getText())) +
                (minBedField.getText().isEmpty() ? "" : (" AND bedroom_num >= " + minBedField.getText())) +
                (maxBedField.getText().isEmpty() ? "" : (" AND bedroom_num <= " + maxBedField.getText())) +
                (residentPeriodBox.getText().isEmpty() ? "" : (" AND period = '" + residentPeriodBox.getText() + "'")) +
                (gardenCheckbox.isSelected()? " AND garden = TRUE" : "") +
                (petCheckbox.isSelected()? " AND pet_friendliness = TRUE" : "");
            System.out.println("Executed query: " + query);
            ResultSet matchedProperties = statement.executeQuery(query);

            ObservableList<ResidentProperty> f_resident = FXCollections.observableArrayList();
            while (matchedProperties.next()) {
                f_resident.add(new ResidentProperty(
                matchedProperties.getInt("property_id"),
                matchedProperties.getString("address"),
                matchedProperties.getString("status"),
                matchedProperties.getString("period"),
                matchedProperties.getFloat("renting_fee"),
                matchedProperties.getInt("bedroom_num"),
                matchedProperties.getBoolean("garden"),
                matchedProperties.getBoolean("pet_friendliness")
                ));
            }

            f_residentIdRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPropId()).asObject());
            f_residentAddressRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
            f_residentBedRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBedroomNum()).asObject());
            f_residentGardenRow.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getGarden()).asObject());
            f_residentPetRow.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getPetFriendliness()).asObject());
            f_residentPeriodRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
            f_residentFeeRow.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getFee()).asObject());

            filterResidentTable.setItems(f_resident);
            filterResidentTable.refresh();
            return;
            }

            DatabaseConnector db = new DatabaseConnector();
            Statement statement = db.connect().createStatement();

            String query = "SELECT * " +
                    "FROM public.property p " +
                    "JOIN public.commercial_property cp ON p.property_id = cp.property_id " +
                    "WHERE status = 'Available'" +
                    (parkMinBox.getText().isEmpty() ? "" : (" AND parking_num >= '" + parkMinBox.getText() + "'")) +
                    (parkMaxBox.getText().isEmpty() ? "" : (" AND parking_num <= '" + parkMaxBox.getText() + "'")) +
                    (commercialPriceMinBox.getText().isEmpty() ? "" : (" AND renting_fee >= " + commercialPriceMinBox.getText())) +
                    (commercialPriceMaxBox.getText().isEmpty() ? "" : (" AND renting_fee <= " + commercialPriceMaxBox.getText())) +
                    (areaMinBox.getText().isEmpty() ? "" : (" AND area >= " + areaMinBox.getText())) +
                    (areaMaxBox.getText().isEmpty() ? "" : (" AND area <= " + areaMaxBox.getText())) +
                    (commercialPeriodBox.getText().isEmpty() ? "" : (" AND period = '" + commercialPeriodBox.getText() + "'")) +
                    (commercialBusinessBox.getText().isEmpty() ? "" : (" AND business_type = '" + commercialBusinessBox.getText() + "'"));
            System.out.println("Executed query: " + query);
            ResultSet matchedProperties = statement.executeQuery(query);
            System.out.println("Commercial SQL clause part 1 executed.");

            ObservableList<CommercialProperty> f_commercial = FXCollections.observableArrayList();
            while (matchedProperties.next()) {
                f_commercial.add(new CommercialProperty(
                        matchedProperties.getInt("property_id"),
                        matchedProperties.getString("address"),
                        matchedProperties.getString("status"),
                        matchedProperties.getString("period"),
                        matchedProperties.getFloat("renting_fee"),
                        matchedProperties.getString("business_type"),
                        matchedProperties.getInt("parking_num"),
                        matchedProperties.getFloat("area")
                ));
            }
            f_commercialIdRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPropId()).asObject());
            f_commercialAddressRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
            f_commercialAreaRow.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getArea()).asObject());
            f_commercialParkRow.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getParkingNum()).asObject());
            f_commercialBusinessRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBusinessType()));
            f_commercialPeriodRow.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
            f_commercialPeriodPrice.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getFee()).asObject());

            filterCommercialTable.setItems(f_commercial);
            filterCommercialTable.refresh();
        }

    //display filter option for resident prop
    public void dispFilterResident(ActionEvent event) throws IOException, SQLException {
        //disable resident filter button
        filterResidentButton.setDisable(true);
        //enable commercial filter button
        filterCommercialButton.setDisable(false);

        //enable and show resident option
        residentFilterOption.setDisable(false);
        residentFilterOption.setVisible(true);
        //enable and show resident table
        filterResidentTable.setDisable(false);
        filterResidentTable.setVisible(true);

        //disable and hide commercial option
        commercialFilterOption.setDisable(true);
        commercialFilterOption.setVisible(false);
        //disable and hide commercial table
        filterCommercialTable.setDisable(true);
        filterCommercialTable.setVisible(false);

        //show and enable the search button
        if(searchButton.isDisabled() && !searchButton.isVisible()) {
            searchButton.setDisable(false);
            searchButton.setVisible(true);
        }
    }

    public void dispFilterCommercial(ActionEvent event) {
        //disable commercial filter button
        filterCommercialButton.setDisable(true);
        //enable resident filter button
        filterResidentButton.setDisable(false);

        //disable and hide resident option
        residentFilterOption.setDisable(true);
        residentFilterOption.setVisible(false);
        //disable and hide resident table
        filterResidentTable.setDisable(true);
        filterResidentTable.setVisible(false);

        //enable and show commercial option
        commercialFilterOption.setDisable(false);
        commercialFilterOption.setVisible(true);
        //enable and show commercial table
        filterCommercialTable.setDisable(false);
        filterCommercialTable.setVisible(true);

        //show and enable the search button
        if(searchButton.isDisabled() && !searchButton.isVisible()) {
            searchButton.setDisable(false);
            searchButton.setVisible(true);
        }
    }
}


