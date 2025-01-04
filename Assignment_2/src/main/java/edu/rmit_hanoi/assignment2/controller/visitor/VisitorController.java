package edu.rmit_hanoi.assignment2.controller.visitor;

import edu.rmit_hanoi.assignment2.model.property.ResidentProperty;
import edu.rmit_hanoi.assignment2.view.AllPropertyView;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class VisitorController {


    public AnchorPane visitorPane;
    public Button searchButton;
    public Button allPropButton;
    public Button helpButton;
    public Button toLoginMenu;

    @FXML
    public TableView<ResidentProperty> residentPropTable;

    public TableColumn<ResidentProperty, Integer> propID;
    public TableColumn<ResidentProperty, String> propType;
    public TableColumn<ResidentProperty, String> propAddress;
    public TableColumn<ResidentProperty, Integer> propBedroom;
    public TableColumn<ResidentProperty, Boolean> propGarden;
    public TableColumn<ResidentProperty, Boolean> propPet;

    public Stage stage;
    public Scene scene;
    public Parent root;

    private ObservableList<ResidentProperty> commercialPropList;


    public void switchToLoginMenu(ActionEvent event) throws IOException {
        if (logout(event) == false) return;

        root = FXMLLoader.load(getClass().getResource("/Fxml/login/Login.fxml"));
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public boolean logout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("Press OK to logout, or Cancel to stay on the current page.");

        if (alert.showAndWait().get() != ButtonType.OK) {
            return false;
        }
        return true;

    }


    public void browseAllProp(ActionEvent event) throws IOException {

        residentPropTable.setDisable(false);
        residentPropTable.setVisible(true);

        ObservableList<ResidentProperty> residentPropList = FXCollections.observableArrayList(
                new ResidentProperty(1, 2, "Test", 1, "Available", 2, true, true)
        );

        propID.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPropId()).asObject());
        propType.setCellValueFactory(cellData -> new SimpleStringProperty("Resident"));
        propAddress.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        propBedroom.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBedroomNum()).asObject());
        propGarden.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getGarden()).asObject());
        propPet.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getPetFriendliness()).asObject());
        residentPropTable.setItems(residentPropList);

    }


}
