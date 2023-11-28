import java.util.ArrayList;

public class DummyCompetitionMember extends  DummyMember{
    private ArrayList<DummyTrainingScore> trainingScores =new ArrayList<>();
    private ArrayList<DummyCompetitionScore> competitionScores = new ArrayList<>();

    public DummyCompetitionMember(String name, int memberID) {
        super(name, memberID);
        trainingScores.add(new DummyTrainingScore("Crawl", 12));
        trainingScores.add(new DummyTrainingScore("Butterfly", 30));
        competitionScores.add(new DummyCompetitionScore("Fyns mesterskab", 34));
        competitionScores.add(new DummyCompetitionScore("Århus mesterskab", 48));
    }
    public ArrayList<DummyTrainingScore> getTrainingScores(){
        return new ArrayList<>(trainingScores);
    }
    public ArrayList<DummyCompetitionScore> getCompetitionScores(){
        return  new ArrayList<>(competitionScores);
    }
    public void addTolist(DummyTrainingScore score){
        trainingScores.add(score);
    }
    public  void removeFromList(int i){
        trainingScores.remove(i);
    }
    public  void addCompetitionScore(DummyCompetitionScore score){
        competitionScores.add(score);
    }

    @Override
    public String toString() {
        return "DummyCompetitionMember{" +
                "trainingScores=" + trainingScores +
                ", competitionScores=" + competitionScores +
                '}';
    }
}
