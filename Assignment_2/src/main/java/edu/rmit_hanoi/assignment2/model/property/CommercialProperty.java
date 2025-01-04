/** 
* @author Nguyen Phu Minh Quang - s3996843
*/ 

package edu.rmit_hanoi.assignment2.model.property;

public class CommercialProperty extends Property {
    private String buisnessType;
    private int parkingNum;
    private float area;

    public CommercialProperty(int id, int ownerId, String address, float price, String status,
                       String businessType, int parkingNum, float area) {
        super(id, ownerId, address, price, status);
        this.buisnessType = businessType;
        this.parkingNum = parkingNum;
        this.area = area;
    }

    //businessType, parkingNum, area
    public String getBusinessType() {return this.buisnessType;}

    public int getParkingNum() {return this.parkingNum;}

    public float getArea() {return this.area;}


    public String propInfo() {
        return super.propInfo() + "\nBusiness Type: " + this.buisnessType + "\nParking Number: " + this.parkingNum +
                "\nArea: " + this.area;
    }
}
