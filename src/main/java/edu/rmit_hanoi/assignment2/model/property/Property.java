package edu.rmit_hanoi.assignment2.model.property;
/**
 * @author Group 18
 */

public class Property {
    private int id;
    private int ownerId;
    private String address;
    private String status;
    private String period;
    private double fee;

    public Property(int id, int ownerId, String address, String status, String period, double fee) {
        this.id = id;
        this.ownerId = ownerId;
        this.address = address;
        this.status = status;
        this.period = period;
        this.fee = fee;
    }

    //id, ownerId, address, price, status
    public int getPropId() {return this.id;}
    public int getOwnerId() {return this.ownerId;}
    public String getAddress() {return this.address;}
    public String getStatus() {return this.status;}
    public String getPeriod() {return this.period;}
    public double getFee() {return this.fee;}
}
