/** 
* @author Nguyen Phu Minh Quang - s3996843
*/ 

package edu.rmit_hanoi.assignment2.model.property;

public class ResidentProperty extends Property {
    private int bedroomNum;
    private boolean garden;
    private boolean petFriendliness;

    public ResidentProperty(int id, int ownerId, String address, float price, String status,
                     int bedroomNum, boolean garden, boolean petFriendliness) {
        super(id, ownerId, address, price, status);
        this.bedroomNum = bedroomNum;
        this.garden = garden;
        this.petFriendliness = petFriendliness;
    }

    public int getBedroomNum() {return this.bedroomNum;}

    public boolean getGarden() {return this.garden;}

    public boolean getPetFriendliness() {return this.petFriendliness;}

    public String propInfo() {
        return super.propInfo() + "\nBedroom Number: " + this.bedroomNum + "\nGarden: " + this.garden +
                "\nPet Friendliness: " + this.petFriendliness;
    }
}
