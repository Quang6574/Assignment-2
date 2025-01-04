/** 
* @author Nguyen Phu Minh Quang - s3996843
*/ 

package edu.rmit_hanoi.assignment2.model.rent_agreement;

import edu.rmit_hanoi.assignment2.model.date.Date;
import edu.rmit_hanoi.assignment2.model.person.Host;
import edu.rmit_hanoi.assignment2.model.person.Tenant;
import edu.rmit_hanoi.assignment2.model.property.Property;
import edu.rmit_hanoi.assignment2.model.property.CommercialProperty;
import edu.rmit_hanoi.assignment2.model.property.ResidentProperty;

import java.util.ArrayList;
import java.util.LinkedList;

import edu.rmit_hanoi.assignment2.model.person.Owner;
public class RentAgreement {
    private int agreementId;

    private int ownerId;
    private Owner owner;

    private int mHostId;
    private Host mHost;
    private LinkedList<Host> sHost;

    private String propType;
    private String period;

    private int mTenantId;
    private Tenant mTenant;
    private LinkedList<Tenant> sTenant;

    private int propertyId;
    private Property property;

    private Date startDate;
    private Date endDate;

    private float fee;
    private String status;


    public RentAgreement(int agreementId, int propertyId, int mTenantId, int mHostId, String propType, String period,
                         int sday, int smonth, int syear, int eday, int emonth, int eyear, float fee, String status) {
        this.agreementId = agreementId;

        this.ownerId = -1;
        this.owner = null;

        this.mHostId = mHostId;
        this.mHost = null;
        this.sHost = new LinkedList<Host>();

        this.propType = propType;
        this.period = period;

        this.mTenantId = mTenantId;
        this.mTenant = null;
        this.sTenant = new LinkedList<Tenant>();

        this.propertyId = propertyId;
        this.property = null;

        this.startDate = new Date(sday, smonth, syear);
        this.endDate = new Date(eday, emonth, eyear);

        this.fee = fee;
        this.status = status;
    }

    //
    public int getAgreementId() {return this.agreementId;}

    public void setOwner(Owner owner) {this.owner = owner;}
    public Owner getOwner() {return this.owner;}

    public void setOwnerId(int ownerId) {this.ownerId = ownerId;}
    public int getOwnerId() {return this.ownerId;}

    public int getMTenantId() {return this.mTenantId;}
    public void setMTenant(Tenant mTenant) {this.mTenant = mTenant;}
    public void setMTenantId(int mTenantId) {this.mTenantId = mTenantId;}

    public int getMHostId() {return this.mHostId;}
    public void setMHost(Host mHost) {this.mHost = mHost;}
    public void setMHostId(int mHostId) {this.mHostId = mHostId;}

    public int getPropertyId() {return this.propertyId;}

    public void setProperty(Property property) {this.property = property;}
    public Property getProperty() {return this.property;}

    public String getPeriod() {return this.period;}
    public void setPeriod(String period) {this.period = period;}

    public Date getStartDate() {return this.startDate;}
    public void setStartDate(Date startDate) {this.startDate = startDate;}

    public Date getEndDate() {return this.endDate;}
    public void setEndDate(Date endDate) {this.endDate = endDate;}

    public String getPropType() {return this.propType;}
    public void setPropType(String propType) {this.propType = propType;}

    public String getStatus() {return this.status;}

    public float getFee() {return fee;}
    public void setFee(float fee) {this.fee = fee;}

    public void addSHost(Host host) {this.sHost.add(host);}
    public void addSTenant(Tenant tenant) {this.sTenant.add(tenant);}

    public LinkedList<Tenant> getSTenantList() {return this.sTenant;}
    public LinkedList<Host> getSHostList() {return this.sHost;}

    //get all sub-hosts
    public void allSHost() {
        if (this.sHost.isEmpty()) {
            System.out.println("No sub-hosts found");
            return;
        }
        int counter = 1;
        for (Host host : this.sHost) {
            System.out.println("________\nSub Host No." + (counter++) + ":");
            System.out.println(host.personInfo());
        }
    }

    //get all sub-tenants
    public void allSTenant() {
        if (this.sTenant.isEmpty()) {
            System.out.println("No sub-tenants found");
            return;
        }
        int counter = 1;
        for (Tenant tenant : this.sTenant) {
            System.out.println("________\nSub Tenant No." + (counter++) +
                    " (id, name, edu.rmit_hanoi.assignment2.model.date of birth, phone number, email):");
            System.out.println(tenant.personInfo());
        }
    }

    //get basic info of agreement
    public String agreementInfo() {
        return "__________________\n" + "Agreement ID: " + this.getAgreementId() +
                "\n__________________\nProperty Info (id, address and price):\n" + this.property.propInfo() +
                "\n__________________\nOwner Info (id, name, edu.rmit_hanoi.assignment2.model.date of birth, phone number and email):\n" + this.owner.personInfo() +
                "\n__________________\nMain Tenant Info (id, name, edu.rmit_hanoi.assignment2.model.date of birth, phone number and email):\n" + this.mTenant.personInfo() +
                "\n__________________\nMain Host Info (id, name, edu.rmit_hanoi.assignment2.model.date of birth, phone number and email):\n" + this.mHost.personInfo() +
                "\n__________________\nAgreement Info:" + "\nStart Date: " + this.startDate.dInfo() +
                "\nEnd Date: " + this.endDate.dInfo() + "\nFee: " + this.fee + "\nStatus: " + this.status;
    }

    //convert id of owner, tenant, host and edu.rmit_hanoi.assignment2.model.property to object in agreement
    public void addObjToRentAgreement(ArrayList<ResidentProperty> residentProperty,
                                   ArrayList<CommercialProperty> commercialProperty, ArrayList<Owner> owner,
                                   ArrayList<Tenant> tenant, ArrayList<Host> host) {

        String propType = this.getPropType().toLowerCase();
        if (propType.equals("resident")) {
            this.setProperty(residentProperty.get(this.getPropertyId() - 1));
        } else {
            this.setProperty(commercialProperty.get(this.getPropertyId() - 1));
        }

        this.setOwnerId(this.getProperty().getOwnerId() - 1);

        this.setOwner(owner.get(this.getOwnerId()));
        this.setMTenant(tenant.get(this.getMTenantId() - 1));
        this.setMHost(host.get(this.getMHostId() - 1));
    }

}

