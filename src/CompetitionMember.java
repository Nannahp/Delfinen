import java.time.LocalDate;
import java.util.ArrayList;

public class CompetitionMember extends Member{

    private ArrayList<Discipline> disciplines = new ArrayList<>();

    // Liste af træningsscore for dette konkurrencemedlem

    private ArrayList<TrainingScore> trainingScores = new ArrayList<>();
    private ArrayList<DummyCompetitionScore> competitionScores = new ArrayList<>();

    // Bedste træningsscore for dette konkurrencemedlem

    private TrainingScore bestTrainingScore;

    private Coach coach;

    // Konstruktør, der kun tager disciplines og coach som parametre

    public CompetitionMember(String name, LocalDate birthdate, char gender, boolean isActive, int memberID, Coach coach, Discipline... disciplines) {
        super(name, birthdate, gender, isActive, memberID);
        //metode til at adde dicipplines til listen
        for (Discipline discipline: disciplines) {
            addDisciplines(discipline);
        }

       // this.disciplines = disciplines;
        this.coach = coach;
        //skal settes efter konstruktør?

        //this.trainingScores = trainingScores;
        //this.bestTrainingScore = bestTrainingScore;

    }
    // Metoder for at få og indstille disciplines

    public ArrayList<Discipline> getDisciplines() {
        return disciplines;
    }
    //Needs to add disciplines to a list of disciplines, AND check that there's only 1 of each disciplin
    public void addDisciplines(Discipline discipline) {
        if (!this.disciplines.contains(discipline)){
          this.disciplines.add(discipline);}
    }

    // Metoder for at få og indstille coach

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;

    }


    // Metoder for at få og indstille træningsscorer

    public ArrayList<TrainingScore> getTrainingScores() {
        return trainingScores;

    }

    //Needs method to add to trainingScore
    public void addTrainingScore(TrainingScore trainingScore){
        //needs to check for disciplines so there is only one score for each discpline
        this.trainingScores.add(trainingScore);

    }
    public ArrayList<DummyCompetitionScore> getCompetitionScores(){
        return competitionScores;
    }
}


