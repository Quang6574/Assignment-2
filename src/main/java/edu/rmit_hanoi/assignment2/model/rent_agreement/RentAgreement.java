package edu.rmit_hanoi.assignment2.model.rent_agreement;
/**
 * @author Group 18
 */

import java.util.Date;
public class RentAgreement {
    private int agreementId;
    private int propertyId;
    private int ownerId;
    private int TenantId;
    private Date contractDate;
    private String period;
    private double fee;
    private String status;
    private String paymentStatus;



    public RentAgreement(int agreementId, int propertyId, int ownerId, int tenantId, Date contractDate, String period, double fee, String status, String paymentStatus) {
        this.agreementId = agreementId;
        this.propertyId = propertyId;
        this.ownerId = ownerId;
        this.TenantId = tenantId;
        this.contractDate = contractDate;
        this.period = period;
        this.fee = fee;
        this.status = status;
        this.paymentStatus = paymentStatus;
    }
    
    public int getAgreementId() {
        return agreementId;
    }
    public int getOwnerId() {
        return ownerId;
    }
    public int getTenantId() {
        return TenantId;
    }
    public Date getContractDate() {
        return contractDate;
    }
    public String getPeriod() {
        return period;
    }
    public double getFee() {
        return fee;
    }
    public String getStatus() {
        return status;
    }
    public int getPropertyId() {return propertyId;}
    public String getPaymentStatus() {return paymentStatus;}
}

