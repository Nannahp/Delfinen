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

    // Prints the heading text and the menu
    public void printMenu() {
        String printString = menuTitle + "\n";

        for (int i = 0; i < menuItems.size(); i++) {
            printString += (i+1) +") " + menuItems.get(i) + "\n";
        }
        System.out.println("\n" + printString);
    }
}