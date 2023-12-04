import java.io.Serializable;

public class CompetitionScore implements Serializable {
    private String competitionName;
    private int placement;
    private int time;
    private Discipline discipline;

    public CompetitionScore(String competitionName, int placement, int time, Discipline discipline) {
        this.competitionName = competitionName;
        this.placement = placement;
        this.time = time;
        this.discipline = discipline;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public int getPlacement() {
        return placement;
    }

    public int getTime() {
        return time;
    }
}