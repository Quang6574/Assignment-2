/** 
* @author Nguyen Phu Minh Quang - s3996843
*/ 

package edu.rmit_hanoi.assignment2.model.person;

import java.util.ArrayList;
import java.util.LinkedList;

import edu.rmit_hanoi.assignment2.model.property.Property;
import edu.rmit_hanoi.assignment2.model.rent_agreement.RentAgreement;

public class Host extends Person {
    private ArrayList<Property> coopProperty;
    private ArrayList<Owner> coopOwner;
    private LinkedList<RentAgreement> rentAgreement;

    //Default constructor
    /*
    public Host() {
        super();
        this.coopProperty = new ArrayList<Property>();
        this.coopOwner = new ArrayList<Owner>();
        this.rentAgreement = new LinkedList<RentAgreement>();
    }
     */
    public Host(int id, String fullName, int month, int date, int year, int phoneNum, String email) {
        super(id, fullName, month, date, year, phoneNum, email);
        this.coopProperty = new ArrayList<Property>();
        this.coopOwner = new ArrayList<Owner>();
        this.rentAgreement = new LinkedList<RentAgreement>();
    }

    public LinkedList<RentAgreement> getAgreementList() {return this.rentAgreement;}
    public void addCoopOwner(Owner owner) {
        this.coopOwner.add(owner);
    }
    public void addRentAgreement(RentAgreement rentAgreement) {this.rentAgreement.add(rentAgreement);}
    public void addProperty(Property property) {this.coopProperty.add(property);}

    public void allRentAgreement() {
        if (this.rentAgreement.isEmpty()) {
            System.out.println("No rent agreement found for this host.");
            return;
        }
        int counter = 1;
        for (RentAgreement ra : this.rentAgreement) {
            System.out.println("________\nAgreement No." + (counter++) + ":\n");
            System.out.println(ra.agreementInfo() + "\n");
        }
        System.out.println("-----------------------------");
    }

    public void allOwner() {
        if (this.coopOwner.isEmpty()) {
            System.out.println("No owner found for this host.");
            return;
        }
        int counter = 1;
        for (Owner o : this.coopOwner) {
            System.out.println("________\nCoop Owner No." + (counter++) + ":");
            System.out.println(o.personInfo() + "\n");
        }
        System.out.println("-----------------------------");
    }

    public void allProp() {
        if (this.coopProperty.isEmpty()) {
            System.out.println("No edu.rmit_hanoi.assignment2.model.property found for this host.");
            return;
        }
        int counter = 1;
        for (Property p : this.coopProperty) {
            System.out.println("________\nManaging Property No." + (counter++) + ":");
            System.out.println(p.propInfo() + "\n");
        }
        System.out.println("-----------------------------");
    }


}
