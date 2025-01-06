/** 
* @author Nguyen Phu Minh Quang - s3996843
*/ 

package edu.rmit_hanoi.assignment2.model.person;

import edu.rmit_hanoi.assignment2.model.date.Date;

public abstract class Person {
    private int id;
    private String fullName;
    private Date dob;
    private int phoneNum;
    private String email;


    public Person(int id, String fullName, int month, int date, int year, int phoneNum, String email) {
        this.id = id;
        this.fullName = fullName;
        this.dob = new Date(month, date, year);
        this.phoneNum = phoneNum;
        this.email = email;
    }

    public String personInfo() {
        return this.id + ", " + this.fullName + ", " + this.dob.dInfo() + ", " + this.phoneNum + ", " + this.email;
    }

    public String getName() {return this.fullName;}
    public int getId() {return this.id;}
    public Date getDob() {return this.dob;}
    public int getPhoneNum() {return this.phoneNum;}
    public String getEmail() {return this.email;}

    public void setName(String fullName) {this.fullName = fullName;}
    public void setId(int id) {this.id = id;}
    public void setDob(Date dob) {this.dob = dob;}
    public void setPhoneNum(int phoneNum) {this.phoneNum = phoneNum;}
    public void setEmail(String email) {this.email = email;}
}
