
import java.io.Serializable;
import java.sql.ResultSet;
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
            printText("You didn't use a string. Try again.\n", ConsoleColor.RED);
            stringInput = in.nextLine();
        }
        return stringInput;
    }

    public static int getIntInput() {
        int intInput = 0;
        while (intInput == 0){
        try {
            intInput = in.nextInt();
        }
        catch (InputMismatchException e){
            printText("\nInput not recognized, please enter a number: ", ConsoleColor.RED);
        }
        in.nextLine();//scannerbug?
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
        UI.printText("\n Please enter the gender of the member (F/M)" ,ConsoleColor.RESET);
        String gender = null;
        while (gender == null) {
            String input = getStringInput();
            if (input.equalsIgnoreCase("f") || input.equalsIgnoreCase("m")) {
                gender = input;
            } else UI.printText("\n Please enter either \"f\" or \"m\"", ConsoleColor.RED);
        }
        return gender;
    }

    //Yes we know it's a f*cking long method
    public LocalDate getLocalDateInput() {
        int day = 0, month = 0, year = 0;

        while (year == 0) {
            try {
                System.out.print("Day('DD)': ");
                int inputDay = in.nextInt();
                in.nextLine();

                if (inputDay < 1 || inputDay > 31) {
                    printText("Invalid day. Please ensure the day is between 1 and 31: ", ConsoleColor.RED);
                    continue;
                }
                day = inputDay;

                System.out.print("Month('MM'): ");
                int inputMonth = in.nextInt();
                in.nextLine();

                if (inputMonth < 1 || inputMonth > 12) {
                    printText("Invalid month. Please ensure the month is between 1 and 12: ", ConsoleColor.RED);
                    continue;
                }
                month = inputMonth;

                System.out.print("Year('YYYY'): ");
                int inputYear = in.nextInt();
                in.nextLine();

                if (inputYear < 1915 || inputYear > LocalDate.now().getYear()) {
                    printText("Invalid year. Please ensure the year is between 1915 and this year: ", ConsoleColor.RED);
                    continue;
                }

                // Check if the selected month has fewer than 31 days
                if (inputDay > 28 && inputMonth == 2 && !isLeapYear(inputYear)) {
                    printText("Invalid day for February in a non-leap year. Please choose a valid day: ", ConsoleColor.RED);
                    day = 0; // Reset day to 0 to force re-entry
                    continue;
                } else if (inputDay > 29 && inputMonth == 2 && isLeapYear(inputYear)) {
                    printText("Invalid day for February in a leap year. Please choose a valid day: ", ConsoleColor.RED);
                    day = 0; // Reset day to 0 to force re-entry
                    continue;
                }

                // Check if the selected date is before the current date
                LocalDate selectedDate = LocalDate.of(inputYear, inputMonth, inputDay);
                if (selectedDate.isAfter(LocalDate.now())) {
                    printText("You can't register someone that isn't born yet. Try again: ", ConsoleColor.RED);
                    day = 0; // Reset day to 0 to force re-entry
                    continue;
                }

                year = inputYear;

            } catch (InputMismatchException e) {
                printText("Invalid input. Please enter numeric values for the date: ", ConsoleColor.RED);
                in.nextLine(); // Clear the input buffer
            } catch (Exception e) {
                printText("An unexpected error occurred. Please try again: ", ConsoleColor.RED);
                in.nextLine(); // Clear the input buffer
            }
        }

        return LocalDate.of(year, month, day);
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
            System.out.printf("%02dh:%02dm:%02ds%n", hours, minutes, seconds);
        } else {
            System.out.printf("%02dm:%02ds%n", minutes, seconds);
        }
    }

    //where did we use this?
    public void printListOfMembers(ArrayList<Member> members){
        System.out.println("\n");
        if (!members.isEmpty()) {
            for (Member member: members) {
                printText(member.toString()+"\n",ConsoleColor.RESET);
            }
        }
        System.out.println("\n");
    }

    public void printDisciplines(ArrayList<Discipline> disciplines){
        if (!disciplines.isEmpty()) {
            for (Discipline discipline: disciplines) {
                printText(discipline.toString() + "\n",ConsoleColor.RESET);
            }
        }
    }

    public void printArrayList (ArrayList list){
        for (int i =0; i< list.size(); i++){
            printText((String) list.get(i) + "\n", ConsoleColor.RESET);
        }
    }

    public void printList(ArrayList<Object> list) {

        if (!list.isEmpty()) {

            String objectClass = list.get(0).getClass().getName();
            if (objectClass.equalsIgnoreCase("Member")) {
                for (Object obj : list) {
                    if (obj instanceof Member member) {
                        printMember(member);
                    }
                }
            } else if (objectClass.equalsIgnoreCase("Coach")) {
                for (Object obj : list) {
                    if (obj instanceof Coach coach) {
                        System.out.println("Coach " + coach.getName());
                    }
                }
            } else {
                printText("Unsupported object type in the list: ", ConsoleColor.RED); }
        } else {
            printText("This list empty.", ConsoleColor.RED);
        }
    }

    public void printMember(Member member) {
        System.out.println(" _________________________________________________ ");

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
            System.out.println();
            System.out.println(" ___________________Competition___________________");
            System.out.printf(" Coach: Coach %S%n", ((CompetitionMember) member).getCoach().getName());
            System.out.printf(" %s","Discipline(s): ");
            for (int i = 0; i < disciplines.size(); i++) {
                Discipline discipline = disciplines.get(i);
                System.out.printf("%S", discipline); // ADD CODE HERE TO PRINT DISCIPLE TRAINING SCORES

                // Check if the current discipline is not the last one
                if (i < disciplines.size() - 1) {
                    System.out.print(", ");
                }
            }
            // ADD CODE HERE TO ADD COMPETITION SCORES AND OTHER DATA
            System.out.println();
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
        String info = member.getFirstName() + " " + member.getLastName() + " : ";
        printText("    " + info,ConsoleColor.RESET);
        printFormattedSecondsToReadableTime(time);
    }
    public void printTop5List(ArrayList<CompetitionMember> listOfMembers, String prompt, Discipline discipline) {
        String info = prompt + " in " + discipline.label + " top 5:_";
        int seniorSpaceLength = (48 - info.length()) / 2;
        System.out.println("\n " + "_".repeat(seniorSpaceLength) + info + "_".repeat(seniorSpaceLength) + " \n");
        for (int i = 0; i < listOfMembers.size(); i++) {
            printTrainingScore(discipline, listOfMembers.get(i));}
    }
    /// private boolean methods to check data type

    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    private boolean isStringBoolean(String strBool) {
        if (strBool.equalsIgnoreCase("y")
                || strBool.equalsIgnoreCase("t")
                || strBool.equalsIgnoreCase("true")
                || strBool.equalsIgnoreCase("yes")
                || strBool.equalsIgnoreCase("n")
                || strBool.equalsIgnoreCase("f")
                || strBool.equalsIgnoreCase("false")
                || strBool.equalsIgnoreCase("no")) {
            return true;
        } else {
            return false;
        }
    }


    private boolean whichBooleanIsString(String strBool) {
        if (strBool.equalsIgnoreCase("y")
                || strBool.equalsIgnoreCase("t")
                || strBool.equalsIgnoreCase("true")
                || strBool.equalsIgnoreCase("yes")) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


    private boolean isString(String str) {
        Pattern pattern = Pattern.compile("^[a-zA-Z]+$"); // regex - complicated
        // Pattern with regex for letters only

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
                printText("Discipline not recognised, please try again: \n",ConsoleColor.RED);
            }
        }
        return  discipline;
    }

    public void printAllMembers(ArrayList<Member> members) {
        for (Member member : members) {
            System.out.println("------------------------");
            System.out.printf("Member ID: %d%n", member.getMemberID());
            System.out.printf("Name: %s %s%n", member.getFirstName(), member.getLastName());
            System.out.printf("Membership Price: %d,-%n", member.getMembershipPrice());

            //Check payment status and print in red or green
            if (member.getPaymentStatus()) {
                printText("Payment Status: Paid\n", ConsoleColor.GREEN);
            } else {
                printText("Payment Status: Not Paid\n", ConsoleColor.RED);
            }

            System.out.println("_________________________________\n");
        }
    }

    public void printWelcomeMessage() {
        System.out.println(" ________________________________________________ ");
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
            String info = "MemberID: " + member.getMemberID() + " : " + member.getFirstName();
            printText("    " + info + " ".repeat(45-info.length()) + " \n",ConsoleColor.RESET);}
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
        System.out.printf(" Member ID: %d", member.getMemberID());
        System.out.printf(" %s %s ", member.getFirstName(), member.getLastName());

        if(member.getPaymentStatus()) {
            printText(" Paid\n", ConsoleColor.GREEN);
        } else {
            printText(" Not Payed\n", ConsoleColor.RED);
        }
    }


    // ------ MENUS -------

    public Menu buildMainMenu() {
        Menu mainMenu = new Menu();
        mainMenu.setMenuTitle("Which menu would you like to access?");
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
        editMenu.setMenuItems("Name", "Active-status", "Remove Disciplin","Add Disciplines","Return to Main Menu");
        editMenu.printMenu();
        return editMenu;
    }

}
