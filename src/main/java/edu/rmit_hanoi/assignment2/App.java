package edu.rmit_hanoi.assignment2;
/**
 * @author Group 18
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
public class App extends Application {

    @Override
    public void start (Stage stage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/login/Login.fxml"));
        Scene scene = new Scene(loader.load());

        stage.setResizable(false);
        stage.setTitle("Lux Property Management System");
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(event -> {
            event.consume();
            exit(stage);
        });
    }

    public void exit(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit application");
        alert.setHeaderText("Are you sure you want to close the application?");
        alert.setContentText("Press OK to close the application, or Cancel to stay on the current page.");

        if (alert.showAndWait().get() == ButtonType.OK) stage.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
