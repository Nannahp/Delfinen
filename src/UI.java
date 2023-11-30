
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UI {
    private Scanner in = new Scanner(System.in);


    // Getting inout methods
    public String getStringInput() {
        String stringInput = in.nextLine();

        while (!isString(stringInput)) {
            System.out.println("You didn't use a string. Try again.");
            stringInput = in.nextLine();
        }
        return stringInput;
    }

    public int getIntInput() {
        int intInput = 0;
        while (intInput == 0){
        try {
            intInput = in.nextInt();
        }
        catch (InputMismatchException e){
            printText("Input not recognized, please enter a number:");
         }
        in.nextLine();//scannerbug?
        }
        return intInput;
    }

    public boolean getBooleanInput() {
        String booleanInput = in.nextLine();

        while (!isStringBoolean(booleanInput)) {
            System.out.println("Boolean input non-identified. Try again.");
            booleanInput = in.nextLine();
        }

        return whichBooleanIsString(booleanInput);
    }

    public LocalDate getLocalDateInput() {
        int day = 0, month = 0, year = 0;

        while (year == 0) {
            try {
                System.out.print("Day('DD)': ");
                int inputDay = in.nextInt();
                in.nextLine();

                if (inputDay < 1 || inputDay > 31) {
                    System.out.println("Invalid day. Please ensure the day is between 1 and 31.");
                    continue;
                }
                day = inputDay;

                System.out.print("Month('MM'): ");
                int inputMonth = in.nextInt();
                in.nextLine();

                if (inputMonth < 1 || inputMonth > 12) {
                    System.out.println("Invalid month. Please ensure the month is between 1 and 12.");
                    continue;
                }
                month = inputMonth;

                System.out.print("Year('YYYY'): ");
                int inputYear = in.nextInt();
                in.nextLine();

                if (inputYear < 1915 || inputYear > LocalDate.now().getYear()) {
                    System.out.println("Invalid year. Please ensure the year is between 1915 and this year.");
                    continue;
                }

                // Check if the selected month has fewer than 31 days
                if (inputDay > 28 && inputMonth == 2 && !isLeapYear(inputYear)) {
                    System.out.println("Invalid day for February in a non-leap year. Please choose a valid day.");
                    day = 0; // Reset day to 0 to force re-entry
                    continue;
                } else if (inputDay > 29 && inputMonth == 2 && isLeapYear(inputYear)) {
                    System.out.println("Invalid day for February in a leap year. Please choose a valid day.");
                    day = 0; // Reset day to 0 to force re-entry
                    continue;
                }

                // Check if the selected date is before the current date
                LocalDate selectedDate = LocalDate.of(inputYear, inputMonth, inputDay);
                if (selectedDate.isAfter(LocalDate.now())) {
                    System.out.println("You can't register someone that isn't born yet. Try again.");
                    day = 0; // Reset day to 0 to force re-entry
                    continue;
                }

                year = inputYear;

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter numeric values for the date.");
                in.nextLine(); // Clear the input buffer
            } catch (Exception e) {
                System.out.println("An unexpected error occurred. Please try again.");
                in.nextLine(); // Clear the input buffer
            }
        }

        return LocalDate.of(year, month, day);
    }


    // Printing methods
    public void printLocalDate(LocalDate date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        System.out.println(date.format(dateFormatter));
    }

    public void printText(String text) {
        System.out.println(text);
    }

    public void printFormattedSecondsToReadableTime(int totalSecs) {
        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;

        if (hours > 0) {
            System.out.printf("%02d:%02d:%02d%n", hours, minutes, seconds);
        } else {
            System.out.printf("%02d:%02d%n", minutes, seconds);
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
                System.out.println("Unsupported object type in the list.");
            }
        } else {
            System.out.println("This list empty.");
        }
    }

    public void printMember(Member member) {
        System.out.println("__________________________________________");

        if (member instanceof CompetitionMember) {
            System.out.printf("COMPETITION-MEMBER №%d%20.7s%n", member.getMemberID(), member.getIsActive()?"Active":"Passive");
        } else {
            System.out.printf("MEMBER №%d%32.7s%n", member.getMemberID(), member.getIsActive()?"Active":"Passive");
        }

        System.out.printf("%-10s %-12S%18.1s%n", member.getFirstName(), member.getLastName(), member.getGender().toUpperCase() );
        System.out.printf("Born on: %1$td/%1$tm/%tY ", member.getBirthdate());
        System.out.printf("%19s%d%n","Age:",  Period.between(member.getBirthdate(), LocalDate.now()).getYears());
        System.out.printf("Team: %S%n", member.getTeam());
        if (member instanceof CompetitionMember) {
            ArrayList<Discipline> disciplines = ((CompetitionMember) member).getDisciplines();
            System.out.println();
            System.out.println("_______________Competition________________");
            System.out.printf("Coach: Coach %S%n", ((CompetitionMember) member).getCoach().getName());
            System.out.printf("%s","Discipline(s): ");
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
        System.out.println("________________Membership________________");
        System.out.printf("Price:%d,-%10s%n", member.getMembershipPrice(), member.hasPaid()?"Paid":"Not-Paid");
        System.out.println("__________________________________________");
        System.out.println();
        System.out.println();
    }

    public void printTrainingScore(Discipline discipline, CompetitionMember member){
        int time =  member.findTrainingTime(discipline);
        printText(member.getFirstName() + " " + member.getLastName() + " : " + time );
    }

    public void printTop5List(ArrayList<CompetitionMember> listOfMembers, Discipline discipline){
        for( int i =0; i <listOfMembers.size(); i++){
            printTrainingScore(discipline, listOfMembers.get(i));
        }
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
                printText("Discipline not recognised, please try again");
            }
        }
        return  discipline;
    }



    // HANDLE MENU CHOICES

    public int getMenuChoiceFromUserInput(){
        printText("Please enter the desired menu-number: ");
        return getIntInput();
    }


    // ------ MENUS -------

    public void buildMainMenu() {
        Menu mainMenu = new Menu();
        mainMenu.setMenuTitle("     Which menu would you like to access?\n");
        mainMenu.setMenuItems("Cashier", "Manager ", "Coach", "Quit");
        mainMenu.printMenu();
    }

    public void buildManagerMenu() {
        Menu managerMenu = new Menu();
        managerMenu.setMenuTitle("     MANAGER \n What would you like to do?");
        managerMenu.setMenuItems("Add member", "See member information","Edit member information", "Delete member", "Add new Coach", "Return to Main Menu");
        managerMenu.printMenu();

    }

    public void buildCashierMenu() {
        Menu cashierMenu = new Menu();
        cashierMenu.setMenuTitle("     CASHIER \n");
        cashierMenu.setMenuItems("Return to Main Menu");
        cashierMenu.printMenu();

    }

    public void buildChooseCoachMenu(ArrayList<Coach> coaches) {
        Menu chooseCoachMenu = new Menu();
        chooseCoachMenu.setMenuTitle("     COACHES \n");
        for (Coach coach : coaches) {
            chooseCoachMenu.addMenuItems(coach.getName());
        }
        chooseCoachMenu.printMenu();

    }

    public void buildCoachMenu(){
        Menu coachMenu = new Menu();
        coachMenu.setMenuTitle("     COACH \n");
        coachMenu.setMenuItems("See Top 5", "Register training score", "Register competition score","Return to Main Menu");
        coachMenu.printMenu();

    }
    public void buildSeeTop5Menu(){
        Menu seeTop5Menu = new Menu();
        seeTop5Menu.setMenuTitle("     SEE TOP 5 \n   Please enter a discipline ");
        seeTop5Menu.setMenuItems("Crawl", "BackCrawl", "BreastStoke","Butterfly", "Medley","Return to Main Menu");
        seeTop5Menu.printMenu();

    }

}
