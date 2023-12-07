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
        UI.printText(" Payment status updated\n", ConsoleColor.GREEN);
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
        member = new Member(firstName, lastName, date, gender, isActive);
        member.setMemberID(nextMemberId++);
        return member;
    }


    //Methods to get information for member
    private String getMemberNameInput(String prompt) {
        UI.printText("\n Please enter the " + prompt + " of the member: ", ConsoleColor.RESET);
        return ui.getStringInput();
    }

    private LocalDate getMemberDateInput() {
        UI.printText("\n Please enter the birthdate of the member: ",ConsoleColor.RESET);
        return ui.getLocalDateInput();
    }

    private boolean getMemberBooleanInput() {
        UI.printText("\n Is the member active? (y/n): ",ConsoleColor.RESET);
        return ui.getBooleanInput();
    }

    //Method for CompetitionMember, choose as many disciplines as you want
    public Discipline[] chooseDisciplinesForMember(){
        ArrayList<Discipline> disciplines = new ArrayList<>();
        disciplines.add(askForDiscipline());
        boolean needToAddMoreDisciplines;
        do{
            UI.printText("\n Are there additional disciplines? (y/n): ",ConsoleColor.RESET);
            needToAddMoreDisciplines = ui.getBooleanInput();
            if (needToAddMoreDisciplines){

                Discipline discipline = askForDiscipline();
                if (disciplines.contains(discipline)){
                    UI.printText(" \n Member already active in this discipline \n", ConsoleColor.RED);
                } else {
                    disciplines.add(discipline);
                }
            }
        }
        while (needToAddMoreDisciplines && disciplines.size()<5);
        return disciplines.toArray(new Discipline[disciplines.size()]);
    }

    //Methods for CompetitionMember
    public Discipline askForDiscipline(){
        UI.printText("\n Please enter a discipline: ",ConsoleColor.RESET);
        return ui.getDiscipline();
    }


    public boolean isMemberACompetitionMember(){
        UI.printText("\n Is the member a CompetitionMember? (y/n): ",ConsoleColor.RESET);
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
        UI.printText("\n Are you sure? (y/n) ", ConsoleColor.RESET);

        if (ui.getStringInput().equalsIgnoreCase("y")) {
            removeMemberFromFile(member);
            updateMembers();
            if (member instanceof  CompetitionMember){
                deleteCompetitionMember((CompetitionMember) member);
            }
            UI.printText("\n " + member.getFirstName() + " " + member.getLastName() + " is no longer a member.",ConsoleColor.RED);
        } else {
            UI.printText("\n " + member.getFirstName() + " " + member.getLastName() + " is still a member.",ConsoleColor.GREEN);
            runManagerMenu();
        }

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
        if(member instanceof CompetitionMember){
            runEditCompetitionMemberMenu((CompetitionMember) member);
        }else runEditMemberMenu(member);
        updateMemberInfoInFile(member);
        updateMembers();
    }

    public void runEditMemberMenu(Member member){
        boolean exitMenu = false;
        while (!exitMenu) {
            Menu menu = ui.buildEditMemberMenu();
            int choice = menu.menuInputHandler();
            switch (choice) {
                case 1 -> editName(member);
                case 2 -> editActiveStatus(member);
                case 3 -> exitMenu = true;
            }
        }
    }

    //Edit menu
    public  void runEditCompetitionMemberMenu(CompetitionMember member) {
        boolean exitMenu = false;
        while (!exitMenu) {
            Menu menu = ui.buildEditCompetitionMemberMenu();
            int choice = menu.menuInputHandler();
            switch (choice) {
                case 1 -> editName(member);
                case 2 -> editActiveStatus(member);
                case 3 -> removeDiscipline( member);
                case 4 -> addDiscipline( member);
                case 5 -> exitMenu = true;
            }
            updateCoachInfo(member.getCoach());
        }
    }

    //Add disciplin through edit
    public void addDiscipline(CompetitionMember member) {
            UI.printText("\n " + member.getFirstName() + " is active in: \n", ConsoleColor.RESET);
            ui.printDisciplines((member).getDisciplines());
            UI.printText("\n Which discipline would you like to add?\n Please write here: ", ConsoleColor.RESET);
            Discipline discipline = ui.getDiscipline();
            (member).addDisciplines(discipline);
            Coach coach = ( member).getCoach();
            coach.checkCompetitionMemberTeam(member);
        }


    //Remove disciplin through edit
    public void removeDiscipline(CompetitionMember member){
            if(!( member).getDisciplines().isEmpty()){
            UI.printText("\n Which discipline would you like to delete?\n Please write here:", ConsoleColor.RESET);
            ui.printDisciplines((member).getDisciplines());
            Discipline discipline = ui.getDiscipline();
            (member).deleteDiscipline(discipline);
            deleteMemberByDiscipline( member, discipline);
        }
            else UI.printText(" This member is not active in any disciplines\n", ConsoleColor.RED);
        }

    //Edit names
    public void editName(Member member){
        member.setFirstName(getMemberNameInput("first-name"));
        member.setLastName(getMemberNameInput("last-name"));
    }

    //Edit activity status, adds an active member to a coach or removes an inactive member.
    public void editActiveStatus(Member member){
        member.setIsActive(getMemberBooleanInput());
        if (member instanceof CompetitionMember) {
            Coach coach = ((CompetitionMember) member).getCoach();
            if (member.getIsActive()) {
                coach.addMemberToCoach((CompetitionMember) member);
            } else {
                coach.removeMemberFromCoachLists((CompetitionMember) member);
            }
        }
        UI.printText("\n Status updated",ConsoleColor.GREEN);
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
        if (!coach.getAllMembers().isEmpty()) {
            UI.printText("\n Which member you would like to add a training score to?\n ", ConsoleColor.RESET);
            ui.printMembers(coach.getAllMembers());
            Member member = getMember();
            if (coach.getAllMembers().contains(member)){
             if (member instanceof CompetitionMember) {
                 System.out.print(" This member has these disciplines: \n");
                 ui.printDisciplines(((CompetitionMember) member).getDisciplines());
                 Discipline discipline = checkIfMemberHasDiscipline((CompetitionMember) member);
                 if (discipline != null){
                coach.addTrainingScoreToMember((CompetitionMember) member, createTrainingScore(discipline));
                updateMemberInfoInFile(member);
                coach.updateMemberInCoach((CompetitionMember) member);
                updateCoachInfoInFile(coach);}
             } else UI.printText("\n The member ID you have entered is not a competition member!\n", ConsoleColor.RED);
            } else  {
                UI.printText("\n This member is not assigned to this coach!\n", ConsoleColor.RED);
                registerTrainingScore(coach);
            }
        }
        else UI.printText("\n No members assigned to this coach\n", ConsoleColor.RED);
    }

    //Asks questions and creates the trainingscore to be registered
    public TrainingScore createTrainingScore(Discipline discipline){
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
    public void registerCompetitionScore(Coach coach) {
        if (!coach.getAllMembers().isEmpty()) {
            UI.printText("\n Which member would you like to add a competition score to?\n\n", ConsoleColor.RESET);
            ui.printMembers(coach.getAllMembers());
            Member member = getMember();
            if (coach.getAllMembers().contains(member)){
                if (member instanceof CompetitionMember) {
                    System.out.print(" This member has these disciplines: \n");
                    ui.printDisciplines(((CompetitionMember) member).getDisciplines());
                    Discipline discipline = checkIfMemberHasDiscipline((CompetitionMember) member);
                    if (discipline != null){
                    coach.addCompetitionScoreToMember((CompetitionMember) member, createCompetitionScore((CompetitionMember) member,discipline));
                    updateMemberInfoInFile(member);
                    coach.updateMemberInCoach((CompetitionMember) member);
                    updateCoachInfoInFile(coach);}
                } else UI.printText("\n The member ID you have entered is not a competition member!", ConsoleColor.RED);
            } else {
                UI.printText("\n The member is not assigned to this coach!\n", ConsoleColor.RED);
                registerCompetitionScore(coach);
            }
        } else UI.printText("\n No members assigned to this coach\n", ConsoleColor.RED);
    }

    public Discipline checkIfMemberHasDiscipline(CompetitionMember member){
        UI.printText("\n Please enter discipline: ",ConsoleColor.RESET);
        Discipline discipline = ui.getDiscipline();
         if (member.doesMemberHaveDiscipline(discipline)){
             return discipline;}
        else {
            UI.printText("\n Member is not active in this discipline\n", ConsoleColor.RED);
        return null;}
    }

    //Asks questions for competitionScore to be registeres
    public CompetitionScore createCompetitionScore(CompetitionMember member, Discipline discipline){
        UI.printText("\n Please enter the competition name: ",ConsoleColor.RESET);
        String competitionName = ui.getStringWithNumbersInput();
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
            throw new IllegalArgumentException("\n Member with given ID not found!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    //Returns a specific member
    public Member getMember() {
        Member member = null;
        while (member == null) {
            UI.printText(" \n Please enter the MemberId of the member you\n would like to access: ",ConsoleColor.RESET);
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


    public void quitProgram() {
        systemRunning = false;
    }

    // --- STARTUP / ARRAYS -----
    //Updates the coach attributes every time there is an edit for a member
    private void updateCoachInfo(Coach coach) {
        updateCoachInfoInFile(coach);
        updateCoaches();
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


    public void readyArraysAtStartup(){
        FileHandler.createFile("Members.txt");
        FileHandler.createFile("Coaches.txt");
        loadArrays();
        updateNextMemberID();
        if (members.isEmpty() || coaches.isEmpty()){
            initializeData();       //If the arrays are empty at startup then clear the files and add some default members
            initializePaymentData();
            initializeTrainingScores();
            updateArrays();
        }
    }

    //Hardcoded data for testing
    public void initializeData(){
        FileHandler.clearFile("Members.txt"); //clears the files, so it's easier to assess if the data is correct.
        FileHandler.clearFile("Coaches.txt");

        addTestCoach("Tony Stark"); //0
        addTestCoach("Dr. Otto Octavius"); //1
        addTestCoach("Norman Osborn"); //2
        addTestCoach("Ben Parker"); //3

        loadCoachesArray();

        addTestCompetitionMember("Peter", "Parker", "m", 1988,coaches.get(0)); //0
        addTestCompetitionMember("Miles", "Morales","m", 2010,coaches.get(0)); //1
        addTestCompetitionMember("Felicia", "Hardy", "f", 1990,coaches.get(2)); //2
        addTestCompetitionMember("Gwen", "Stacy", "f", 1991, coaches.get(1)); //3
        addTestCompetitionMember("Harry", "Osborn", "m", 1988, coaches.get(2)); //4
        addTestCompetitionMember("May", "Parker", "f", 1963, coaches.get(3)); //5
        addTestCompetitionMember("Mayday", "Parker", "f", 2018, coaches.get(3)); //6
        addTestCompetitionMember("Benjy", "Parker", "m", 2020, coaches.get(3)); //7

        addTestMember("Mary-Jane", "Watson", "f", 1988); //8
        addTestMember("J. Jonah", "Jameson", "m", 1951); //9
        addTestMember("Flash", "Thompson", "m", 1987); //10

    }

    public void initializePaymentData() {
        loadMemberArray();

        Member felicia = members.get(2);
        Member harry = members.get(4);
        Member jameson = members.get(9);
        Member flash = members.get(10);

        felicia.setPaymentStatus(false);
        harry.setPaymentStatus(false);
        jameson.setPaymentStatus(false);
        flash.setPaymentStatus(false);

        updateMemberInfoInFile(felicia);
        updateMemberInfoInFile(harry);
        updateMemberInfoInFile(jameson);
        updateMemberInfoInFile(flash);
    }



    public void initializeTrainingScores(){
        loadMemberArray();

        CompetitionMember peter = (CompetitionMember) members.get(0);
        CompetitionMember miles = (CompetitionMember) members.get(1);
        CompetitionMember felicia = (CompetitionMember) members.get(2);
        CompetitionMember gwen = (CompetitionMember) members.get(3);
        CompetitionMember harry = (CompetitionMember) members.get(4);
        CompetitionMember may = (CompetitionMember) members.get(5);
        CompetitionMember mayday = (CompetitionMember) members.get(6);
        CompetitionMember benjy = (CompetitionMember) members.get(7);

        coaches.get(0).addTrainingScoreToMember(peter, new TrainingScore(80,LocalDate.now(),Discipline.CRAWL));
        coaches.get(0).addTrainingScoreToMember(peter, new TrainingScore(133,LocalDate.now(),Discipline.BUTTERFLY));

        coaches.get(2).addTrainingScoreToMember(felicia, new TrainingScore(120,LocalDate.now(),Discipline.CRAWL));
        coaches.get(2).addTrainingScoreToMember(felicia, new TrainingScore(100,LocalDate.now(),Discipline.BUTTERFLY));

        coaches.get(0).addTrainingScoreToMember(miles, new TrainingScore(55,LocalDate.now(),Discipline.CRAWL));
        coaches.get(0).addTrainingScoreToMember(miles, new TrainingScore(20,LocalDate.now(),Discipline.BUTTERFLY));

        coaches.get(1).addTrainingScoreToMember(gwen, new TrainingScore(120,LocalDate.now(),Discipline.CRAWL));
        coaches.get(1).addTrainingScoreToMember(gwen, new TrainingScore(143,LocalDate.now(),Discipline.BUTTERFLY));

        coaches.get(2).addTrainingScoreToMember(harry, new TrainingScore(91,LocalDate.now(),Discipline.CRAWL));
        coaches.get(2).addTrainingScoreToMember(harry, new TrainingScore(200,LocalDate.now(),Discipline.BUTTERFLY));

        coaches.get(3).addTrainingScoreToMember(may, new TrainingScore(98,LocalDate.now(),Discipline.CRAWL));

        coaches.get(3).addTrainingScoreToMember(mayday, new TrainingScore(47,LocalDate.now(),Discipline.BUTTERFLY));
        coaches.get(3).addTrainingScoreToMember(mayday, new TrainingScore(100,LocalDate.now(),Discipline.CRAWL));

        coaches.get(3).addTrainingScoreToMember(benjy, new TrainingScore(148,LocalDate.now(),Discipline.CRAWL));
        coaches.get(3).addTrainingScoreToMember(benjy, new TrainingScore(126,LocalDate.now(),Discipline.BUTTERFLY));

        updateMemberInfoInFile(peter);
        updateMemberInfoInFile(miles);
        updateMemberInfoInFile(felicia);
        updateMemberInfoInFile(gwen);
        updateMemberInfoInFile(harry);
        updateMemberInfoInFile(may);
        updateMemberInfoInFile(mayday);
        updateMemberInfoInFile(benjy);

        coaches.get(0).updateMemberInCoach(peter);
        coaches.get(0).updateMemberInCoach(miles);
        coaches.get(2).updateMemberInCoach(felicia);
        coaches.get(1).updateMemberInCoach(gwen);
        coaches.get(2).updateMemberInCoach(harry);
        coaches.get(3).updateMemberInCoach(may);
        coaches.get(3).updateMemberInCoach(mayday);
        coaches.get(3).updateMemberInCoach(benjy);


        updateCoachInfoInFile(coaches.get(0));
        updateCoachInfoInFile(coaches.get(1));
        updateCoachInfoInFile(coaches.get(2));
        updateCoachInfoInFile(coaches.get(3));
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
    //Dummyfiles removed
}


