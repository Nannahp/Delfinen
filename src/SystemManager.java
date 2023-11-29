import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SystemManager {
    UI ui = new UI();
    ArrayList<Member> members= new ArrayList<>();
    ArrayList<Coach> coaches= new ArrayList<>();
    //Temporary to test the Execute Program
    public boolean runMainMenu() {
       //initializeFiles(); // Only do once
       //testUpdatingFiles(); //test First so that some data is stored before testing startup
        exampleForInitializedData();
                ui.printMember(members.get(0));
                ui.printMember(members.get(1));
       //testLoadingAtStartup();
        return false;
    }

    public void testLoadingAtStartup(){
        loadArrays();
        Member testMember = members.get(0);
        System.out.println("Arrays have been loaded from file");
        System.out.println("Member 1 in array is: ID: "+ testMember.getMemberID() + "Name: "+ testMember.getFirstName() );
        Coach testCoach = coaches.get(0);
        System.out.println("Coach 1 in array : " + testCoach.getName());
    }

    //FOR TESTING! MISSING USER INPUTS SO SOME STUFF IS HARDCODED
    public void exampleForInitializedData(){
        FileHandler.clearFile("Members.csv"); //clears the files, so it's easier to assess if the test data is correct.
        FileHandler.clearFile("Coaches.csv");
        addCoach("Brian");
        addMember(1,1); //1 for member, 2 for competition member. Second int for member ID
        addMember(2,2);
        addMember(1,3);
    }


    public void testUpdatingFiles(){
        exampleForInitializedData();
        Member testMember = members.get(0);
        System.out.println("Members have " + members.size() + " members");
        System.out.println("Member 1 in array is: ID: "+ testMember.getMemberID() + "Name: "+ testMember.getFirstName() );
        System.out.println("Clearing the array");
        members.clear();
        System.out.println("Members have " + members.size() + " members");
        System.out.println("Loading the file into the array");
        loadMemberArray();
        System.out.println("Members have " + members.size() + " members");
        System.out.println("Member 1 in array is: ID: "+ testMember.getMemberID() + "Name: "+ testMember.getFirstName() );
        System.out.println("Changing the name to Andy:");
        testMember.setFirstName("Andy");
        System.out.println("Member 1 in array is: ID: "+ testMember.getMemberID() + "Name: "+ testMember.getFirstName() );
        System.out.println("Updating the file");
        updateMemberInfoInFile(testMember);
        System.out.println("clearing the arrays and loading it again to check if the name has been updated");
        updateMembers();
        loadMemberArray();
        System.out.println("Member 1 in array is: ID: "+ testMember.getMemberID() + "Name: "+ testMember.getFirstName() );
        System.out.println("checking that the coach is still attached to the competition member:");
        CompetitionMember compMember  = (CompetitionMember) members.get(1);
        System.out.println("Id : " + compMember.getMemberID() + "name: " + compMember.getFirstName());
        System.out.println(compMember.getCoach().getName());
        System.out.println("updating the name of the coach to Clara"); //Can't because we use the name to find the coach in the file
        coaches.get(0).setName("Clara");
        updateCoachInfoInFile(coaches.get(0));
        System.out.println("clearing the arrays and loading it again to check if the name has been updated");
        updateCoaches();
        loadCoachesArray();
        System.out.println(coaches.get(0).getName());
    }

    private Member searchForMember(int memberID) {
        try {
            for (Member member : members) {
                if (member.getMemberID() == memberID)
                    return member;
            }
            throw new IllegalArgumentException("Member with given ID not found");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Member createCompetitionMember(int id){
        String firstName = "Annie";
        String lastName = "Pederson";
        LocalDate date = LocalDate.of(2000,5,7);
        String gender = "F";
        boolean isActive = true;
        Coach coach = coaches.get(0); //Everything should be inputs
        Discipline[] disciplines = new Discipline[]{Discipline.BUTTERFLY};
        return new CompetitionMember(firstName, lastName, date, gender, isActive,coach,disciplines );
    }

    public Member createMember( int id){
        //Ask for input, shouldn't be hardcoded - for testing:
        String firstName = "Mia";
        String lastName = "Jensen";
        LocalDate date = LocalDate.of(2000,2,3);
        String gender = "F";
        boolean isActive = true;
        //ID should not be given it should be calculated, but I need it now for testing
        return new Member(firstName, lastName,date,gender,isActive);
    }

    public void addMember(int choice, int id){
        Member member = null;
    switch (choice){
        case 1 -> member = createMember(id); //ID should not be given should be calculated.
        case 2 -> member = createCompetitionMember(id);
    }
        members.add(member);
        FileHandler.appendObjectToFile("Members.csv", member);
        System.out.println("Member added");
    }

    public Coach createCoach(String name){
        return new Coach (name);
    }
    public void addCoach(String name){
        Coach coach = createCoach(name);
        coaches.add(coach);
        FileHandler.appendObjectToFile("Coaches.csv", coach);
    }

    public void updateMemberInfoInFile(Member member){
        FileHandler.updateObjectInFile("Members.csv", member);
    }
    public void updateCoachInfoInFile(Coach coach){
        FileHandler.updateObjectInFile("Coaches.csv", coach);
    }

    public void initializeFiles(){
        FileHandler.createFile("Members.csv");
        FileHandler.createFile("Coaches.csv");
    }

    public void updateMembers(){
        members.clear();
        loadMemberArray();
    }

    public void updateCoaches(){
        coaches.clear();
        loadCoachesArray();
    }

    public void loadArrays(){
        loadMemberArray();
        loadCoachesArray();
    }

    private void loadMemberArray(){
        List<Object> objects = FileHandler.loadObjectsFromFile("Members.csv");
        for (Object obj : objects) {
            members.add((Member) obj);
        }
    }

    private void loadCoachesArray(){
        List<Object> objects = FileHandler.loadObjectsFromFile("Coaches.csv");
        for (Object obj : objects) {
            coaches.add((Coach) obj);
        }
    }
}


