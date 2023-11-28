import java.io.Serializable;
import java.time.LocalDate;

public class Member implements Serializable {

    // Attributter

    private String name;
    private LocalDate birthdate;
    private char gender;
    private boolean isActive;
    private boolean hasPaid;
    private int membershipPrice;
    private int memberID;
    private Team team;


// Konstruktør

    public Member(String name, LocalDate birthdate, char gender, boolean isActive, int memberID) {
        this.name = name;
        this.birthdate = birthdate;
        this.gender = gender;
        this.isActive = isActive;
        this.memberID = memberID; //not needed after proper method is made
        setHasPaid(true); //altid betalt ved oprettelse
        setMembershipPrice();
        setTeamFromBirthdate();
    }


    // Alternativ konstruktør
    //Hvad bruges den til?
    /*public Member(String name, LocalDate birthdate, char gender, boolean isActive, boolean hasPaid, int membershipPrice, int memberID, CompetitionMember.Team team) {
        this.name = name;
        this.birthdate = birthdate;
        this.gender = gender;
        this.isActive = isActive;
    }
*/

    // Metoder for at få og indstille navn

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Metoder for at få og indstille fødselsdato

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    // Metoder for at få og indstille køn

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
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

    //Needs to calculate price
    public void setMembershipPrice() {
        this.membershipPrice = 0;

    }

    // Metoder for at få og indstille medlems -ID

    public int getMemberID() {
        return memberID;

    }
    //Needs to set memberID from static nextID
    public void setMemberID(int memberID) {
        this.memberID = memberID;

    }

    // Metode til at få aktiv status

    public boolean isActive() {
        return isActive;
    }

    //Metode til at indstille aktiv status

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    // Metode for at få og indstille betalingsstatus
    //Har allerede lavet tidligerer
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
    public void setTeam(Team team) {
        this.team = team;

    }

    // Metode til at indstille hold baseret på fødselsdato

    public void setTeamFromBirthdate() {
        this.team = calcTeamFromDate();
    }

    private int calculateAge() {
        return LocalDate.now().getYear() - this.birthdate.getYear();

    }

    public Team calcTeamFromDate() {
        int age = calculateAge();
        if (age < 18) {
            return this.team = Team.JUNIOR;
        } else {
            return this.team = Team.SENIOR;
        }

    }



    // Metode til at få discipliner

    //public List<String> getDisciplines() {
    //  return List.of();
    //}



    // Metode til at beregne betaling

    public double calcPayment() {

        //Hvis medlemmet betalt, returneres medlemskabsprisen, ellers returneres 0.
        return hasPaid ? membershipPrice : 0;
    }

    // Metode til at beregne hold ud fra fødselsdato





}









