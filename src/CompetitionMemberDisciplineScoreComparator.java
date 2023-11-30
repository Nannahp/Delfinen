import java.util.Comparator;

public class CompetitionMemberDisciplineScoreComparator implements Comparator<CompetitionMember> {
    private final Discipline discipline;

    public CompetitionMemberDisciplineScoreComparator(Discipline discipline) {
        this.discipline = discipline;
    }

    @Override
    public int compare(CompetitionMember o1, CompetitionMember o2) {
        int o1Time = o1.findTrainingTime(discipline);
        int o2Time = o2.findTrainingTime(discipline);

        return Integer.compare(o1Time, o2Time);
    }
}
