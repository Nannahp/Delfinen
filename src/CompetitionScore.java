public class CompetitionScore {
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

}