import java.io.Serializable;
import java.util.ArrayList;

public class Coach implements Serializable {
    String name;
    private int coachId;
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

    public void addMemberToCoach(CompetitionMember member){
        checkCompetitionMemberTeam(member);
    }

    public void removeMemberFromCoachLists(CompetitionMember member) {
        if (member.getTeam() == Team.JUNIOR) {
            deleteMemberInList(juniorCrawl, member);
            deleteMemberInList(juniorBackcrawl, member);
            deleteMemberInList(juniorBreaststroke, member);
            deleteMemberInList(juniorButterfly, member);
            deleteMemberInList(juniorMedley, member);
        } else {
            deleteMemberInList(seniorCrawl, member);
            deleteMemberInList(seniorBackcrawl, member);
            deleteMemberInList(seniorBreaststroke, member);
            deleteMemberInList(seniorButterfly, member);
            deleteMemberInList(seniorMedley, member);
        }
    }
    public void deleteMemberInList(ArrayList<CompetitionMember> list, CompetitionMember memberToDelete) {
        list.removeIf(member -> member.getMemberID() == memberToDelete.getMemberID());
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
        boolean addToTeam = false;
        try {
            if (member.getIsActive()) {
                addToTeam = true;
                System.out.println("Added to team");
                if (member.getTeam() == Team.JUNIOR) {  //getTeam()
                    juniorList.add(member);
                } else {
                    seniorList.add(member);
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while adding a member to the team:" + e.getMessage());
        }
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCoachId(int coachId) {
        this.coachId = coachId;
    }

    public ArrayList<CompetitionMember> getJuniorList(Discipline discipline) {
        switch(discipline) {
            case CRAWL -> {return juniorCrawl;}
            case BACKCRAWL -> {return juniorBackcrawl;}
            case BUTTERFLY -> {return juniorButterfly;}
            case BREASTSTROKE -> {return juniorBreaststroke;}
            case MEDLEY -> {return juniorMedley;}
        }
        System.out.println("Something went wrong while retrieving the junior team list");
        return null;
    }

    public ArrayList<CompetitionMember> getSeniorList(Discipline discipline) {
        switch(discipline) {
            case CRAWL -> {return seniorCrawl;}
            case BACKCRAWL -> {return seniorBackcrawl;}
            case BUTTERFLY -> {return seniorButterfly;}
            case BREASTSTROKE -> {return seniorBreaststroke;}
            case MEDLEY -> {return seniorMedley;}
        }
        System.out.println("Something went wrong while retrieving the senior team list");
        return null;
    }



    public ArrayList<String> getMemberNamesInList(ArrayList<CompetitionMember> members){
        ArrayList<String> names = new ArrayList<>();
        for (Member member: members) {
           names.add(member.getFirstName() + " " + member.getLastName());
        }
        return names;
    }





    @Override
    public String toString() {
        return "Coach{" +
                "name='" + name + '\'' +
                "id='" + coachId + '\'' +
                ", juniorCrawl=" + getMemberNamesInList(juniorCrawl) +
                ", juniorBackcrawl=" + getMemberNamesInList(juniorBackcrawl) +
                ", juniorBreaststroke=" + getMemberNamesInList(juniorBreaststroke) +
                ", juniorButterfly=" + getMemberNamesInList(juniorButterfly) +
                ", juniorMedley=" + getMemberNamesInList(juniorMedley) +
                ", seniorCrawl=" + getMemberNamesInList(seniorCrawl) +
                ", seniorBackcrawl=" + getMemberNamesInList(seniorBackcrawl) +
                ", seniorBreaststroke=" + getMemberNamesInList(seniorBreaststroke) +
                ", seniorButterfly=" + getMemberNamesInList(seniorButterfly) +
                ", seniorMedley=" + getMemberNamesInList(seniorMedley) +
                '}';
    }
}
