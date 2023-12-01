import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        // printMembers();//for testing - to see that it works
        // printCoaches(); //for testing - to see that it works
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
            ui.printText("Not an option", ConsoleColor.WHITE);
            choice = ui.getMenuChoiceFromUserInput();
        }
        return choice;
    }

    // ---- CASHIER ----
public void runCashierMenu() {
        boolean exitMenu = false;
          while (!exitMenu) {
              ui.buildCashierMenu();
              int choice = menuInputHandler(3);
              switch (choice) {
                  case 1 -> showPaymentStatusForAllMembers();
                  case 2 -> registerPaymentStatus();
                  case 3 -> exitMenu = true;
                  default -> ui.printText("Invalid choice. Please select a valid option.", ConsoleColor.RED);


              }
          }
    }

    public void showPaymentStatusForAllMembers() {
        if (members.isEmpty()) {
            ui.printText("No members found.", ConsoleColor.RED);
        } else {
            ui.printText("Payment Status for all members:" , ConsoleColor.RED);
            for (Member member: members) {
                ui.printText("Member ID: " + member.getMemberID() + "-Payment Status:" + member.getPaymentStatus(), ConsoleColor.RED);
            }
        }
    }

    public void registerPaymentStatus(){
        printMembers();
        Member member = getMember();
        ui.printText("Payment received? (y/n)",ConsoleColor.WHITE);
        member.setPaymentStatus(ui.getBooleanInput());
        updateMemberInfoInFile(member);
    }


    // ---- MANAGER -----
    public void runManagerMenu() {
        boolean exitMenu = false;
        while (!exitMenu) {
            ui.buildManagerMenu();
            int choice = menuInputHandler(6);
            switch (choice) {
                case 1 -> addMember();
                case 2 -> seeMemberInformation();
                case 3 -> ui.printText("edit member info is coming soon", ConsoleColor.WHITE);
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
        ui.printText("Member added", ConsoleColor.GREEN);
        ui.printMember(member);
    }


    public CompetitionMember createCompetitionMember() {
        CompetitionMember member;
        String firstName = getMemberStringInput("first-name");
        String lastName = getMemberStringInput("last-name");
        LocalDate date = getMemberDateInput();
        String gender = getGenderInput();
        boolean isActive = getMemberBooleanInput();

        ui.printText("CompetitionMembers need to be assigned a coach ", ConsoleColor.WHITE);
        Coach coach = runChooseCoachMenu();
        ui.printText("""
                CompetitionMembers need to be assigned a discipline.\s
                (Crawl, BackCrawl, BreastStroke, Butterfly, Medley)
                                                                """, ConsoleColor.WHITE);
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

    //SHOULD BE IN UI?
    private String getGenderInput() {
        ui.printText("Please enter the gender of the member (F/M)" ,ConsoleColor.WHITE);
        String gender = null;
        while (gender == null) {
            String input = ui.getStringInput();
            if (input.equalsIgnoreCase("f") || input.equalsIgnoreCase("m")) {
                gender = input;
            } else ui.printText("Please enter either \"f\" or \"m\"", ConsoleColor.RED);
        }
        return gender;
    }

    private String getMemberStringInput(String prompt) {
        ui.printText("Please enter the " + prompt + " of the member:", ConsoleColor.WHITE);
        return ui.getStringInput();
    }

    private LocalDate getMemberDateInput() {
        ui.printText("Please enter the birthdate of the member:",ConsoleColor.WHITE);
        return ui.getLocalDateInput();
    }

    private boolean getMemberBooleanInput() {
        ui.printText("Is the member active? (y/n)",ConsoleColor.WHITE);
        return ui.getBooleanInput();
    }


    public Discipline[] chooseDisciplinesForMember(){
        ArrayList<Discipline> disciplines = new ArrayList<>();
        disciplines.add(askForDiscipline());
        boolean needToAddMoreDisciplines;
        do{
            ui.printText("Are there additional disciplines? (y/n)",ConsoleColor.WHITE);
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
        ui.printText("Please enter a discipline:",ConsoleColor.WHITE);
        return ui.getDiscipline();
    }


    public boolean isMemberACompetitionMember(){
        ui.printText("Is the member a CompetitionMember? (y/n)",ConsoleColor.WHITE);
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
        ui.printText("Member: " + member.getFirstName() + " " + member.getLastName() + " deleted",ConsoleColor.GREEN);
    }
    public void deleteCompetitionMember(CompetitionMember member){
        Coach coach = member.getCoach();
        coach.removeMemberFromCoachLists(member);
        updateCoachInfoInFile(coach);
        updateCoaches();
    }

    public void deleteMemberByDiscipline(CompetitionMember member, Discipline discipline){
        Coach coach = member.getCoach();
        coach.removeMemberByDiscipline(member, discipline);
        updateCoachInfoInFile(coach);
        updateCoaches();
    }


    public void addCoach(){
        ui.printText("What is the name of the coach?",ConsoleColor.WHITE);
        String name = ui.getStringInput();
        Coach coach = createCoach(name);
        coaches.add(coach);
        FileHandler.appendObjectToFile("Coaches.txt", coach);
        ui.printText("Coach added",ConsoleColor.GREEN);

    }
    public Coach createCoach(String name){
        return new Coach (name);
    }

   // ---- COACH ----

    public Coach runChooseCoachMenu() {
        Coach coach = null;
        ui.buildChooseCoachMenu(coaches);
        ui.printText("Which coach do you want? ",ConsoleColor.WHITE);
        while (coach == null) {
            int choice = ui.getIntInput();
            if (choice >= 1 && choice <= coaches.size()) {
                coach = coaches.get(choice - 1);
            } else {
                ui.printText("Invalid choice. Please select a valid option.",ConsoleColor.RED);
            }
        }
        return coach;
    }

    public void runCoachMenu() {
        Coach coach = runChooseCoachMenu();
       // ui.printText(coach.toString()); for testing
        boolean exitMenu = false;
        while (!exitMenu) {
            ui.buildCoachMenu();
            int choice = menuInputHandler(4);
            switch (choice) {
                case 1 -> runSeeTop5Menu(coach);
                case 2 -> registerTrainingScore(coach);
                case 3 -> registerCompetitionScore();
                case 4 -> exitMenu = true;
            }
        }

    }

    public void registerTrainingScore(Coach coach){
        ui.printText("Which member you would like to add a training score to?", ConsoleColor.WHITE);
        ui.printListOfMembers(coach.getAllMembers());
        Member member = getMember();
        if (member instanceof CompetitionMember){
        coach.addTrainingScoreToMember((CompetitionMember) member, createTrainingScore());
        coach.updateMemberInCoach((CompetitionMember) member);
        updateMemberInfoInFile(member);
        updateCoachInfoInFile(coach);     }
        else ui.printText("The member ID you have entered is not a competition member", ConsoleColor.RED);

    }

    public TrainingScore createTrainingScore(){
        ui.printText("Please enter discipline",ConsoleColor.WHITE);
        Discipline discipline = ui.getDiscipline();
        ui.printText("Please enter the training-time (in seconds)",ConsoleColor.WHITE);
        int time = ui.getIntInput();
        LocalDate date = LocalDate.now();
        return  new TrainingScore(time, date,discipline);
    }
    public void runSeeTop5Menu(Coach coach) {
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
        ui.printText("\nSeniors in " + discipline.label + " top 5:\n",ConsoleColor.WHITE);
        ui.printTop5List(seniors, discipline);
        ui.printText("\nJuniors in " + discipline.label + " top 5:\n",ConsoleColor.WHITE);
        ui.printTop5List(juniors, discipline);
    }

    public ArrayList<CompetitionMember> sortTop5Members(ArrayList<CompetitionMember> memberList, Discipline discipline) {
        CompetitionMemberDisciplineScoreComparator comparator = new CompetitionMemberDisciplineScoreComparator(discipline);
        Collections.sort(memberList, comparator);
        return new ArrayList<>(memberList.subList(0, Math.min(memberList.size(), 5)));
        // Returns top five members in the list, but doesn't crash if there are fewer than 5 members
    }

    public void printCoachList(Coach coach) {
        ArrayList<CompetitionMember> seniorMedleyList = coach.getSeniorList(Discipline.MEDLEY);
        for (CompetitionMember member : seniorMedleyList) {
            System.out.println(member.getFirstName()); //Det virker, men her er det specifik seniorMedley
        }
    }

    public void registerCompetitionScore() {
        getMember();
        ui.printText("coming soon ;)",ConsoleColor.RED);
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
            ui.printText("Please enter the MemberId of the member you would like to access:",ConsoleColor.WHITE);
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
            ui.printText("Please enter the name of the Coach you want",ConsoleColor.WHITE);
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
            ui.printText("MemberID: " + member.getMemberID() + " : " + member.getFirstName(),ConsoleColor.WHITE);
        }
    }

    public void printCoaches(){
        for (Coach coach: coaches) {
            ui.printText(coach.getName(),ConsoleColor.WHITE);
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
        addTestCompetitionMember("Peter", "Parker", "m", 1988,coaches.get(0));
        addTestCompetitionMember("Miles", "Morales","m", 2010,coaches.get(0));
        addTestCompetitionMember("Felicia", "Hardy", "f", 1990,coaches.get(0));
        addTestCompetitionMember("Gwen", "Stacy", "f", 1991, coaches.get(1));
        addTestMember("MJ", "Watson", "f", 1988);
        addTestMember("Otto", "Octavious", "m", 1960);

    }

    public void initializeTrainingScores(){
        loadMemberArray();
        CompetitionMember peter = (CompetitionMember) members.get(0);
        CompetitionMember miles = (CompetitionMember) members.get(1);
        CompetitionMember felicia = (CompetitionMember) members.get(2);
        CompetitionMember gwen = (CompetitionMember) members.get(3);
       // peter.addTestTrainingScore(new TrainingScore(38,LocalDate.now(),Discipline.CRAWL));
       // peter.addTestTrainingScore(new TrainingScore(120,LocalDate.now(),Discipline.BUTTERFLY));
       // miles.addTestTrainingScore(new TrainingScore(44, LocalDate.now(), Discipline.CRAWL));
       // miles.addTestTrainingScore(new TrainingScore(62,LocalDate.now(),Discipline.BUTTERFLY));
       // felicia.addTestTrainingScore(new TrainingScore(50,LocalDate.now(),Discipline.CRAWL));
       // felicia.addTestTrainingScore(new TrainingScore(66,LocalDate.now(),Discipline.BUTTERFLY));

        coaches.get(0).addTrainingScoreToMember(peter, new TrainingScore(80,LocalDate.now(),Discipline.CRAWL));
        coaches.get(0).addTrainingScoreToMember(peter, new TrainingScore(133,LocalDate.now(),Discipline.BUTTERFLY));
        coaches.get(0).addTrainingScoreToMember(felicia, new TrainingScore(120,LocalDate.now(),Discipline.CRAWL));
        coaches.get(0).addTrainingScoreToMember(felicia, new TrainingScore(120,LocalDate.now(),Discipline.CRAWL));
        coaches.get(0).addTrainingScoreToMember(felicia, new TrainingScore(111,LocalDate.now(),Discipline.CRAWL));
        coaches.get(0).addTrainingScoreToMember(felicia, new TrainingScore(38,LocalDate.now(),Discipline.BACKCRAWL));
        coaches.get(0).addTrainingScoreToMember(felicia, new TrainingScore(100,LocalDate.now(),Discipline.MEDLEY));
        coaches.get(0).addTrainingScoreToMember(felicia, new TrainingScore(100,LocalDate.now(),Discipline.BUTTERFLY));
        coaches.get(0).addTrainingScoreToMember(miles, new TrainingScore(55,LocalDate.now(),Discipline.CRAWL));
        coaches.get(0).addTrainingScoreToMember(miles, new TrainingScore(20,LocalDate.now(),Discipline.BUTTERFLY));
        coaches.get(1).addTrainingScoreToMember(gwen, new TrainingScore(120,LocalDate.now(),Discipline.CRAWL));
        coaches.get(1).addTrainingScoreToMember(gwen, new TrainingScore(143,LocalDate.now(),Discipline.BUTTERFLY));
        updateMemberInfoInFile(peter);
        updateMemberInfoInFile(miles);
        updateMemberInfoInFile(felicia);
        updateMemberInfoInFile(gwen);
        coaches.get(0).updateMemberInCoach(peter);
        coaches.get(0).updateMemberInCoach(miles);
        coaches.get(0).updateMemberInCoach(felicia);
        coaches.get(1).updateMemberInCoach(gwen);
        updateCoachInfoInFile(coaches.get(0));
        updateCoachInfoInFile(coaches.get(1));
    }

    public void addTestCoach(String name){
        Coach coach = createCoach(name);
        FileHandler.appendObjectToFile("Coaches.txt", coach);

    }

    public void addTestCompetitionMember(String firstName, String lastName, String gender, int year, Coach coach){
        LocalDate date = LocalDate.of(year,5,7);
        boolean isActive = true;
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


