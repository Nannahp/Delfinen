import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SystemManager {
    boolean systemRunning = true;
    Scanner scanner = new Scanner(System.in); //will be removed
    UI ui  = new UI();
    ArrayList<Member> members= new ArrayList<>();
    ArrayList<Coach> coaches= new ArrayList<>();



    public void runMainMenu() {
        //initializeData();
        loadArrays();
        printMembers();//for testing - to see that it works
        printCoaches(); //for testing - to see that it works
        while(systemRunning){
            ui.buildMainMenu();
            sendFromMainMenu();}

    }

    public void sendFromMainMenu(){
        int choice = scanner.nextInt();
        scanner.nextLine(); //scannerbug
        switch (choice){
            case 1 -> runCashierMenu();
            case 2 -> runManagerMenu();
            case 3 -> runCoachMenu();
            case 4 -> quitProgram();
        }
    }
    public void quitProgram(){
        systemRunning = false;
    }

    public void  runCashierMenu(){
        ui.buildCashierMenu();
        System.out.println("show members and their paymentStatus");
    }

    public void runManagerMenu(){
        boolean exitMenu = false;
        while(!exitMenu){

        ui.buildManagerMenu();
        int choice = scanner.nextInt();
        scanner.nextLine(); //scannerbug
        switch (choice){
            case 1 -> addMember();
            case 2 -> System.out.println("edit member info is coming soon");
            case 3 -> System.out.println("delete member is coming soon");
            case 4 -> addCoach();
            case 5 -> exitMenu = true;
        }
        }
    }


    public void runCoachMenu(){
        Coach coach = runChooseCoachMenu();
        boolean exitMenu = false;
        while(!exitMenu){
            ui.buildCoachMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); //scannerbug
            switch (choice){
                case 1 -> runSeeTop5Menu(coach);
                case 2 -> System.out.println("coming soon");//registerTrainingScore();
                case 3 -> registerCompetitionScore();
                case 4 -> exitMenu = true;
            }
        }
    }

    public void runSeeTop5Menu(Coach coach){
        //need coach to see top 5
        boolean exitMenu = false;
        while(!exitMenu){

            ui.buildSeeTop5Menu();
            int choice = scanner.nextInt();
            scanner.nextLine(); //scannerbug
            switch (choice){
                case 1 -> System.out.println("see top 5 of Crawl");
                case 2 -> System.out.println("see top 5 of BackCrawl");
                case 3 -> System.out.println("see top 5 of BreastStroke");
                case 4 -> System.out.println("see top 5 of Butterfly");
                case 5 -> System.out.println("see top 5 of Medley");
                case 6 -> exitMenu = true;
            }
        }
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

    public Member getMember(){
        Member member = null;
        while(member ==null){  //not tested!
        System.out.println("enter id");
        int memberId = scanner.nextInt();
        scanner.nextLine(); //scannerbug
        member =  searchForMember(memberId);}
        return member;
    }

    //Not working currently, needs CompetitionMember methods
    public void registerTrainingScore(){
        CompetitionMember member = (CompetitionMember) getMember();
        System.out.println("what was the registered time?");
        int time = scanner.nextInt();
        scanner.nextLine(); //scannerbug
        LocalDate date = LocalDate.of(2000,4,6);//dummy
        Discipline discipline = Discipline.BUTTERFLY; //dummy
        member.addTrainingScore(new TrainingScore(time,date, discipline)); //needs to handle limited disciplines
        FileHandler.updateObjectInFile("Members.csv",member);
    }

    public void registerCompetitionScore(){
        getMember();
        System.out.println("coming soon ;)");
    }


    public Coach runChooseCoachMenu() { //OBS! Does not work dynamically
        Coach coach = null;
            ui.buildChooseCoachMenu(coaches);
            int choice = scanner.nextInt();
            scanner.nextLine(); //scannerbug
            switch (choice) {
                case 1 -> coach = coaches.get(0);
                case 2 -> coach = coaches.get(1);
        }//needs catch so that it doesn't return a null coach
        return  coach;
    }

        //needs user input
    public Member createCompetitionMember(int id){
        String name = "Annie";
        LocalDate date = LocalDate.of(2000,5,7);
        char gender = 'f';
        boolean isActive = true;
        Coach coach = coaches.get(0); //Everything should be inputs
        Discipline[] disciplines = new Discipline[]{Discipline.BUTTERFLY};
        return new CompetitionMember(name, date, gender, isActive,id, coach,disciplines );


    }
    public Member createMember( int id){
        //Ask for input, shouldn't be hardcoded - for testing:
        String name = "Mia";
        LocalDate date = LocalDate.of(2000,2,3);
        char gender = 'f';
        boolean isActive = true;
        //ID should not be given it should be calculated, but I need it now for testing
        return new Member(name,date,gender,isActive, id);
    }

        //Needs proper user input
    public int chooseMemberOrCompetitionMember(){
        System.out.println("choose 1 for member, choose 2 for competitionMember");
        int choice  = scanner.nextInt(); //Only for now, will be edited once we have ui
        scanner.nextLine(); //scannerbug
        return choice;
    }


    public void addMember(){
        int choice = chooseMemberOrCompetitionMember();
        System.out.println("enter id");
        int id = scanner.nextInt(); //only for now, will be deleted once memberID is calculated
        scanner.nextLine(); //scannerbug
        //All of the above will be removed or edited
        Member member = null;
    switch (choice){
        case 1 -> member = createMember(id); //ID should not be given should be calculated.
        case 2 -> member = createCompetitionMember(id);
    }
        members.add(member);
        FileHandler.appendObjectToFile("Members.csv", member);
        System.out.println("Member added");
        printMembers(); // for testing - to see if it works
    }


    public Coach createCoach(String name){
        return new Coach (name);
    }
    public void addCoach(){
        System.out.println("What name?");
        String name = scanner.nextLine();
        Coach coach = createCoach(name);
        coaches.add(coach);
        FileHandler.appendObjectToFile("Coaches.csv", coach);
        printCoaches(); // for testing - to see if it works

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

    public void printMembers(){
        for (Member member: members) {
            System.out.println(member.getMemberID() + " : " + member.getName());
        }
    }

    public void printCoaches(){
        for (Coach coach: coaches) {
            System.out.println(coach.getName());
        }
    }



    //FOR TESTING


    public void testLoadingAtStartup(){
        loadArrays();
        Member testMember = members.get(0);
        System.out.println("Arrays have been loaded from file");
        System.out.println("Member 1 in array is: ID: "+ testMember.getMemberID() + "Name: "+ testMember.getName() );
        Coach testCoach = coaches.get(0);
        System.out.println("Coach 1 in array : " + testCoach.getName());


    }

    //FOR TESTING! MISSING USER INPUTS SO SOME STUFF IS HARDCODED
    public void initializeData(){
        FileHandler.clearFile("Members.csv"); //clears the files, so it's easier to assess if the test data is correct.
        FileHandler.clearFile("Coaches.csv");
        addCoach();
        addCoach();
        addMember();
        addMember();
        addMember();

    }


    public void testUpdatingFiles(){
        initializeData();
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
        System.out.println("checking that the coach is still attached to the competition member:");
        CompetitionMember compMember  = (CompetitionMember) members.get(1);
        System.out.println("Id : " + compMember.getMemberID() + "name: " + compMember.getName());
        System.out.println(compMember.getCoach().getName());
        System.out.println("updating the name of the coach to Clara"); //Can't because we use the name to find the coach in the file
        coaches.get(0).setName("Clara");
        updateCoachInfoInFile(coaches.get(0));
        System.out.println("clearing the arrays and loading it again to check if the name has been updated");
        updateCoaches();
        loadCoachesArray();
        System.out.println(coaches.get(0).getName());


    }

}


