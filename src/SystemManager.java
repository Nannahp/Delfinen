import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SystemManager {
    ArrayList<Member> members= new ArrayList<>();
    ArrayList<Coach> coaches= new ArrayList<>();
    //Temporary to test the Execute Program
    public boolean runMainMenu() {
       //initializeFiles(); // Only do once
       addCoach("Charlie");
       addMember("Annie", LocalDate.of(1998,03,30), 'f', true, 1 );
       addCompetitionMember("Conrad", LocalDate.of(2000,4,4),'m',true,2,coaches.get(0),Discipline.BACKCRAWL, Discipline.CRAWL);
       System.out.println("Array member 1 = "+ members.get(0).getName());
       FileHandler.appendObjectToFile("testFil.csv", members.get(0));
       FileHandler.appendObjectToFile("Coaches.csv", coaches.get(0));
       FileHandler.appendObjectToFile("testFil.csv", members.get(1));
        System.out.println("members size before: " +members.size());
       members.clear();
        System.out.println("members is now empty: "+ members.size());
        updateArrays();
        System.out.println("members loaded, size:" + members.size());
        System.out.println("member 1 Id:" + members.get(0).getMemberID() + " name: " + members.get(0).getName());
        members.get(0).setName("Benny");
        FileHandler.updateObjectInFile("testFil.csv", members.get(0));
        System.out.println("member 1 Id:" + members.get(0).getMemberID() + " name: " + members.get(0).getName());




        return false;
    }

    //Søger efter en Member gennem ID ved at søge ArrayList og return Member
    private Member searchForMember(int memberID) {
        try {
            for (Member member : members) {
                if (member.getMemberID() == memberID)
                    return member; //lille m
            }
            throw new IllegalArgumentException("Member with given ID not found");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    //should they be here or in FileHandler?
    public void createFile(String fileName, String... strings){
        FileHandler.createFile(fileName);
        ArrayList<String> titles = createStringArrayList(strings);
        FileHandler.createTitlesForFile(fileName, titles);
    }

    public  ArrayList<String> createStringArrayList(String... strings) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (String str : strings) {
            arrayList.add(str);
        }
        return arrayList;
    }
    public void initializeFiles(){
        createFile("Members.csv","ID","Name","Birthday","Active","PaymentStatus","MembershipPrice","Team","TrainingScores", "CompetitionScores" );
        createFile("Coaches.csv","Name");
    }

    public void updateArrays(){
      updateMemberArray();
      updateCoachesArray();
    }
    private void updateMemberArray(){
        List<Object> objects = FileHandler.loadObjectsFromFile("testFil.csv");
        for (Object obj : objects) {
            members.add((Member) obj);
        }
    }
    private void updateCoachesArray(){
        List<Object> objects = FileHandler.loadObjectsFromFile("Coaches.csv");
        for (Object obj : objects) {
            coaches.add((Coach) obj);
        }
    }



///FOR TESTING, FOR NOW ///



    public void addMember(String name, LocalDate date, char gender, boolean isActive, int id){
        Member member = new Member(name,date,gender,isActive, id);
        members.add(member);
        FileHandler.appendMemberToFile("Members.csv", member); //double code, split into separate method

    }

    public void addCompetitionMember(String name, LocalDate date, char gender, boolean isActive, int id, Coach coach, Discipline... disciplines){
        CompetitionMember competitionMember = new CompetitionMember(name, date, gender, isActive,id, coach,disciplines );
        members.add(competitionMember);
        FileHandler.appendMemberToFile("Members.csv", competitionMember);

    }

    public void addCoach(String name){
        Coach coach  = new Coach (name);
        coaches.add(coach);
        FileHandler.appendCoachToFile("Coaches.csv", coach);

    }
    public void testEditTrainingScore(){
        CompetitionMember member = (CompetitionMember) members.get(1);
        member.addTrainingScore(new TrainingScore(50, LocalDate.of(2023,11,27),Discipline.BUTTERFLY));
        FileHandler.updateMember("Members.csv", member);

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