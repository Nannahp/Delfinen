
import java.io.Serializable;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UI implements  Serializable {
    private static Scanner in = new Scanner(System.in);


    // Getting inout methods
    public String getStringInput() {
        String stringInput = in.nextLine();

        while (!isString(stringInput)) {
            printText(" You didn't use a string. Try again: ", ConsoleColor.RED);
            stringInput = in.nextLine();
        }
        return stringInput;
    }

    public static int getIntInput() {
        int intInput = 0;
        while (intInput == 0){
            try {
                intInput = in.nextInt();
                in.nextLine(); //Scanner bug
            }
            catch (InputMismatchException e){
                printText(" Input not recognized, please enter a number: ", ConsoleColor.RED);
                in.next();
            }
        }
        return intInput;
    }

    public boolean getBooleanInput() {
        String booleanInput = in.nextLine();

        while (!isStringBoolean(booleanInput)) {
            printText(" Boolean input non-identified. Try again: ", ConsoleColor.RED);
            booleanInput = in.nextLine();
        }
        return whichBooleanIsString(booleanInput);
    }

    public String getGenderInput() {
        UI.printText(" Please enter the gender of the member (f/m): " ,ConsoleColor.RESET);
        String gender = null;
        while (gender == null) {
            String input = getStringInput();
            if (input.equalsIgnoreCase("f") || input.equalsIgnoreCase("m")) {
                gender = input;
            } else UI.printText(" Please enter either \"f\" or \"m\"", ConsoleColor.RED);
        }
        return gender;
    }

    //Date input for creating a member and creating a local date for it
    public LocalDate getLocalDateInput() {
        int day = 0, month = 0, year = 0;
        day = getDayInput();
        month = getMonthInput();
        year = getYearInput();
        LocalDate date = null;
        while (date == null) {
            try {
                date = LocalDate.of(year, month, day);
            } catch (DateTimeException e) {
                printText(" The date you have entered is invalid, please enter a valid date: ", ConsoleColor.RED);
                return getLocalDateInput();
            }
        } return  date;
    }

    //Ask for day input
    private int getDayInput() {
        int inputDay = 0;
        boolean isValid = false;

        while(!isValid) {
            System.out.print("\n Day('DD)': ");

            try {
                inputDay = in.nextInt();
                in.nextLine();

                if (inputDay >= 1 && inputDay <= 31) {
                    isValid = true;
                } else {
                    printText(" Invalid day. Please ensure the day is between 1 and 31. ", ConsoleColor.RED);
                }
            } catch (InputMismatchException e){
                printText(" Input not recognized, please try again. ", ConsoleColor.RED);
                in.next();
                return getDayInput();
            }
        }
        return inputDay;
    }

    //Ask for month inpput
    private int getMonthInput() {
        int inputMonth = 0;
        boolean isValid = false;

        while(!isValid) {
            System.out.print(" Month('MM'): ");

            try {
                inputMonth = in.nextInt();
                in.nextLine();

                if (inputMonth >= 1 && inputMonth <= 12) {
                    isValid = true;
                } else {
                    printText(" Invalid month. Please ensure the month is between 1 and 12. \n", ConsoleColor.RED);
                }
            } catch (InputMismatchException e){
                printText(" Input not recognized, please try again. \n", ConsoleColor.RED);
                in.next();
                return getMonthInput();
            }
        }
        return inputMonth;
    }

    //Ask for year input while checking what year it is
    private int getYearInput() {
        int inputYear = 0;
        boolean endLoop = false;

        while(!endLoop) {
            System.out.print(" Year('YYYY'): ");

            try {
                inputYear = in.nextInt();
                in.nextLine();

                if (inputYear >= 1915 && inputYear <= LocalDate.now().getYear()) {
                    endLoop = true;
                } else {
                    printText(" Invalid year. Please ensure the year is between 1915 and this year. \n", ConsoleColor.RED);
                }
            } catch (InputMismatchException e){
                printText(" Input not recognized, please try again. \n", ConsoleColor.RED);
                in.next();
                return getYearInput();
            }
        }
        return inputYear;
    }

    public static void printText(String text, ConsoleColor color) {
        System.out.print(color + text + ConsoleColor.RESET);
    }

    //Should be used to print trainingScore times but not implemented yet
    public void printFormattedSecondsToReadableTime(int totalSecs) {
        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;

        if (hours > 0) {
            System.out.printf(" %02dh:%02dm:%02ds%n", hours, minutes, seconds);
        } else {
            System.out.printf(" %02dm:%02ds%n", minutes, seconds);
        }
    }

    public void printDisciplines(ArrayList<Discipline> disciplines){
        if (!disciplines.isEmpty()) {
            for (Discipline discipline: disciplines)
                printText(" "+discipline.toString() + "\n",ConsoleColor.RESET);
        }
    }

    public void printMember(Member member) {
        System.out.println("\n\n _________________________________________________ ");

        if (member instanceof CompetitionMember) {
            System.out.printf(" COMPETITION-MEMBER №%d%28.7s%n", member.getMemberID(), member.getIsActive()?"Active":"Passive");
        } else {
            System.out.printf(" MEMBER №%d%39.7s%n", member.getMemberID(), member.getIsActive()?"Active":"Passive");
        }

        System.out.printf(" %-10s %-15S%23.1s%n", member.getFirstName(), member.getLastName(), member.getGender().toUpperCase() );
        System.out.printf(" Born on: %1$td/%1$tm/%tY ", member.getBirthdate());
        System.out.printf(" %26s%d%n","Age:",  Period.between(member.getBirthdate(), LocalDate.now()).getYears());
        System.out.printf(" Team: %S%n", member.getTeam());
        if (member instanceof CompetitionMember) {
            ArrayList<Discipline> disciplines = ((CompetitionMember) member).getDisciplines();
            ArrayList<CompetitionScore> competitions = ((CompetitionMember) member).getCompetitionScores();
            System.out.println();
            System.out.println(" ___________________Competition___________________");
            System.out.printf(" Coach: Coach %S%n", ((CompetitionMember) member).getCoach().getName());
            System.out.printf(" %s","Discipline(s): \n");
            for (Discipline discipline: disciplines) {
                System.out.printf("   %S | Best training score: ", discipline.label); // ADD CODE HERE TO PRINT DISCIPLE TRAINING SCORES
                printFormattedSecondsToReadableTime(((CompetitionMember) member).findTrainingTime(discipline));
                }
            System.out.println("\n Competition(s): ");
            for (CompetitionScore score: competitions) {
                System.out.printf("  %S: Discipline: %S | Placement: %d | Time: ", score.getCompetitionName(),score.getDiscipline(),score.getPlacement()); // ADD CODE HERE TO PRINT DISCIPLE TRAINING SCORES
                printFormattedSecondsToReadableTime(score.getTime());
            }
        }
        System.out.println();
        System.out.println(" ___________________Membership____________________");
        System.out.printf(" Price:%d,-%30s%n", member.getMembershipPrice(), member.getPaymentStatus()?"Paid":"Not-Paid");
        System.out.println(" _________________________________________________ ");
        System.out.println();
        System.out.println();
    }

    public void printTrainingScore(Discipline discipline, CompetitionMember member){
        int time =  member.findTrainingTime(discipline);
        if(time != 1000){
        String info = member.getFirstName() + " " + member.getLastName() + " : ";
        printText("    " + info,ConsoleColor.RESET);
        printFormattedSecondsToReadableTime(time);}
    }
    public void printTop5List(ArrayList<CompetitionMember> listOfMembers, String prompt, Discipline discipline) {
        String info = prompt + " in " + discipline.label + " top 5:_";
        int seniorSpaceLength = (48 - info.length()) / 2;
        System.out.println("\n " + "_".repeat(seniorSpaceLength) + info + "_".repeat(seniorSpaceLength) + " \n");
        for (int i = 0; i < listOfMembers.size(); i++)
            printTrainingScore(discipline, listOfMembers.get(i));
    }

    /// private boolean methods to check data type
    private boolean isStringBoolean(String strBool) {
        if (strBool.equalsIgnoreCase("y")
                || strBool.equalsIgnoreCase("true")
                || strBool.equalsIgnoreCase("yes")
                || strBool.equalsIgnoreCase("n")
                || strBool.equalsIgnoreCase("false")
                || strBool.equalsIgnoreCase("no")) {
            return true;
        } else {return false;}
    }


    private boolean whichBooleanIsString(String strBool) {
        if (strBool.equalsIgnoreCase("y")
                || strBool.equalsIgnoreCase("t")
                || strBool.equalsIgnoreCase("true")
                || strBool.equalsIgnoreCase("yes")) {
            return true;
        } else {return false;}
    }

    private boolean isString(String str) {
        Pattern pattern = Pattern.compile("^[a-zA-Z\\s\\-'.]+$"); // regex - complicated - Pattern for letters and a few special characters

        //check if StringInput matches our letter pattern
        Matcher matcher = pattern.matcher(str);

        return matcher.find(); // Boolean true if there are only letters in stringInput
    }


    // GET DISCIPLINES
    public Discipline getDiscipline(){
        Discipline discipline = null;
        while (discipline == null){
            String input = getStringInput();
            for (Discipline d : Discipline.values()){
            if (d.label.equals(input.toUpperCase())){
                 discipline = d;}
            }
            if (discipline ==null){
                printText(" Discipline not recognised, please try again: ",ConsoleColor.RED);
            }
        }
        return  discipline;
    }

    public void printWelcomeMessage() {
        System.out.println("\n ________________________________________________ ");
        System.out.println("                    DELFINEN                      ");
        System.out.println(" ________________________________________________ ");
        System.out.println("                                                  ");
        System.out.println("                    WELCOME                       ");
        System.out.println("                                                  ");
        System.out.println(" ________________________________________________ ");
    }

    public void printMembers(ArrayList<Member> members){
        System.out.println(" " + "_".repeat(48) + " ");
        for (Member member: members) {
            String info = "MemberID: " + member.getMemberID() + " - " + member.getFirstName();
            printText( "    " + info + " ".repeat(45-info.length()) + " \n",ConsoleColor.RESET);}
        System.out.println(" " + "_".repeat(48) + " ");
    }

    public void showPaymentStatusForAllMembers(ArrayList<Member> members) {
        printText("\n __________________PAYMENT STATUS_________________\n", ConsoleColor.RESET);
        for (Member member : members) {
            printMemberPaymentStatus(member);
        }
        printText(" _________________________________________________\n", ConsoleColor.RESET);
    }

    public void printMemberPaymentStatus(Member member) {
        System.out.printf(" Member ID: %d -", member.getMemberID());
        System.out.printf(" %s %s -", member.getFirstName(), member.getLastName());

        if(member.getPaymentStatus()) {
            printText(" Paid\n", ConsoleColor.GREEN);
        } else {
            printText(" Not Payed\n", ConsoleColor.RED);
        }
    }


    // ------ MENUS -------
    public Menu buildMainMenu() {
        Menu mainMenu = new Menu();
        mainMenu.setMenuTitle(" Which menu would you like to access?");
        mainMenu.setMenuItems("Cashier", "Manager ", "Coach", "Quit");
        mainMenu.printMenu();
        return mainMenu;
    }

    public Menu buildManagerMenu() {
        Menu managerMenu = new Menu();
        managerMenu.setMenuTitle("MANAGER_");
        managerMenu.setMenuItems("Add member", "See member information","Edit member information", "Delete member", "Add new Coach", "Return to Main Menu");
        managerMenu.printMenu();
        return managerMenu;
    }

    public Menu buildCashierMenu() {
        Menu cashierMenu = new Menu();
        cashierMenu.setMenuTitle("CASHIER_");
        cashierMenu.setMenuItems("Show payment status for all members", "Register payment status","Return to Main Menu");
        cashierMenu.printMenu();
        return cashierMenu;
    }

    public Menu buildChooseCoachMenu(ArrayList<Coach> coaches) {
        Menu chooseCoachMenu = new Menu();
        chooseCoachMenu.setMenuTitle("COACHES_");
        for (Coach coach : coaches) {
            chooseCoachMenu.addMenuItems(coach.getName());
        }
        chooseCoachMenu.printMenu();
        return chooseCoachMenu;
    }

    public Menu buildCoachMenu(){
        Menu coachMenu = new Menu();
        coachMenu.setMenuTitle("COACH_");
        coachMenu.setMenuItems("See Top 5", "Register training score", "Register competition score","Return to Main Menu");
        coachMenu.printMenu();
        return coachMenu;
    }
    public Menu buildSeeTop5Menu(){
        Menu seeTop5Menu = new Menu();
        seeTop5Menu.setMenuTitle("SEE TOP 5_");
        seeTop5Menu.setMenuItems("Crawl", "BackCrawl", "BreastStoke","Butterfly", "Medley");
        seeTop5Menu.printMenu();
        return seeTop5Menu;
    }
    public Menu buildEditMenu(){
        Menu editMenu = new Menu();
        editMenu.setMenuTitle("EDIT");
        editMenu.setMenuItems("Name", "Active-status", "Remove Disciplin","Add Disciplines","Return to Previous Menu");
        editMenu.printMenu();
        return editMenu;
    }

}
