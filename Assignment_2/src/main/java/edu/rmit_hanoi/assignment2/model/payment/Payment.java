/** 
* @author Nguyen Phu Minh Quang - s3996843
*/ 

package edu.rmit_hanoi.assignment2.model.payment;

import edu.rmit_hanoi.assignment2.model.date.Date;

public class Payment {
    private int id;
    private int tenantId;

    private float amount;
    private Date pDate;
    private String method;

    public Payment(int id, int tenantId, float amount, int pDate, int pMonth, int pYear, String method) {
        this.id = id;
        this.tenantId = tenantId;
        this.amount = amount;
        this.pDate = new Date(pMonth, pDate, pYear);
        this.method = method;
    }

    public String paymentInfo() {
        return "Payment method: " + this.method + "\nAmount: " + this.amount + "\nPayment day: " + this.pDate.dInfo();
    }

    public int getPaymentId() {return this.id;}
    public int getTenantId() {return this.tenantId;}
}


