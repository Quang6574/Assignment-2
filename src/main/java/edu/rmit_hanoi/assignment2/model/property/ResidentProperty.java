package edu.rmit_hanoi.assignment2.model.property;
/**
 * @author Group 18
 */

public class ResidentProperty {
    private int id;
    private String address;
    private String status;
    private String period;
    private float fee;
    private int bedroomNum;
    private boolean garden;
    private boolean petFriendliness;

    public ResidentProperty(int id, String address, String status, String period, float fee, int bedroomNum, boolean garden, boolean petFriendliness) {
        this.id = id;
        this.address = address;
        this.status = status;
        this.period = period;
        this.fee = fee;
        this.bedroomNum = bedroomNum;
        this.garden = garden;
        this.petFriendliness = petFriendliness;
    }

    public int getBedroomNum() {return this.bedroomNum;}
    public boolean getGarden() {return this.garden;}
    public boolean getPetFriendliness() {return this.petFriendliness;}
    public int getPropId() {return this.id;}
    public String getAddress() {return this.address;}
    public String getStatus() {return this.status;}
    public void setStatus(String status) {this.status = status;}
    public String getPeriod() {return this.period;}
    public float getFee() {return this.fee;}
}
