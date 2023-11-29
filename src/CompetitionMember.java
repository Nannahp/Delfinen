import java.time.LocalDate;
import java.util.ArrayList;

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

    //Needs to add disciplines to a list of disciplines, AND check that there's only 1 of each disciplin
    public ArrayList<Discipline> getDisciplines() {
        return disciplines;
    }

    public void addDisciplines(Discipline discipline) {
        if (!this.disciplines.contains(discipline)){
            this.disciplines.add(discipline);
        } else {
            System.out.println("A member can only be assigned to a discipline once");
        }
            this.disciplines.add(discipline);
        } else {
            System.out.println("The member already has this discipline assigned");
        }
    }


    //Checks if member has discipline, if score already exists and updates accordingly
    private boolean updateTrainingScore(TrainingScore trainingScore) {
        try {
            if (doesMemberHaveDiscipline(trainingScore.getDiscipline())) {
                //Loop checks if the trainingScores list has a score with given discipline
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
}

