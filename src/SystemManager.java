import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SystemManager {
    ArrayList<Member> members= new ArrayList<>();
    ArrayList<Coach> coaches= new ArrayList<>();
    //Temporary to test the Execute Program
    public boolean runMainMenu() {
       //initializeFiles(); // Only do once
        exampleRunMenu();
        testLoadingFromFile();

        return false;
    }



    //FOR TESTING! MISSING USER INPUTS SO SOME STUFF IS HARDCODED
    public void exampleRunMenu(){
        loadArrays(); // reads the files and updates the arrays
        addCoach("Brian");
        addMember(1,1); //1 for member, 2 for competition member. Second int for member ID
        addMember(2,2);
        addMember(1,3);

    }





    public void testLoadingFromFile(){
        Member testMember = members.get(0);
        System.out.println("Members have " + members.size() + " members");
        System.out.println("Member 1 in array is: ID: "+ testMember.getMemberID() + "Name: "+ testMember.getName() );
        System.out.println("Clearing the array");
        members.clear();
        System.out.println("Members have " + members.size() + " members");
        System.out.println("Loading the file into the array");
        loadMemberArray();
        System.out.println("Members have " + members.size() + " members");
        System.out.println("Member 1 in array is: ID: "+ testMember.getMemberID() + "Name: "+ testMember.getName() );
        System.out.println("Changing the name to Andy:");
        testMember.setName("Andy");
        System.out.println("Member 1 in array is: ID: "+ testMember.getMemberID() + "Name: "+ testMember.getName() );
        System.out.println("Updating the file");
        updateMemberInfoInFile(testMember);
        System.out.println("clearing the arrays and loading it again to check if the name has been updated");
        updateMembers();
        loadMemberArray();
        System.out.println("Member 1 in array is: ID: "+ testMember.getMemberID() + "Name: "+ testMember.getName() );


    }

    public Member createCompetitionMember(int id){
        String name = "Annie";
        LocalDate date = LocalDate.of(2000,5,7);
        char gender = 'f';
        boolean isActive = true;
        Coach coach = coaches.get(0); //Everything should be inputs
        Discipline[] disciplines = new Discipline[]{Discipline.BUTTERFLY};
        CompetitionMember competitionMember = new CompetitionMember(name, date, gender, isActive,id, coach,disciplines );
        return competitionMember;

    }
    public Member createMember( int id){
        //Ask for input, shouldn't be hardcoded - for testing:
        String name = "Mia";
        LocalDate date = LocalDate.of(2000,2,3);
        char gender = 'f';
        boolean isActive = true;
        //Id should not be given it should be calculated, but I need it now for testing
        Member member = new Member(name,date,gender,isActive, id);
        return member;
    }

    public void addMember(int choice, int id){
        Member member = null;
    switch (choice){
        case 1 -> member = createMember(id); //Id should not be given should be calculated.
        case 2 -> member = createCompetitionMember(id);
    }
        members.add(member);
        FileHandler.appendObjectToFile("Members.csv", member);

    }
    public Coach createCoach(String name){
        Coach coach  = new Coach (name);
        return coach;
    }
    public void addCoach(String name){
        Coach coach = createCoach(name);
        coaches.add(coach);
        FileHandler.appendObjectToFile("Coaches.csv", coach);

    }
    /*
    public void testEditTrainingScore(){
        CompetitionMember member = (CompetitionMember) members.get(1);
        member.addTrainingScore(new TrainingScore(50, LocalDate.of(2023,11,27),Discipline.BUTTERFLY));
        FileHandler.updateMember("Members.csv", member);

    }
*/

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




/*
    public void addCompetition(DummyCompetitionMember member, String name, int time){
        DummyCompetitionScore newCompetitionScore =new DummyCompetitionScore(name, time);
        member.addCompetitionScore(newCompetitionScore);
        FileHandler.appendCompetitionScoreForMemberToFile("testFil.csv", member);
    }
    //For testing

    public void testAddCompetition(){
        addCompetition((DummyCompetitionMember) memberList.get(1),"Grønlandskamp", 8);

    }
    public void testEditTrainingScore(){
        DummyCompetitionMember member1 = (DummyCompetitionMember) memberList.get(1);
        member1.removeFromList(0);
        member1.addTolist(new DummyTrainingScore("Butterfly",50));
        FileHandler.editTrainingScores("testFil.csv", member1);
    }
    public void printMemberArray(){
        for (DummyMember member:memberList) {
            System.out.println(member);
        }
    }
}
*/