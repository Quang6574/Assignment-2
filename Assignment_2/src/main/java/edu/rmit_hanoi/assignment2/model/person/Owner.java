/** 
* @author Nguyen Phu Minh Quang - s3996843
*/ 

package edu.rmit_hanoi.assignment2.model.person;

import java.util.ArrayList;
import java.util.LinkedList;

import edu.rmit_hanoi.assignment2.model.property.Property;
import edu.rmit_hanoi.assignment2.model.rent_agreement.RentAgreement;

public class Owner extends Person {
    private ArrayList<Property> property;
    private ArrayList<Host> coopHost;
    private LinkedList<RentAgreement> rentAgreement;


    public Owner(int id, String fullName, int month, int date, int year, int phoneNum, String email) {
        super(id, fullName, month, date, year, phoneNum, email);
        this.property = new ArrayList<Property>();
        this.coopHost = new ArrayList<Host>();
        this.rentAgreement = new LinkedList<RentAgreement>();
    }

    public LinkedList<RentAgreement> getAgreementList() {return this.rentAgreement;}

    public void addProperty(Property property) {this.property.add(property);}
    public void addCoopHost(Host host) {this.coopHost.add(host);}
    public void addRentAgreement(RentAgreement rentAgreement) {this.rentAgreement.add(rentAgreement);}

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

    public void allProp() {
        if (this.property.isEmpty()) {
            System.out.println("No edu.rmit_hanoi.assignment2.model.property found for this owner.");
            System.out.println("This should not be possible, unless the csv data is incorrect.");
            return;
        }
        int counter = 1;
        for (Property p : this.property) {
            System.out.println("________\nProperty No." + (counter++) + ":");
            System.out.println(p.propInfo() + "\n");
        }
    }

    public void allHost() {
        if (this.coopHost.isEmpty()) {
            System.out.println("No host found for this owner.");
            System.out.println("-----------------------------");
            return;
        }
        int counter = 1;
        for (Host h : this.coopHost) {
            System.out.println("________\nHost No." + (counter++) + ":");
            System.out.println(h.personInfo() + "\n");
        }
        System.out.println("-----------------------------");
    }

}
