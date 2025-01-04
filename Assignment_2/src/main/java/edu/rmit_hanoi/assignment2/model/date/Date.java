/** 
* @author Nguyen Phu Minh Quang - s3996843
*/ 
package edu.rmit_hanoi.assignment2.model.date;

public class Date {
    private int month;
    private int day;
    private int year;

    public Date() {
        this.month = 0;
        this.day = 0;
        this.year = -1;
    }
    public Date(int month, int day, int year) {
        this.month = month;
        this.day = day;
        this.year = year;
    }

    public int getMonth() {return month;}
    public void setMonth(int month) {this.month = month;}

    public int getDay() {return day;}
    public void setDay(int day) {this.day = day;}

    public int getYear() {return year;}
    public void setYear(int year) {this.year = year;}

    public String dInfo() {return this.month + "/" + this.day + "/" + this.year;}
}
