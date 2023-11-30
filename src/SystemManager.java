import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SystemManager {
    UI ui = new UI();
    boolean systemRunning = true;
    private int nextMemberId;
    private int nextCoachId;
    ArrayList<Member> members = new ArrayList<>();
    ArrayList<Coach> coaches = new ArrayList<>();

    public void runMainMenu() {
        //initializeFiles(); //Maybe needs to run if you don't have the files
        readyArraysAtStartup();
         printMembers();//for testing - to see that it works
         printCoaches(); //for testing - to see that it works
        while (systemRunning) {
            ui.buildMainMenu();
            sendFromMainMenu();
        }

    }

    // ----- MENUS -----

    public void sendFromMainMenu() {
        int choice = menuInputHandler(4);
        switch (choice) {
            case 1 -> runCashierMenu();
            case 2 -> runManagerMenu();
            case 3 -> runCoachMenu();
            case 4 -> quitProgram();
        }
    }

    public int menuInputHandler(int upperLimitOfMenuItems) { //maybe should be in UI er menu
        int choice = ui.getMenuChoiceFromUserInput();
        while (choice < 0 || upperLimitOfMenuItems < choice) {
            ui.printText("Not an option");
            choice = ui.getMenuChoiceFromUserInput();
        }
        return choice;
    }

    // ---- CASHIER ----

    public void runCashierMenu() {
        ui.buildCashierMenu();
        ui.printText("show members and their paymentStatus");
    }

    // ---- MANAGER -----
    public void runManagerMenu() {
        boolean exitMenu = false;
        while (!exitMenu) {
            printMembers();
            ui.buildManagerMenu();
            int choice = menuInputHandler(6);
            switch (choice) {
                case 1 -> addMember();
                case 2 -> seeMemberInformation();
                case 3 -> ui.printText("edit member info is coming soon");
                case 4 -> deleteMember();
                case 5 -> addCoach();
                case 6 -> exitMenu = true;
            }
        }
    }
    public void addMember(){
        boolean isACompetitionMember = isMemberACompetitionMember();
        Member member;
        if (isACompetitionMember) {
            member = createCompetitionMember();}
        else member = createMember();

        members.add(member);
        FileHandler.appendObjectToFile("Members.txt", member);
        ui.printText("Member added");
        ui.printMember(member);
    }


    public CompetitionMember createCompetitionMember() {
        CompetitionMember member;
        String firstName = getMemberStringInput("first-name");
        String lastName = getMemberStringInput("last-name");
        LocalDate date = getMemberDateInput();
        String gender = getGenderInput();
        boolean isActive = getMemberBooleanInput();

        ui.printText("CompetitionMembers need to be assigned a coach ");
        Coach coach = runChooseCoachMenu();
        ui.printText("""
                CompetitionMembers need to be assigned a discipline.\s
                (Crawl, BackCrawl, BreastStroke, Butterfly, Medley)
                                                                """);
        Discipline[] disciplines = chooseDisciplinesForMember();
        member = new CompetitionMember(firstName, lastName, date, gender, isActive, coach, disciplines);
        member.setMemberID(nextMemberId++);

        coach.addMemberToCoach( member);
        updateCoachInfoInFile(coach);

        return member;
    }

    public Member createMember() {
        Member member;
        String firstName = getMemberStringInput("first-name");
        String lastName = getMemberStringInput("last-name");
        LocalDate date = getMemberDateInput();
        String gender = getGenderInput();
        boolean isActive = getMemberBooleanInput();
        member=new Member(firstName, lastName, date, gender, isActive);
        member.setMemberID(nextMemberId++);
        return member;
    }
    private String getGenderInput() {
        ui.printText("Please enter the gender of the member (F/M)");
        String gender = null;
        while (gender == null) {
            String input = ui.getStringInput();
            if (input.equalsIgnoreCase("f") || input.equalsIgnoreCase("m")) {
                gender = input;
            } else ui.printText("Please enter either \"f\" or \"m\"");
        }
        return gender;
    }

    private String getMemberStringInput(String prompt) {
        ui.printText("Please enter the " + prompt + " of the member:");
        return ui.getStringInput();
    }

    private LocalDate getMemberDateInput() {
        ui.printText("Please enter the birthdate of the member:");
        return ui.getLocalDateInput();
    }

    private boolean getMemberBooleanInput() {
        ui.printText("Is the member active? (y/n)");
        return ui.getBooleanInput();
    }


    public Discipline[] chooseDisciplinesForMember(){
        ArrayList<Discipline> disciplines = new ArrayList<>();
        disciplines.add(askForDiscipline());
        boolean needToAddMoreDisciplines;
        do{
            ui.printText("Are there additional disciplines? (y/n)");
            needToAddMoreDisciplines = ui.getBooleanInput();
            if (needToAddMoreDisciplines){
                Discipline discipline = askForDiscipline();
                disciplines.add(discipline);
            }
        }
        while (needToAddMoreDisciplines);
        Discipline[] disciplinesArray = disciplines.toArray(new Discipline[disciplines.size()]);
        return disciplinesArray;
    }


    public Discipline askForDiscipline(){
        ui.printText("Please enter a discipline:");
        return ui.getDiscipline();
    }


    public boolean isMemberACompetitionMember(){
        ui.printText("Is the member a CompetitionMember? (y/n)");
        return ui.getBooleanInput();
    }
    public void seeMemberInformation(){
        Member member = getMember();
        ui.printMember(member);
    }

    public void deleteMember(){
        Member member = getMember();
        removeMemberFromFile(member);
        updateMembers(); //Updates the membersArrayList
        if (member instanceof  CompetitionMember){
            deleteCompetitionMember((CompetitionMember) member);
        }
        ui.printText("Member: " + member.getFirstName() + " " + member.getLastName() + " deleted");
    }
    public void deleteCompetitionMember(CompetitionMember member){
        Coach coach = member.getCoach();
        coach.removeMemberFromCoachLists(member);
        updateCoachInfoInFile(coach);
        updateCoaches();
    }



    public void addCoach(){
        ui.printText("What is the name of the coach?");
        String name = ui.getStringInput();
        Coach coach = createCoach(name);
        coaches.add(coach);
        FileHandler.appendObjectToFile("Coaches.txt", coach);
        ui.printText("Coach added");

    }
    public Coach createCoach(String name){
        return new Coach (name);
    }

   // ---- COACH ----

    public Coach runChooseCoachMenu() {
        Coach coach = null;
        ui.buildChooseCoachMenu(coaches);
        ui.printText("Which coach do you want? ");
        while (coach == null) {
            int choice = ui.getIntInput();
            if (choice >= 1 && choice <= coaches.size()) {
                coach = coaches.get(choice - 1);
            } else {
                System.out.println("Invalid choice. Please select a valid option.");
            }
        }
        return coach;
    }

    public void runCoachMenu() {
        Coach coach = runChooseCoachMenu();
        ui.printText(coach.toString());
        boolean exitMenu = false;
        while (!exitMenu) {
            ui.buildCoachMenu();
            int choice = menuInputHandler(4);
            switch (choice) {
                case 1 -> runSeeTop5Menu(coach);
                case 2 -> ui.printText("coming soon");//registerTrainingScore();
                case 3 -> registerCompetitionScore();
                case 4 -> exitMenu = true;
            }
        }

    }

    public void runSeeTop5Menu(Coach coach) {
        //need coach to see top 5
        ui.buildSeeTop5Menu();
        int choice = menuInputHandler(5);
        switch (choice) {
            case 1 -> seeTop5(coach,Discipline.CRAWL);
            case 2 -> seeTop5(coach,Discipline.BACKCRAWL);
            case 3 -> seeTop5(coach,Discipline.BREASTSTROKE);
            case 4 -> seeTop5(coach,Discipline.BUTTERFLY);
            case 5 -> seeTop5(coach,Discipline.MEDLEY);
        }
    }

    //For testing
    public void seeTop5(Coach coach, Discipline discipline){
        ArrayList seniors = sortTop5Members(coach.getSeniorList(discipline),discipline);
        ArrayList juniors = sortTop5Members(coach.getJuniorList(discipline),discipline);
        ui.printText("\nSeniors in " + discipline.label + " top 5:\n");
        ui.printTop5List(seniors, discipline);
        ui.printText("\nJuniors in " + discipline.label + " top 5:\n");
        ui.printTop5List(juniors, discipline);
    }

    public ArrayList<CompetitionMember> sortTop5Members(ArrayList<CompetitionMember> memberList, Discipline discipline) {
        CompetitionMemberDisciplineScoreComparator comparator = new CompetitionMemberDisciplineScoreComparator(discipline);
        Collections.sort(memberList, comparator);
        return new ArrayList<>(memberList.subList(0, Math.min(memberList.size(), 5)));
        // Returns top five members in the list, but doesn't crash if there are fewer than 5 members
    }

//for testing:
    public void checkTrainingScores(){
        CompetitionMember member = (CompetitionMember) members.get(0);
        System.out.println("Member info");
        System.out.println(member.getTrainingScores().get(0).toString());
        System.out.println("Coach info");
        System.out.println(coaches.get(0).getMemberNamesInList(coaches.get(0).getSeniorList(Discipline.CRAWL)));
        System.out.println(coaches.get(0).getSeniorList(Discipline.CRAWL).get(0));
        CompetitionMember member2 = coaches.get(0).getSeniorList(Discipline.CRAWL).get(0);
        System.out.println(member2.getTrainingScores().get(0).toString());
        System.out.println(coaches.get(0).getSeniorList(Discipline.CRAWL).get(0).getTrainingScores().get(0).toString());
    }
    public void printCoachList(Coach coach) {
        ArrayList<CompetitionMember> seniorMedleyList = coach.getSeniorList(Discipline.MEDLEY);
        for (CompetitionMember member : seniorMedleyList) {
            System.out.println(member.getFirstName()); //Det virker, men her er det specifik seniorMedley
        }
    }
    public void registerCompetitionScore() {
        getMember();
        ui.printText("coming soon ;)");
    }


    // ---- SYSTEM METHODS----

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
    public Member getMember() {
        Member member = null;
        while (member == null) {  //not tested!
            ui.printText("enter id");
            ui.printText("Please enter the MemberId of the member you would like to access:");
            int memberId = ui.getIntInput();
            member = searchForMember(memberId);
        }
        return member;
    }

    public void updateNextMemberID(){
        if(members.size() ==0){
            nextMemberId = 1;}  //ensures that memberID can't be 0
        else  nextMemberId = members.size() +1;
    }

    //Maybe not needed
    public Coach searchForCoach(){
        Coach coachToReturn = null;
        while(coachToReturn==null){
            ui.printText("Please enter the name of the Coach you want");
            String name = ui.getStringInput();
            for (Coach coach: coaches) {
                if (coach.getName().equals(name)){
                    coachToReturn = coach;
                }
            }
        }return coachToReturn;
    }

    public void quitProgram() {
        systemRunning = false;
    }

    // --- STARTUP / ARRAYS -----

    public void updateMembers(){
        members.clear();
        loadMemberArray();
    }
    public void updateCoaches(){
        coaches.clear();
        loadCoachesArray();
    }

    public void updateArrays(){
        updateCoaches();
        updateMembers();
    }

    public void loadArrays(){
        loadMemberArray();
        loadCoachesArray();
    }

    private void loadMemberArray(){
        List<Object> objects = FileHandler.loadObjectsFromFile("Members.txt");
        for (Object obj : objects) {
            members.add((Member) obj);
        }
    }

    private void loadCoachesArray(){
        List<Object> objects = FileHandler.loadObjectsFromFile("Coaches.txt");
        for (Object obj : objects) {
            coaches.add((Coach) obj);
        }
    }

    public void printMembers(){
        for (Member member: members) {
            ui.printText(member.getMemberID() + " : " + member.getFirstName());
        }
    }

    public void printCoaches(){
        for (Coach coach: coaches) {
            ui.printText(coach.getName());
        }
    }


    public void updateMemberInfoInFile(Member member){
        FileHandler.modifyObjectInFile("Members.txt", member, true);
    }
    public void updateCoachInfoInFile(Coach coach){
        FileHandler.modifyObjectInFile("Coaches.txt", coach, true);
    }
    public void removeMemberFromFile(Member member){
        FileHandler.modifyObjectInFile("Members.txt", member,false);
    }


    public void initializeFiles(){
        FileHandler.createFile("Members.txt");
        FileHandler.createFile("Coaches.txt");
    }

    public void readyArraysAtStartup(){
        loadArrays();
        updateNextMemberID();
        if (members.size() ==0 || coaches.size() ==0){
            initializeData();       //If the arrays are empty at startup then clear the files and add some default members
            initializeTrainingScores();
            updateArrays();
        }
    }

    public void initializeData(){
        FileHandler.clearFile("Members.txt"); //clears the files, so it's easier to assess if the data is correct.
        FileHandler.clearFile("Coaches.txt");
        addTestCoach("Henry");
        addTestCoach("Maria");
        loadCoachesArray();
        addTestCompetitionMember("Peter", "Parker", "m", 1988);
        addTestCompetitionMember("Miles", "Morales","m", 2010);
        addTestCompetitionMember("Felicia", "Hardy", "f", 1990);
        addTestMember("MJ", "Watson", "f", 1988);
        addTestMember("Otto", "Octavious", "m", 1960);

    }

    public void initializeTrainingScores(){
        loadMemberArray();
        CompetitionMember peter = (CompetitionMember) members.get(0);
        CompetitionMember miles = (CompetitionMember) members.get(1);
        CompetitionMember felicia = (CompetitionMember) members.get(2);
        peter.addTestTrainingScore(new TrainingScore(38,LocalDate.now(),Discipline.CRAWL));
        peter.addTestTrainingScore(new TrainingScore(120,LocalDate.now(),Discipline.BUTTERFLY));
        miles.addTestTrainingScore(new TrainingScore(44, LocalDate.now(), Discipline.CRAWL));
        miles.addTestTrainingScore(new TrainingScore(62,LocalDate.now(),Discipline.BUTTERFLY));
        felicia.addTestTrainingScore(new TrainingScore(50,LocalDate.now(),Discipline.CRAWL));
        felicia.addTestTrainingScore(new TrainingScore(66,LocalDate.now(),Discipline.BUTTERFLY));
        updateMemberInfoInFile(peter);
        updateMemberInfoInFile(miles);
        updateMemberInfoInFile(felicia);
        coaches.get(0).updateMemberInCoach(peter);
        coaches.get(0).updateMemberInCoach(miles);
        coaches.get(0).updateMemberInCoach(felicia);
        updateCoachInfoInFile(coaches.get(0));
        updateCoachInfoInFile(coaches.get(1));
    }

    public void addTestCoach(String name){
        Coach coach = createCoach(name);
        FileHandler.appendObjectToFile("Coaches.txt", coach);

    }

    public void addTestCompetitionMember(String firstName, String lastName, String gender, int year){
        LocalDate date = LocalDate.of(year,5,7);
        boolean isActive = true;
        Coach coach = coaches.get(0);
        Discipline[] disciplines = new Discipline[]{Discipline.BUTTERFLY,Discipline.CRAWL};
        CompetitionMember member = new CompetitionMember(firstName, lastName, date, gender, isActive,coach,disciplines );
        member.setMemberID(nextMemberId++);
        FileHandler.appendObjectToFile("Members.txt", member);
        coach.addMemberToCoach(member);
        updateCoachInfoInFile(coach);
    }

    public void addTestMember(String firstName, String lastName,String gender, int year){
        LocalDate date = LocalDate.of(year,5,7);
        boolean isActive = true;
        Member member = new Member(firstName, lastName, date, gender, isActive );
        member.setMemberID(nextMemberId++);
        FileHandler.appendObjectToFile("Members.txt", member);
    }
}


