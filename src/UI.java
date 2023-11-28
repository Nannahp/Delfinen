import java.util.ArrayList;
import java.util.Arrays;

public class UI {


    public void buildMainMenu() {
        Menu mainMenu = new Menu();
        mainMenu.setMenuTitle("     Which menu would you like to access?\n");
        mainMenu.setMenuItems("Cashier", "Manager ", "Coach", "Quit");
        mainMenu.printMenu();
    }

    public void buildManagerMenu() {
        Menu managerMenu = new Menu();
        managerMenu.setMenuTitle("     MANAGER \n What would you like to do?");
        managerMenu.setMenuItems("Add member", "Edit member information", "Delete member", "Add new Coach", "Return to Main Menu");
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
        chooseCoachMenu.setMenuTitle("     WHICH COACH ARE YOU \n");
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
        seeTop5Menu.setMenuTitle("     SEE TOP 5 \n");
        seeTop5Menu.setMenuItems("Crawl", "BackCrawl", "BreastStoke","Butterfly", "Medley","Return to Main Menu");
        seeTop5Menu.printMenu();

    }

}

