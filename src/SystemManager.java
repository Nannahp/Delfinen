import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SystemManager {
    UI ui = new UI();
    boolean systemRunning = true;
    private int nextMemberId;
    ArrayList<Member> members = new ArrayList<>();
    ArrayList<Coach> coaches = new ArrayList<>();


    public void runProgram(){
        readyArraysAtStartup();
        ui.printWelcomeMessage();
        while (systemRunning) {
            runMainMenu();
        }
    }

    // ----- MENUS -----
    public void runMainMenu() {
        Menu menu =ui.buildMainMenu();
        int choice = menu.menuInputHandler();
        switch (choice) {
            case 1 -> runCashierMenu();
            case 2 -> runManagerMenu();
            case 3 -> runCoachMenu();
            case 4 -> quitProgram();
        }
    }


    // ---- CASHIER ----
    public void runCashierMenu() {
        boolean exitMenu = false;

          while (!exitMenu) {
              Menu menu = ui.buildCashierMenu();
              int choice = menu.menuInputHandler();
              switch (choice) {
                  case 1 -> showPaymentStatusForAllMembers();
                  case 2 -> registerPaymentStatus();
                  case 3 -> exitMenu = true;
                  default -> UI.printText("\n Invalid choice. Please select a valid option.", ConsoleColor.RED);
              }
          }
    }

    //NOT DONE
    public void showPaymentStatusForAllMembers() {
        if (members.isEmpty()) {
            UI.printText("\n No members found.", ConsoleColor.RED);
        } else {
             ui.showPaymentStatusForAllMembers(members);
            }
        }


    public void registerPaymentStatus(){
        ui.printMembers(members);
        UI.printText("\n",ConsoleColor.RESET);
        Member member = getMember();
        UI.printText("\n Payment received? (y/n): ",ConsoleColor.RESET);
        member.setPaymentStatus(ui.getBooleanInput());
        UI.printText(" Payment status updated\n", ConsoleColor.RESET);
        updateMemberInfoInFile(member);
    }


    // ---- MANAGER -----
    public void runManagerMenu() {
        boolean exitMenu = false;
        while (!exitMenu) {
            Menu menu =  ui.buildManagerMenu();
            int choice = menu.menuInputHandler();
            switch (choice) {
                case 1 -> addMember();
                case 2 -> seeMemberInformation();
                case 3 -> editMember();
                case 4 -> deleteMember();
                case 5 -> addCoach();
                case 6 -> exitMenu = true;
            }
        }
    }

    //Method to add a member to the system
    public void addMember(){
        boolean isACompetitionMember = isMemberACompetitionMember();
        Member member;
        if (isACompetitionMember) {
            member = createCompetitionMember();}
        else member = createMember();

        members.add(member);
        FileHandler.appendObjectToFile("Members.txt", member);
        UI.printText("\n Member added", ConsoleColor.GREEN);
        ui.printMember(member);
    }

    //If the member is a competitionMember this method is used
    public CompetitionMember createCompetitionMember() {
        CompetitionMember member;
        String firstName = getMemberNameInput("first-name");
        String lastName = getMemberNameInput("last-name");
        LocalDate date = getMemberDateInput();
        String gender = ui.getGenderInput();
        boolean isActive = getMemberBooleanInput();

        UI.printText("\n CompetitionMembers need to be assigned a coach \n", ConsoleColor.RESET);
        Coach coach = runChooseCoachMenu();
        UI.printText(""" 
                CompetitionMembers need to be assigned a discipline.\s
                (Crawl, BackCrawl, BreastStroke, Butterfly, Medley)
                                                                """, ConsoleColor.RESET);
        Discipline[] disciplines = chooseDisciplinesForMember();
        member = new CompetitionMember(firstName, lastName, date, gender, isActive, coach, disciplines);
        member.setMemberID(nextMemberId++);

        coach.addMemberToCoach( member);
        updateCoachInfoInFile(coach);

        return member;
    }

    //Creates the member with a unique ID based on the information you get
    public Member createMember() {
        Member member;
        String firstName = getMemberNameInput("first-name");
        String lastName = getMemberNameInput("last-name");
        LocalDate date = getMemberDateInput();
        String gender = ui.getGenderInput();
        boolean isActive = getMemberBooleanInput();
        member=new Member(firstName, lastName, date, gender, isActive);
        member.setMemberID(nextMemberId++);
        return member;
    }


    //Methods to get information for member
    private String getMemberNameInput(String prompt) {
        UI.printText("\n Please enter the " + prompt + " of the member:", ConsoleColor.RESET);
        return ui.getStringInput();
    }

    private LocalDate getMemberDateInput() {
        UI.printText("\n Please enter the birthdate of the member:",ConsoleColor.RESET);
        return ui.getLocalDateInput();
    }

    private boolean getMemberBooleanInput() {
        UI.printText("\n Is the member active? (y/n):",ConsoleColor.RESET);
        return ui.getBooleanInput();
    }

    //Method for CompetitionMember, choose as many disciplines as you want
    public Discipline[] chooseDisciplinesForMember(){
        ArrayList<Discipline> disciplines = new ArrayList<>();
        disciplines.add(askForDiscipline());
        boolean needToAddMoreDisciplines;
        do{
            UI.printText("\n Are there additional disciplines? (y/n)",ConsoleColor.RESET);
            needToAddMoreDisciplines = ui.getBooleanInput();
            if (needToAddMoreDisciplines){
                Discipline discipline = askForDiscipline();
                disciplines.add(discipline);
            }
        }
        while (needToAddMoreDisciplines);
        return disciplines.toArray(new Discipline[disciplines.size()]);
    }

    //Methods for CompetitionMember
    public Discipline askForDiscipline(){
        UI.printText("\n Please enter a discipline:",ConsoleColor.RESET);
        return ui.getDiscipline();
    }


    public boolean isMemberACompetitionMember(){
        UI.printText("\n Is the member a CompetitionMember? (y/n)",ConsoleColor.RESET);
        return ui.getBooleanInput();
    }

    //Shows information of all members
    public void seeMemberInformation(){
        ui.printMembers(members);
        UI.printText("\n", ConsoleColor.RESET);
        Member member = getMember();
        ui.printMember(member);
    }

    //Used to delete a member and update ArrayList afterward
    public void deleteMember(){
        ui.printMembers(members);
        UI.printText("\n", ConsoleColor.RESET);
        Member member = getMember();
        removeMemberFromFile(member);
        updateMembers();
        if (member instanceof  CompetitionMember){
            deleteCompetitionMember((CompetitionMember) member);
        }
        UI.printText("\n Member: " + member.getFirstName() + " " + member.getLastName() + " deleted",ConsoleColor.GREEN);
    }

    //Specifically for CompetitionMember
    public void deleteCompetitionMember(CompetitionMember member){
        Coach coach = member.getCoach();
        coach.removeMemberFromCoachLists(member);
        updateCoachInfoInFile(coach);
        updateCoaches();
    }

    //Deletes Member by disciplin and removes it from the coach lists too
    public void deleteMemberByDiscipline(CompetitionMember member, Discipline discipline){
        Coach coach = member.getCoach();
        coach.removeMemberByDiscipline(member, discipline);
        updateCoachInfoInFile(coach);
        updateCoaches();
    }

    //Edits the member and updates the file
    public void editMember(){
        ui.printMembers(members);
        UI.printText("\n", ConsoleColor.RESET);
        Member member = getMember();
        runEditMenu(member);
        updateMemberInfoInFile(member);
        updateMembers();
    }

    //Edit menu
    public  void runEditMenu(Member member) {
        boolean exitMenu = false;
        while (!exitMenu) {
            Menu menu = ui.buildEditMenu();
            int choice = menu.menuInputHandler();
            switch (choice) {
                case 1 -> editName(member);
                case 2 -> editActiveStatus(member);
                case 3 -> removeDiscipline( member);
                case 4 -> addDiscipline( member);
                case 5 -> exitMenu = true;
            }
        }
        if ((member instanceof CompetitionMember)){
            updateCoachInfo( ((CompetitionMember) member).getCoach());
        }
        }

    //Add disciplin through edit
public void addDiscipline(Member member){
        if( member instanceof CompetitionMember){
        UI.printText("\n " +member.getFirstName() + " is active in:" ,ConsoleColor.RESET);
        ui.printDisciplines(((CompetitionMember) member).getDisciplines());
        UI.printText("\n Which discipline would you like to add?", ConsoleColor.RESET);
        Discipline discipline = ui.getDiscipline();
        ((CompetitionMember) member).addDisciplines(discipline);
        Coach coach = ((CompetitionMember) member).getCoach();
        coach.checkCompetitionMemberTeam((CompetitionMember) member);
}
    else UI.printText("\n Member is not a competition member", ConsoleColor.RED);}

    //Remove disciplin through edit
public void removeDiscipline(Member member){
    if( member instanceof CompetitionMember) {
        UI.printText("\n Which discipline would you like to delete?", ConsoleColor.RESET);
        ui.printDisciplines(((CompetitionMember) member).getDisciplines());
        Discipline discipline = ui.getDiscipline();
        ((CompetitionMember) member).deleteDiscipline(discipline);
        deleteMemberByDiscipline((CompetitionMember) member, discipline);
    }
    else UI.printText("\n Member is not a competition member", ConsoleColor.RED);
}

    //Edit names
    public void editName(Member member){
        member.setFirstName(getMemberNameInput("first-name"));
        member.setLastName(getMemberNameInput("last-name"));
    }

    //Edit activity status
    public void editActiveStatus(Member member){
        member.setIsActive(getMemberBooleanInput());
    }


    public void addCoach(){
        UI.printText("\n What is the name of the coach? Please write here: ",ConsoleColor.RESET);
        String name = ui.getStringInput();
        Coach coach = createCoach(name);
        coaches.add(coach);
        FileHandler.appendObjectToFile("Coaches.txt", coach);
        UI.printText("\n Coach added",ConsoleColor.GREEN);
    }

    //create a coach with Coach constructor
    public Coach createCoach(String name){
        return new Coach (name);
    }

   // ---- COACH ----
   //Method to 'login' as a specific coach in system to only see relevant information
    public Coach runChooseCoachMenu() {
        Coach coach = null;
        ui.buildChooseCoachMenu(coaches);
        UI.printText("\n Which coach do you want? ",ConsoleColor.RESET);
        while (coach == null) {
            int choice = UI.getIntInput();
            if (choice >= 1 && choice <= coaches.size()) {
                coach = coaches.get(choice - 1);
            } else {
                UI.printText("\n Invalid choice. Please select a valid option:",ConsoleColor.RED);
            }
        }
        return coach;
    }

    public void runCoachMenu() {
        Coach coach = runChooseCoachMenu();
       // ui.printText(coach.toString()); for testing
        boolean exitMenu = false;
        while (!exitMenu) {
            Menu menu = ui.buildCoachMenu();
            int choice = menu.menuInputHandler();
            switch (choice) {
                case 1 -> runSeeTop5Menu(coach);
                case 2 -> registerTrainingScore(coach);
                case 3 -> registerCompetitionScore(coach);
                case 4 -> exitMenu = true;
            }
        }
    }

    //Register trainingscore based on specific coach
    public void registerTrainingScore(Coach coach){
        UI.printText("\n Which member you would like to add a training score to?  Please write here:\n", ConsoleColor.RESET);
        ui.printMembers(coach.getAllMembers());
        Member member = getMember();
        if (member instanceof CompetitionMember){
        coach.addTrainingScoreToMember((CompetitionMember) member, createTrainingScore());
            updateMemberInfoInFile(member);
            coach.updateMemberInCoach((CompetitionMember) member);
            updateCoachInfoInFile(coach);}
        else UI.printText("\n The member ID you have entered is not a competition member", ConsoleColor.RED);

    }
    //Asks questions and creates the trainingscore to be registered
    public TrainingScore createTrainingScore(){
        UI.printText("\n Please enter discipline: ",ConsoleColor.RESET);
        Discipline discipline = ui.getDiscipline();
        UI.printText("\n Please enter the training-time (in seconds): ",ConsoleColor.RESET);
        int time = UI.getIntInput();
        LocalDate date = LocalDate.now();
        return  new TrainingScore(time, date,discipline);
    }
    //Shows top 5 members for the coach based of disciplin
    public void runSeeTop5Menu(Coach coach) {
          Menu menu =  ui.buildSeeTop5Menu();
        int choice = menu.menuInputHandler();
        switch (choice) {
            case 1 -> seeTop5(coach,Discipline.CRAWL);
            case 2 -> seeTop5(coach,Discipline.BACKCRAWL);
            case 3 -> seeTop5(coach,Discipline.BREASTSTROKE);
            case 4 -> seeTop5(coach,Discipline.BUTTERFLY);
            case 5 -> seeTop5(coach,Discipline.MEDLEY);
        }
    }

    //Prints top 5 for a specific discipline
    public void seeTop5(Coach coach, Discipline discipline){
        ArrayList seniors = sortTop5Members(coach.getSeniorList(discipline),discipline);
        ArrayList juniors = sortTop5Members(coach.getJuniorList(discipline),discipline);
        ui.printTop5List(seniors, "Seniors", discipline);
        ui.printTop5List(juniors, "Juniors",discipline);
    }

    //Sorts top 5, so that it ony shows 5 member sorted by best to worst
    public ArrayList<CompetitionMember> sortTop5Members(ArrayList<CompetitionMember> memberList, Discipline discipline) {
        CompetitionMemberDisciplineScoreComparator comparator = new CompetitionMemberDisciplineScoreComparator(discipline);
        Collections.sort(memberList, comparator);
        return new ArrayList<>(memberList.subList(0, Math.min(memberList.size(), 5)));
        // Returns top five members in the list, but doesn't crash if there are fewer than 5 members
    }

    //Registers competitionScore by coach
    public void registerCompetitionScore(Coach coach){
        UI.printText("\n Which member would you like to add a competition score to? Please write here: \n", ConsoleColor.RESET);
        ui.printMembers(coach.getAllMembers());
        Member member = getMember();
        if (member instanceof CompetitionMember){
            coach.addCompetitionScoreToMember((CompetitionMember) member, createCompetitionScore());
            updateMemberInfoInFile(member);
            coach.updateMemberInCoach((CompetitionMember) member);
            updateCoachInfoInFile(coach);}
        else UI.printText("\n The member ID you have entered is not a competition member \n", ConsoleColor.RED);
    }

    //Asks questions for competitionScore to be registeres
    public CompetitionScore createCompetitionScore(){
        UI.printText("\n Please enter discipline: ",ConsoleColor.RESET);
        Discipline discipline = ui.getDiscipline();
        UI.printText("\n Please enter the competition name: ",ConsoleColor.RESET);
        String competitionName = ui.getStringInput();
        UI.printText("\n Please enter placement: ",ConsoleColor.RESET);
        int placement = UI.getIntInput();
        UI.printText("\n Please enter the competition-time (in seconds): ",ConsoleColor.RESET);
        int time = UI.getIntInput();
        return new CompetitionScore(competitionName, placement, time, discipline);
    }

    // ---- SYSTEM METHODS----

    //Searches for member by ID
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

    //Returns a specific member
    public Member getMember() {
        Member member = null;
        while (member == null) {  //not tested!
            UI.printText(" \n Please enter the MemberId of the member you would like to access:",ConsoleColor.RESET);
            int memberId = UI.getIntInput();
            member = searchForMember(memberId);
        }
        return member;
    }

    //Method that gives unique memberID to every Member
    public void updateNextMemberID(){
        if(members.isEmpty()){
            nextMemberId = 1;}  //ensures that memberID can't be 0
        else  nextMemberId = Collections.max(members, Comparator.comparing(Member::getMemberID)).getMemberID() + 1;
    }                        //ensures that the ID can't be the same if we delete a member and add a new one


    //Maybe not needed  - But might if we add Delete Coach in edit
    public Coach searchForCoach(){
        Coach coachToReturn = null;
        while(coachToReturn==null){
            UI.printText(" \nPlease enter the name of the Coach you want",ConsoleColor.RESET);
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
    //Updates the coach attributes every time there is an edit for a member
    private void updateCoachInfo(Coach coach) {
        updateCoachInfoInFile(coach);
        updateCoaches();
    }
    private void updateMemberInfo(Member member) {
        updateMemberInfoInFile(member);
        updateMembers();
    }

    //Updates and loads for the files to always be up-to-date
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

    /*
    public void printCoaches(){
        for (Coach coach: coaches) {
            UI.printText(coach.getName(),ConsoleColor.WHITE);
        }
    }
    */

     //Methods to updates files
    public void updateMemberInfoInFile(Member member){
        FileHandler.modifyObjectInFile("Members.txt", member, true);
    }
    public void updateCoachInfoInFile(Coach coach){
        FileHandler.modifyObjectInFile("Coaches.txt", coach, true);
    }
    public void removeMemberFromFile(Member member){
        FileHandler.modifyObjectInFile("Members.txt", member,false);
    }

    //Method to create files if the files should get lost
    public void initializeFiles(){
        FileHandler.createFile("Members.txt");
        FileHandler.createFile("Coaches.txt");
    }

    public void readyArraysAtStartup(){
        loadArrays();
        updateNextMemberID();
        if (members.isEmpty() || coaches.isEmpty()){
            initializeData();       //If the arrays are empty at startup then clear the files and add some default members
            initializeTrainingScores();
            updateArrays();
        }
    }

    //Hardcoded data for testing
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


