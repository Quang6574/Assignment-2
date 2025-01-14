package edu.rmit_hanoi.assignment2.model.property;
/**
 * @author Group 18
 */

public class CommercialProperty {
    private int id;
    private String address;
    private String status;
    private String period;
    private float fee;
    private String buisnessType;
    private int parkingNum;
    private float area;

    public CommercialProperty(int id, String address, String status, String period, float fee, String businessType, int parkingNum, float area) {
        this.id = id;
        this.address = address;
        this.status = status;
        this.period = period;
        this.fee = fee;
        this.buisnessType = businessType;
        this.parkingNum = parkingNum;
        this.area = area;
    }

    //businessType, parkingNum, area
    public String getBusinessType() {return this.buisnessType;}
    public int getParkingNum() {return this.parkingNum;}
    public float getArea() {return this.area;}
    public int getPropId() {return this.id;}
    public String getAddress() {return this.address;}
    public String getStatus() {return this.status;}
    public void setStatus(String status) {this.status = status;}
    public String getPeriod() {return this.period;}
    public float getFee() {return this.fee;}

}
