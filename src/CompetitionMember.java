import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class CompetitionMember extends Member{

    private ArrayList<Discipline> disciplines = new ArrayList<>();
    private ArrayList<TrainingScore> trainingScores = new ArrayList<>(); //liste, da der er flere discipliner
    private ArrayList<CompetitionScore> competitionScores = new ArrayList<>();
    private Coach coach;
    UI ui = new UI();

    //Constructor that only takes disciplin and coach as parameters
    public CompetitionMember(String firstName, String lastName, LocalDate birthdate, String gender, boolean isActive, Coach coach, Discipline... disciplines) {
        super(firstName, lastName, birthdate, gender, isActive);
        //Metode til at adde dicipplines til listen
        for (Discipline discipline: disciplines) {
            addDisciplines(discipline);
        }
        this.coach = coach;
    }

    //Adds discipline to member
    public void addDisciplines(Discipline discipline) {
        if (!this.disciplines.contains(discipline)){
            this.disciplines.add(discipline);
        } else {
            UI.printText("A member can only be assigned to a discipline once\n", ConsoleColor.RED);
        }
    }

    //Loops through trainingScore list to see if there is a score with given disciplin
    public boolean checkIfTrainingScoreExists(Discipline discipline){
    boolean scoreExists = false;
    for (TrainingScore score: trainingScores) {
        if( score.getDiscipline().equals(discipline)){
            scoreExists = true;
        }
    }
    return scoreExists;
}


    public void editTrainingScores(TrainingScore trainingScore){
        if( doesMemberHaveDiscipline(trainingScore.getDiscipline())){
            if (checkIfTrainingScoreExists(trainingScore.getDiscipline())){
                updateExistingScore(trainingScore);
            }
            else trainingScores.add(trainingScore);
        }
        else {
            UI.printText(" This member is not active in: " + trainingScore.getDiscipline() + "\n", ConsoleColor.RED);
        }
    }

    //If the TrainingScore exist, this method is used to update it
    private void updateExistingScore(TrainingScore trainingScore) {
        // If it has an existing score
        for (TrainingScore existingScore : trainingScores) {
            // Finds the score with the right discipline
            if (existingScore.getDiscipline().equals(trainingScore.getDiscipline())) {
                // Checks if it really is the best score
                if (existingScore.getTime() > trainingScore.getTime()) {
                    existingScore.setTime(trainingScore.getTime());
                    existingScore.setDate(trainingScore.getDate());
                    System.out.println(" Training score updated");
                } else {
                    System.out.println(" This is not the best time for this member.");
                }
            }
        }
}

    //Finds a trainingScore for top 5
    public int findTrainingTime(Discipline discipline) {
        int time = 0;
        for (TrainingScore score : trainingScores) {
            if (score.getDiscipline().equals(discipline)) {
                time = score.getTime();
            }
        }
        return time;
    }

    //Adds competitionScore to member
    public boolean addCompetitionScore(CompetitionScore competitionScore) {
        try {
            //Checks if Member has the discipline
            if (doesMemberHaveDiscipline(competitionScore.getDiscipline())) {
                competitionScores.add(competitionScore);
                System.out.println("Competition score added to member");
                return true;
            } else {
                UI.printText(" This member is not active in: " + competitionScore.getDiscipline() + "\n", ConsoleColor.RED);
                return false;}
        } catch (Exception e) {
            System.err.println(" An error occurred while updating training score: " + e.getMessage());
            e.printStackTrace();}
        return false;
    }

    //Method to make sure the member has the disciplin
    private boolean doesMemberHaveDiscipline(Discipline discipline) {
        return getDisciplines().contains(discipline);
    }

    //Deletes disciplin from member
    public void deleteDiscipline(Discipline discipline) {
        if (doesMemberHaveDiscipline(discipline)) {
            disciplines.remove(discipline);
        } else UI.printText("This member is not active in this discipline\n", ConsoleColor.RED);
    }

    public ArrayList<Discipline> getDisciplines() {
        return disciplines;
    }

    public Coach getCoach() {
        return coach;
    }

    public ArrayList<TrainingScore> getTrainingScores() {
        return trainingScores;
    }

    public ArrayList<CompetitionScore> getCompetitionScores(){
        return competitionScores;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CompetitionMember otherMember = (CompetitionMember) obj;
        return Objects.equals(this.getMemberID(), otherMember.getMemberID());
    }

}

