public class ExecuteProgram {

    SystemManager manager = new SystemManager();

    public static void main(String[] args) {
        new ExecuteProgram().run();
    }

    //Runs the system until runProgram is false
    public void run() {
        manager.runProgram();
    }
}