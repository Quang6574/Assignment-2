package edu.rmit_hanoi.assignment2.model.payment;
/**
 * @author Group 18
 */
import java.util.Date;

public class Payment {
    private int id;
    private int tenantId;
    private int agreementId;
    private float amount;
    private Date pDate;
    private String method;
    private String status;

    public Payment(int id, int tenantId, int agreementId, float amount, Date pDate, String method, String status) {
        this.id = id;
        this.tenantId = tenantId;
        this.agreementId = agreementId;
        this.amount = amount;
        this.pDate = pDate;
        this.method = method;
        this.status = status;
    }

    public int getPaymentId() {return this.id;}
    public int getTenantId() {return this.tenantId;}
    public float getAmount() {return this.amount;}
    public Date getDate() {return this.pDate;}
    public String getMethod() {return this.method;}
    public String getStatus() {return this.status;}
    public int getAgreementId() {return this.agreementId;}
}


