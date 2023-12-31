import java.io.Serializable;
import java.time.LocalDate;

public class Member implements Serializable, Comparable {
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private String gender;
    private boolean isActive;
    private boolean hasPaid;
    private int membershipPrice;
    private int memberID;
    private Team team;

    public Member(String firstName, String lastName, LocalDate birthdate, String gender, boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.gender = gender;
        this.isActive = isActive;
        setPaymentStatus(true); //Always paid when member is created. Can be changed afterward
        calculateMembershipPrice(); //Calculates price when member is created
        calcTeamFromDate(); //Calculates team when Member is created
    }

    //Calculates team from date
    public Team calcTeamFromDate() {
        int age = calculateAge();
        if (age < 18) {
            return this.team = Team.JUNIOR;
        } else {
            return this.team = Team.SENIOR;
        }
    }

    //Method to calculate membership price based on age
    public int calculateMembershipPrice() {
        int age = calculateAge();
        int juniorRate = 1000;
        int seniorRate = 1600;
        int seniorDiscountPercent = 25;
        int seniorDiscount = (seniorDiscountPercent * seniorRate) / 100;
        int passiveRate = 500;

        if (isActive) {
            if (age < 18) {
                return this.membershipPrice = juniorRate;
            } else if (age >= 60) {
                return this.membershipPrice = seniorRate - seniorDiscount;
            } else {
                return this.membershipPrice = seniorRate;
            }
        } else {
            return this.membershipPrice = passiveRate;
        }
    }

    //method to calculate age
    private int calculateAge() {
        return LocalDate.now().getYear() - this.birthdate.getYear();
    }

    //appropriate getters and setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public String getGender() {
        return gender;
    }

    public int getMembershipPrice() {
        return membershipPrice;
    }

    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public boolean getIsActive() {
        return isActive;
    }

    //calculates membership price based on activity every time it is changed
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
        calculateMembershipPrice();
    }

    public Team getTeam() {
        return team;
    }

    public void setPaymentStatus(boolean hasPaid) {
        this.hasPaid = hasPaid;
    }

    public boolean getPaymentStatus() {
        return hasPaid;
    }

    @Override
    public String toString() {
        return " Member Id: " + getMemberID() + " " + getFirstName() + " " + getLastName();
    }

    @Override
    public int compareTo(Object o) {
        // IDK why it has to be this complicated, it should not be
        int returnValue = 0;
        if (o instanceof Member) {
            returnValue = Integer.compare(this.memberID, ((Member) o).getMemberID());
        }
        return returnValue;
    }
}