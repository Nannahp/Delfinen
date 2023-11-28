import java.io.Serializable;
import java.util.ArrayList;

public class Coach implements Serializable {
    String name;
    private ArrayList<CompetitionMember> juniorCrawl = new ArrayList<CompetitionMember>();
    private ArrayList<CompetitionMember> juniorBackcrawl = new ArrayList<CompetitionMember>();
    private ArrayList<CompetitionMember> juniorBreaststroke = new ArrayList<CompetitionMember>();
    private ArrayList<CompetitionMember> juniorButterfly = new ArrayList<CompetitionMember>();
    private ArrayList<CompetitionMember> juniorMedley = new ArrayList<CompetitionMember>();
    private ArrayList<CompetitionMember> seniorCrawl = new ArrayList<CompetitionMember>();
    private ArrayList<CompetitionMember> seniorBackcrawl = new ArrayList<CompetitionMember>();
    private ArrayList<CompetitionMember> seniorBreaststroke = new ArrayList<CompetitionMember>();
    private ArrayList<CompetitionMember> seniorButterfly = new ArrayList<CompetitionMember>();
    private ArrayList<CompetitionMember> seniorMedley = new ArrayList<CompetitionMember>();

    //Constructor
    Coach(String name) {
        this.name = name;
    }

    //Checks if competition member is a junior or senior
    private void checkCompetitionMemberTeam(CompetitionMember member) {
        if (member.getTeam() == Team.JUNIOR) {  //getTeam()
            addJuniorToDisciplineList(member);
        } else {
            addSeniorToDisciplineList(member);
        }
    }

    //Checks the arraylist of disciplines of given member and puts in right arraylist
    private void addJuniorToDisciplineList(CompetitionMember member) {
        ArrayList<Discipline> disciplines = member.getDisciplines();
        for (Discipline discipline : disciplines) {
            switch(discipline) {
                case CRAWL -> juniorCrawl.add(member);
                case BACKCRAWL -> juniorBackcrawl.add(member);
                case BUTTERFLY -> juniorButterfly.add(member);
                case BREASTSTROKE -> juniorBreaststroke.add(member);
                case MEDLEY -> juniorMedley.add(member);
            }
        }
    }
//Er der en måde at få det til at være mindre ens?
    private void addSeniorToDisciplineList(CompetitionMember member) {
        ArrayList<Discipline> disciplines = member.getDisciplines();
        for (Discipline discipline : disciplines) {
            switch(discipline) {
                case CRAWL -> seniorCrawl.add(member);
                case BACKCRAWL -> seniorBackcrawl.add(member);
                case BUTTERFLY -> seniorButterfly.add(member);
                case BREASTSTROKE -> seniorBreaststroke.add(member);
                case MEDLEY -> seniorMedley.add(member);
            }
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
