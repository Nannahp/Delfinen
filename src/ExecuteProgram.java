public class ExecuteProgram {

    SystemManager manager = new SystemManager();

    public static void main(String[] args) {
        new ExecuteProgram().run();
    }


    //Runs the system until runMainMenu is false
    public void run() {
        manager.runMainMenu();

    }
}
