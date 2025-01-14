package edu.rmit_hanoi.assignment2.controller.register_user.manager;
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
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ManagerStatistic implements Initializable {
    private static int managerID;
    public Text totalOwner;
    public Text totalHost;
    public Text totalTenant;
    public Text totalCommercial;
    public Text totalResident;
    public Text mostRentedBedroomNum;
    public Text gardenPreference;
    public Text petPreference;
    public Text popularBusinessType;
    public Text mostParkSlot;
    public Text totalRA;
    public Text totalPayment;
    public Text avgyearly;
    public Text avgMonthly;

    public void initialize(URL url, ResourceBundle resourceBundle) {

        DatabaseConnector db = new DatabaseConnector();
        Statement statement = null;
        try {
            statement = db.connect().createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String query = "SELECT COUNT(*) FROM public.user WHERE role = 'Owner'";
        ResultSet ownerCount = null;
        try {
            ownerCount = statement.executeQuery(query);
            ownerCount.next();
            totalOwner.setText(ownerCount.getString(1));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        query = "SELECT COUNT(*) FROM public.user WHERE role = 'Host'";
        ResultSet hostCount = null;
        try {
            hostCount = statement.executeQuery(query);
            hostCount.next();
            totalHost.setText(hostCount.getString(1));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        query = "SELECT COUNT(*) FROM public.user WHERE role = 'Tenant'";
        ResultSet tenantCount = null;
        try {
            tenantCount = statement.executeQuery(query);
            tenantCount.next();
            totalTenant.setText(tenantCount.getString(1));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        query = "SELECT COUNT(*) FROM public.rental_agreement";
        ResultSet raCount = null;
        try {
            raCount = statement.executeQuery(query);
            raCount.next();
            totalRA.setText(raCount.getString(1));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        query = "SELECT COUNT(*) FROM public.payment";
        ResultSet paymentCount = null;
        try {
            paymentCount = statement.executeQuery(query);
            paymentCount.next();
            totalPayment.setText(paymentCount.getString(1));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        query = "SELECT COUNT(*) FROM public.commercial_property";
        ResultSet commercialCount = null;
        try {
            commercialCount = statement.executeQuery(query);
            commercialCount.next();
            totalCommercial.setText(commercialCount.getString(1));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        query = "SELECT COUNT(*) FROM public.residential_property";
        ResultSet residentCount = null;
        try {
            residentCount = statement.executeQuery(query);
            residentCount.next();
            totalResident.setText(residentCount.getString(1));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        query = "SELECT bedroom_num FROM public.residential_property GROUP BY bedroom_num ORDER BY COUNT(*) DESC LIMIT 1";
        ResultSet mostRentedBedroom = null;
        try {
            mostRentedBedroom = statement.executeQuery(query);
            mostRentedBedroom.next();
            mostRentedBedroomNum.setText(mostRentedBedroom.getString(1));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        query = "SELECT garden FROM public.residential_property GROUP BY garden ORDER BY COUNT(*) DESC LIMIT 1";
        ResultSet garden = null;
        try {
            garden = statement.executeQuery(query);
            garden.next();
            gardenPreference.setText(garden.getString(1));
            if(gardenPreference.getText().equals("t")) {
                gardenPreference.setText("Property with garden is rented more");
            } else {
                gardenPreference.setText("Property without garden is rented more");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        query = "SELECT pet_friendliness FROM public.residential_property GROUP BY pet_friendliness ORDER BY COUNT(*) DESC LIMIT 1";
        ResultSet pet = null;
        try {
            pet = statement.executeQuery(query);
            pet.next();
            petPreference.setText(pet.getString(1));
            if(petPreference.getText().equals("t")) {
                petPreference.setText("Property with friendly pet is rented more");
            } else {
                petPreference.setText("Property without friendly pet is rented more");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        query = "SELECT business_type FROM public.commercial_property GROUP BY business_type ORDER BY COUNT(*) DESC LIMIT 1";
        ResultSet businessType = null;
        try {
            businessType = statement.executeQuery(query);
            businessType.next();
            popularBusinessType.setText(businessType.getString(1));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        query = "SELECT parking_num FROM public.commercial_property GROUP BY parking_num ORDER BY COUNT(*) DESC LIMIT 1";
        ResultSet parkSlot = null;
        try {
            parkSlot = statement.executeQuery(query);
            parkSlot.next();
            mostParkSlot.setText(parkSlot.getString(1));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        query = "WITH rental_data AS (" +
                "SELECT " +
                "r.contract_date, " +
                "r.property_id, " +
                "DATE_PART('day', DATE_TRUNC('year', r.contract_date + INTERVAL '1 year') - r.contract_date) AS days_left_in_year " +
                "FROM " +
                "rental_agreement r " +
                "JOIN property p ON r.property_id = p.property_id " +
                "), " +

                "total_cost_data AS (" +
                "SELECT " +
                "rd.contract_date, " +
                "rd.days_left_in_year, " +
                "CASE " +
                "WHEN p.period = 'Daily' THEN p.renting_fee * rd.days_left_in_year " +
                "WHEN p.period = 'Weekly' THEN p.renting_fee * (rd.days_left_in_year / 7) " +
                "WHEN p.period = 'Monthly' THEN p.renting_fee * (rd.days_left_in_year / 30) " +
                "WHEN p.period = 'Yearly' THEN p.renting_fee * (rd.days_left_in_year / 365) " +
                "ELSE 0 " +
                "END AS total_cost " +
                "FROM " +
                "rental_data rd " +
                "JOIN property p ON rd.property_id = p.property_id " +
                "), " +

                "yearly_revenue AS (" +
                "SELECT " +
                "EXTRACT(YEAR FROM contract_date) AS year, " +
                "SUM(total_cost) AS yearly_total " +
                "FROM total_cost_data " +
                "GROUP BY EXTRACT(YEAR FROM contract_date) " +
                ") " +

                "SELECT " +
                "SUM(yearly_total) / COUNT(*) AS average_yearly_revenue " +
                "FROM yearly_revenue;";
        ResultSet avgYearlyRevenue = null;
        try {
            avgYearlyRevenue = statement.executeQuery(query);
            avgYearlyRevenue.next();
            avgyearly.setText(String.format("%.3f", avgYearlyRevenue.getDouble(1)));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        query = "WITH rental_data AS (" +
                "SELECT " +
                "r.contract_date, " +
                "r.property_id, " +
                "DATE_PART('day', DATE_TRUNC('month', r.contract_date + INTERVAL '1 month') - r.contract_date) AS days_left_in_month " +
                "FROM " +
                "rental_agreement r " +
                "JOIN property p ON r.property_id = p.property_id " +
                "), " +

                "total_cost_data AS (" +
                "SELECT " +
                "rd.contract_date, " +
                "rd.days_left_in_month, " +
                "CASE " +
                "WHEN p.period = 'Daily' THEN p.renting_fee * rd.days_left_in_month " +
                "WHEN p.period = 'Weekly' THEN p.renting_fee * (rd.days_left_in_month / 7) " +
                "WHEN p.period = 'Monthly' THEN p.renting_fee * (rd.days_left_in_month / 30) " +
                "WHEN p.period = 'Yearly' THEN p.renting_fee * (rd.days_left_in_month / 365) " +
                "ELSE 0 " +
                "END AS total_cost " +
                "FROM " +
                "rental_data rd " +
                "JOIN property p ON rd.property_id = p.property_id " +
                "), " +

                "monthly_revenue AS (" +
                "SELECT " +
                "EXTRACT(YEAR FROM contract_date) AS year, " +
                "EXTRACT(MONTH FROM contract_date) AS month, " +
                "SUM(total_cost) AS monthly_total " +
                "FROM total_cost_data " +
                "GROUP BY " +
                "EXTRACT(YEAR FROM contract_date), " +
                "EXTRACT(MONTH FROM contract_date) " +
                ") " +

                "SELECT " +
                "SUM(monthly_total) / COUNT(*) AS average_monthly_revenue " +
                "FROM monthly_revenue;";
        ResultSet avgMonthlyRevenue = null;
        try {
            avgMonthlyRevenue = statement.executeQuery(query);
            avgMonthlyRevenue.next();
            avgMonthly.setText(String.format("%.3f", avgMonthlyRevenue.getDouble(1)));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void toManagerStatistic(ActionEvent actionEvent, int managerId) throws IOException {
        managerID = managerId;
        drawManagerStatistic(actionEvent);
    }
    public void drawManagerStatistic(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/manager/ManagerStatistic.fxml"));
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

    public void toPaymentManage(ActionEvent actionEvent) throws IOException {
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
}
