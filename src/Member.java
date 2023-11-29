import java.io.Serializable;
import java.time.LocalDate;

public class Member implements Serializable {

    // Attributter

    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private String gender;
    private boolean isActive;
    private boolean hasPaid;
    private int membershipPrice;
    private int memberID;
    private Team team;


// Konstruktør

    public Member(String firstName,String lastName, LocalDate birthdate, String gender, boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.gender = gender;
        this.isActive = isActive;
        setHasPaid(true); //altid betalt ved oprettelse
        calculateMembershipPrice();
        calcTeamFromDate();
    }


    // Alternativ konstruktør
    //Hvad bruges den til?
    /*public Member(String name, LocalDate birthdate, String gender, boolean isActive, boolean hasPaid, int membershipPrice, int memberID, CompetitionMember.Team team) {
        this.name = name;
        this.birthdate = birthdate;
        this.gender = gender;
        this.isActive = isActive;
    }
*/

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

    // Metoder for at få og indstille fødselsdato
    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    // Metoder for at få og indstille køn
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    // Metoder for at få og indstille betalingsstatus
    public boolean getHasPaid() {
        return hasPaid;
    }

    public void setHasPaid(boolean hasPaid) {
        this.hasPaid = hasPaid();
    }

    // Metoder for at få og indstille medlemskabspris
    public int getMembershipPrice() {
        return membershipPrice;
    }

    //Metode der kan beregne Membership price ud fra alder
    public int calculateMembershipPrice() {
        int age = calculateAge();
        int juniorRate = 1000;
        int seniorRate = 1600;
        int seniorDiscountPercent = 25;
        int seniorDiscount = (seniorDiscountPercent * seniorRate) / 100;
        int passiveRate = 500;

        //Kontingentet
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

    // Metoder for at få og indstille medlems -ID
    public int getMemberID() {
        return memberID;
    }

    //Brug Emmas kode i trello og lave et system for MemberID
    //En Member må kun have en ID, starter fra fx 1 og går et tal op hver gang der bliver
    //lavet ny member




    //Needs to set memberID from static nextID
    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    // Metode til at få aktiv status
    public boolean getIsActive() {
        return isActive;
    }

    //Metode til at indstille aktiv status
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    //Metode for at få og indstille betalingsstatus
    //Har allerede lavet tidligere
    //Til at begynde med når vi laver en Member, så er den altid True
    //Efterfølgende så skal man kunne ændre på den
    //Efter et år, så er paymentStatus false -> indtil den er betalt

    public boolean hasPaid() {
        return hasPaid;
    }

    public void setPaymentStatus(boolean hasPaid) {
        this.hasPaid = hasPaid;
    }


    // Metoder for at få og indstille hold
    public Team getTeam() {
        return team;
    }

    private int calculateAge() {
        return LocalDate.now().getYear() - this.birthdate.getYear();
    }

    //Metode til at sætte team ift. alder
    public Team calcTeamFromDate() {
        int age = calculateAge();
        if (age < 18) {
            return this.team = Team.JUNIOR;
        } else {
            return this.team = Team.SENIOR;
        }
    }

    // Metode til at beregne betaling
    public double calcPayment() {
        //Hvis medlemmet betalt, returneres medlemskabsprisen, ellers returneres 0.
        return hasPaid ? membershipPrice : 0;
    }

}