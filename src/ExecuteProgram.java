public class ExecuteProgram {
    boolean systemRunning = true;
    SystemManager manager = new SystemManager();

    public static void main(String[] args) {
        new ExecuteProgram().run();
    }

    //Runs the system until runMainMenu is false
    public void run() {
        while (systemRunning) {
            systemRunning = manager.runMainMenu();
        }
    }
}

      /*  SystemManager systemManager = new SystemManager();
        systemManager.createFile("testFil.csv","ID","Name","TrainingScores", "CompetitionScores" );
        systemManager.addMember("oscar", 101);
        systemManager.addCompetitionMember("andy", 102);
        systemManager.printFile("testFil.csv");
        systemManager.testAddCompetition();
        systemManager.testEditTrainingScore();
        systemManager.printFile("testFil.csv");

    }*/

