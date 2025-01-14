package edu.rmit_hanoi.assignment2.model.person;
/**
 * @author Group 18
 */

import edu.rmit_hanoi.assignment2.Database.DatabaseConnector;
import javafx.event.ActionEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class User {
    private int id;
    private String username;
    private String password;
    private String fullName;
    private Date dob;
    private String phoneNum;
    private String email;
    private String role;


    public User(int id, String username, String password, String fullName, Date dob, String phoneNum, String email, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.dob = dob;
        this.phoneNum = phoneNum;
        this.email = email;
        this.role = role;
    }

    public String getName() {return this.fullName;}
    public int getId() {return this.id;}
    public Date getDob() {return this.dob;}
    public String getPhoneNum() {return this.phoneNum;}
    public String getEmail() {return this.email;}
    public String getRole() {return this.role;}
    public String getUsername() {return this.username;}
    public String getPassword() {return this.password;}
}
