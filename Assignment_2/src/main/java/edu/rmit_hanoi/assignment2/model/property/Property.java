/** 
* @author Nguyen Phu Minh Quang - s3996843
*/ 

package edu.rmit_hanoi.assignment2.model.property;

import edu.rmit_hanoi.assignment2.model.person.Owner;

public abstract class Property {
    private int id;
    private String address;
    private float price;
    private String status;

    private int ownerId;
    private Owner owner;

    public Property(int id, int ownerId, String address, float price, String status) {
        this.id = id;

        this.ownerId = ownerId;
        this.owner = null;

        this.address = address;
        this.price = price;
        this.status = status;

    }

    //id, ownerId, address, price, status
    public int getPropId() {return this.id;}

    public int getOwnerId() {return this.ownerId;}
    public void setOwner(Owner owner) {this.owner = owner;}
    //public Owner getOwner() {return this.owner;}

    public String getAddress() {return this.address;}

    public float getPrice() {return this.price;}

    public String getStatus() {return this.status;}
    public void setStatus(String status) {this.status = status;}


    public String propInfo() {return this.id + ", " + this.address + ", " + this.price;}



}
