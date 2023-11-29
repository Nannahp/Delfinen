import java.io.Serializable;
import java.util.ArrayList;

public class Coach implements Serializable {
    String name;
    private static int nextCoachID = 1;
    private int coachID;
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
        this.coachID = nextCoachID++;
    }

    public void addMemberToCoach(CompetitionMember member){
        checkCompetitionMemberTeam(member);
    }

    //Checks the arraylist of disciplines of given member and puts in right arraylist
    private void checkCompetitionMemberTeam(CompetitionMember member) {
        ArrayList<Discipline> disciplines = member.getDisciplines();
        for (Discipline discipline : disciplines) {
            switch(discipline) {
                case CRAWL -> addToDisciplineList(member, juniorCrawl, seniorCrawl);
                case BACKCRAWL -> addToDisciplineList(member, juniorBackcrawl, seniorBackcrawl);
                case BUTTERFLY -> addToDisciplineList(member, juniorButterfly, seniorButterfly);
                case BREASTSTROKE -> addToDisciplineList(member, juniorBreaststroke, seniorBreaststroke);
                case MEDLEY -> addToDisciplineList(member, juniorMedley, seniorMedley);
            }
        }
    }

    //Checks if competition member is a junior or senior
    private void addToDisciplineList(CompetitionMember member, ArrayList<CompetitionMember> juniorList, ArrayList<CompetitionMember> seniorList) {
        if (member.getTeam() == Team.JUNIOR) {  //getTeam()
            juniorList.add(member);
        } else {
            seniorList.add(member);
        }
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getCoachID() {
        return coachID;
    }
    @Override
    public String toString() {
        return "Coach{" +
                "name='" + name + '\'' +
                ", juniorCrawl=" + juniorCrawl.toString() +
                ", juniorBackcrawl=" + juniorBackcrawl.toString() +
                ", juniorBreaststroke=" + juniorBreaststroke.toString() +
                ", juniorButterfly=" + juniorButterfly.toString() +
                ", juniorMedley=" + juniorMedley.toString() +
                ", seniorCrawl=" + seniorCrawl.toString() +
                ", seniorBackcrawl=" + seniorBackcrawl.toString() +
                ", seniorBreaststroke=" + seniorBreaststroke.toString() +
                ", seniorButterfly=" + seniorButterfly.toString() +
                ", seniorMedley=" + seniorMedley.toString() +
                '}';
    }
}
