/** 
* @author Nguyen Phu Minh Quang - s3996843
*/ 

package edu.rmit_hanoi.assignment2.model.person;

import java.util.ArrayList;
import java.util.LinkedList;

import edu.rmit_hanoi.assignment2.model.rent_agreement.RentAgreement;
import edu.rmit_hanoi.assignment2.model.payment.Payment;

public class Tenant extends Person {
    private LinkedList<RentAgreement> rentAgreement;
    private ArrayList<Payment> transaction;

    public Tenant(int id, String fullName, int month, int date, int year, int phoneNum, String email) {
        super(id, fullName, month, date, year, phoneNum, email);
        this.rentAgreement = new LinkedList<RentAgreement>();
        this.transaction = new ArrayList<Payment>();
        //this.rentAgreementId = new ArrayList<Integer>();
    }

    public void addRentAgreement(RentAgreement rentAgreement) {this.rentAgreement.add(rentAgreement);}
    public void addTransaction(Payment transaction) {this.transaction.add(transaction);}
    public LinkedList<RentAgreement> getAgreementList() {return this.rentAgreement;}

    public void allRentAgreement() {
        if (this.rentAgreement.isEmpty()) {
            System.out.println("This tenant has no rent agreement.");
            return;
        }
        int counter = 1;
        for (RentAgreement ra : this.rentAgreement) {
            System.out.println("________\nAgreement No." + (counter++) + ":");
            System.out.println(ra.agreementInfo() + "\n");
        }
        System.out.println("________");
    }

    public void allTransaction() {
        if (this.transaction.isEmpty()) {
            System.out.println("This tenant has no transaction.");
            return;
        }
        int counter = 1;
        for (Payment p : this.transaction) {
            System.out.println("________\nTransaction No." + (counter++) + ":");
            System.out.println(p.paymentInfo() + "\n");
        }
        System.out.println("________");
    }



}
