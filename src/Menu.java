import java.util.ArrayList;
import java.util.Arrays;

public class Menu {
    private String menuTitle;
    private ArrayList<String> menuItems = new ArrayList<>();

    public Menu() {
    }

    public void setMenuItems(String... menuItems) {
        this.menuItems = new ArrayList<String>(Arrays.asList(menuItems));
    }
    public void addMenuItems(String item){
        this.menuItems.add(item);
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public int getMenuItemsSize() {
        return menuItems.size();
    }

    // HANDLE MENU CHOICES

    public int getMenuChoiceFromUserInput() {
        UI.printText(" Please enter the desired menu-number: ",ConsoleColor.RESET);
        return UI.getIntInput();
    }

    public int menuInputHandler() {
        int choice = getMenuChoiceFromUserInput();
        while (choice < 0 || getMenuItemsSize() < choice) {
            UI.printText("Not an option", ConsoleColor.RESET);
            choice = getMenuChoiceFromUserInput();
        }
        return choice;
    }

    // Prints the heading text and the menu
    public void printMenu() {
        int titleLength = menuTitle.length();
        int spaceLength = (49-titleLength)/2;
        System.out.println("\n");
        System.out.println(" "+"_".repeat(spaceLength) +  menuTitle + "_".repeat(spaceLength)+"\n");
        for (int i = 0; i < menuItems.size(); i++) {
            String line = (i+1) +") " + menuItems.get(i);
            System.out.println("   "+line);
        }
        System.out.println(" ________________________________________________ ");
        System.out.println("\n");
    }
}