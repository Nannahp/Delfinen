import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class CompetitionMember extends Member{

    private ArrayList<Discipline> disciplines = new ArrayList<>();
    private ArrayList<TrainingScore> trainingScores = new ArrayList<>(); //liste, da der er flere discipliner
    private ArrayList<DummyCompetitionScore> competitionScores = new ArrayList<>();
    private Coach coach;

    // Konstruktør, der kun tager disciplines og coach som parametre
    public CompetitionMember(String firstName, String lastName, LocalDate birthdate, String gender, boolean isActive, Coach coach, Discipline... disciplines) {
        super(firstName, lastName, birthdate, gender, isActive);
        //Metode til at adde dicipplines til listen
        for (Discipline discipline: disciplines) {
            addDisciplines(discipline);
        }
        this.coach = coach;
    }

    public void addDisciplines(Discipline discipline) {
        if (!this.disciplines.contains(discipline)){
            this.disciplines.add(discipline);
        } else {
            System.out.println("A member can only be assigned to a discipline once");
        }
    }

    public void addTestTrainingScore(TrainingScore trainingScore) {
        if (doesMemberHaveDiscipline(trainingScore.getDiscipline())) {
            //Loop checks if the trainingScores list has a score with given discipline
            if (trainingScores.size() == 0 || !doesTrainingScoresContainsDiscipline(trainingScore.getDiscipline())) {
                trainingScores.add(trainingScore);
            }
        }
    }
/*
    //Checks if member has discipline, if score already exists and updates accordingly
    public boolean updateTrainingScore(TrainingScore trainingScore) {
        try {
            if (doesMemberHaveDiscipline(trainingScore.getDiscipline())) {
                //Loop checks if the trainingScores list has a score with given discipline
                if (trainingScores.size() ==0 || !doesTrainingScoresContainsDiscipline(trainingScore.getDiscipline()) ){
                    trainingScores.add(trainingScore);
                }
                for (TrainingScore existingScore : trainingScores) {
                    if (existingScore.getDiscipline().equals(trainingScore.getDiscipline())) {
                        if (existingScore.getTime() > trainingScore.getTime()) {
                            existingScore.setTime(trainingScore.getTime());
                            existingScore.setDate(trainingScore.getDate());
                            return true;
                        }  else {
                            System.out.println("The score is not the best score for this discipline");
                            return false;
                        }
                    }
                }
                trainingScores.add(trainingScore);
                return true;
            }
        } catch (Exception e) {
            System.err.println("An error occurred while updating training score: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
*/

public boolean checkIfTrainingScoreExists(Discipline discipline){
    boolean scoreExists = false;
    for (TrainingScore score: trainingScores) {
        if( score.getDiscipline().equals(discipline)){
            scoreExists = true;
        }
    }
    return scoreExists;
}
public boolean updateTrainingScore(TrainingScore trainingScore) {
    try {
        //Checks if Member has the discipline
        if (doesMemberHaveDiscipline(trainingScore.getDiscipline())) {
            //Checks if the trainingScores list has a score with given discipline, if not, just adds it
            if (!checkIfTrainingScoreExists(trainingScore.getDiscipline()) || updateExistingScore(trainingScore)){
                trainingScores.add(trainingScore);
                System.out.println("Training score updated");
            } else System.out.println("This is not the best score for this member");
            return true;
        }
        System.out.println("This member is not active in: " + trainingScore.getDiscipline());
        return false;
    } catch (Exception e) {
        System.err.println("An error occurred while updating training score: " + e.getMessage());
        e.printStackTrace();
    }
    return false;
}
    private boolean updateExistingScore(TrainingScore trainingScore) {
        //If it has existing score
        for (TrainingScore existingScore : trainingScores) {
            //Finds the score with the right discipline
            if (existingScore.getDiscipline().equals(trainingScore.getDiscipline())) {
                //Checks if it really is the best score
                if (existingScore.getTime() > trainingScore.getTime()) {
                    existingScore.setTime(trainingScore.getTime());
                    existingScore.setDate(trainingScore.getDate());
                    return true;
                }
            }
        }
        return false;
    }


    public int findTrainingTime(Discipline discipline) {
        int time = 0;
        for (TrainingScore score : trainingScores) {
            if (score.getDiscipline().equals(discipline)) {
                time = score.getTime();
            }
        }
        return time;
    }


    public boolean doesTrainingScoresContainsDiscipline(Discipline discipline) {
        for (TrainingScore score : trainingScores) {
            if (score.getDiscipline().equals(discipline)) {
                return true;
            }
        }
        return false;
    }

    //Metode til at sikre, at medlemmet har disciplinen i træningsscore
    private boolean doesMemberHaveDiscipline(Discipline discipline) {
        return getDisciplines().contains(discipline);
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

    public ArrayList<DummyCompetitionScore> getCompetitionScores(){
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

