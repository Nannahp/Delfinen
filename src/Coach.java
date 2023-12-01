import java.io.Serializable;
import java.util.*;

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

    public void removeMemberByDiscipline(CompetitionMember member, Discipline discipline) {
        if (member.getTeam() == Team.JUNIOR) {
            switch (discipline) {
                case CRAWL -> deleteMemberInList(juniorCrawl, member);
                case BACKCRAWL -> deleteMemberInList(juniorBackcrawl, member);
                case BUTTERFLY -> deleteMemberInList(juniorButterfly, member);
                case BREASTSTROKE -> deleteMemberInList(juniorBreaststroke, member);
                case MEDLEY -> deleteMemberInList(juniorMedley, member);
            }
        } else {
            switch (discipline) {
                case CRAWL -> deleteMemberInList(seniorCrawl, member);
                case BACKCRAWL -> deleteMemberInList(seniorBackcrawl, member);
                case BUTTERFLY -> deleteMemberInList(seniorButterfly, member);
                case BREASTSTROKE -> deleteMemberInList(seniorBreaststroke, member);
                case MEDLEY -> deleteMemberInList(seniorMedley, member);
            }
        }
    }






    //Checks the arraylist of disciplines of given member and puts in right arraylist
    public void checkCompetitionMemberTeam(CompetitionMember member) {
        ArrayList<Discipline> disciplines = member.getDisciplines();
        boolean memberExist = false;
        for (Discipline discipline : disciplines) {
            memberExist = checkIfMemberExistInList(member,discipline);
            if(!memberExist) {
                switch (discipline) {
                    case CRAWL -> addToDisciplineList(member, juniorCrawl, seniorCrawl);
                    case BACKCRAWL -> addToDisciplineList(member, juniorBackcrawl, seniorBackcrawl);
                    case BUTTERFLY -> addToDisciplineList(member, juniorButterfly, seniorButterfly);
                    case BREASTSTROKE -> addToDisciplineList(member, juniorBreaststroke, seniorBreaststroke);
                    case MEDLEY -> addToDisciplineList(member, juniorMedley, seniorMedley);
                }
            }
        }
     if (!memberExist) {
      System.out.println("Added to team");}
    }

    //Checks if competition member is a junior or senior
    public void addToDisciplineList(CompetitionMember member, ArrayList<CompetitionMember> juniorList, ArrayList<CompetitionMember> seniorList) {
        boolean addToTeam = false;
        try {
            if (member.getIsActive()) {
                addToTeam = true;
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

    public void addTrainingScoreToMember(CompetitionMember member, TrainingScore trainingScore) {
       boolean memberExists = checkIfMemberExist(member);
        if (memberExists){
        member.updateTrainingScore(trainingScore);}
        else System.out.println("Member is not assigned to this coach");
    }

    //goes through one list and checks if there is a member
    public boolean checkIfMemberExistInList(CompetitionMember member, Discipline discipline){
        boolean memberExists = false;
            if (findMemberInCoach(member, getJuniorList(discipline) )||
                    findMemberInCoach(member, getSeniorList(discipline))){
                memberExists = true;
        }
        return  memberExists;
    }

    //Goes through all disciplines and checks if there is a member
    public boolean checkIfMemberExist(CompetitionMember member){
        boolean memberExists = false;
        for (Discipline discipline:member.getDisciplines()){
           if(checkIfMemberExistInList(member,discipline)){
                memberExists = true;}
            }
        return  memberExists;
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

    public ArrayList<Member> getAllMembers() {
        Set<Member> uniqueNames = new HashSet<>(); // Using a set to ensure uniqueness
        addMemberNamesToSet(uniqueNames, juniorCrawl);
        addMemberNamesToSet(uniqueNames, juniorBackcrawl);
        addMemberNamesToSet(uniqueNames, juniorBreaststroke);
        addMemberNamesToSet(uniqueNames, juniorButterfly);
        addMemberNamesToSet(uniqueNames, juniorMedley);
        addMemberNamesToSet(uniqueNames, seniorCrawl);
        addMemberNamesToSet(uniqueNames, seniorBackcrawl);
        addMemberNamesToSet(uniqueNames, seniorBreaststroke);
        addMemberNamesToSet(uniqueNames, seniorButterfly);
        addMemberNamesToSet(uniqueNames, seniorMedley);
        ArrayList list = new ArrayList<>(uniqueNames);
        Collections.sort(list);
        return list;
    }

    private void addMemberNamesToSet(Set<Member> uniqueNames, ArrayList<CompetitionMember> members) {
        for (Member member : members) {
            uniqueNames.add(member);
        }
    }




    public void updateMemberInCoach(CompetitionMember updatedMember) {
        if (updatedMember.getTeam().equals(Team.JUNIOR)){
            updateList(juniorCrawl,updatedMember);
            updateList(juniorBackcrawl,updatedMember);
            updateList(juniorBreaststroke,updatedMember);
            updateList(juniorButterfly,updatedMember);
            updateList(juniorMedley,updatedMember);
        }
        else {
            updateList(seniorCrawl,updatedMember);
            updateList(seniorBackcrawl,updatedMember);
            updateList(seniorBreaststroke,updatedMember);
            updateList(seniorButterfly,updatedMember);
            updateList(seniorMedley,updatedMember);

        }
    }

    public void updateList(ArrayList<CompetitionMember> list, CompetitionMember updatedMember){
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getMemberID() == updatedMember.getMemberID()) {
                list.set(i, updatedMember); // Replace the old Member with the updated one
            }
        }
    }
    public boolean findMemberInCoach(CompetitionMember memberToFind, ArrayList<CompetitionMember> list){
       boolean found = false;
        for (CompetitionMember member: list) {
            if(member.equals(memberToFind)){
                found =  true;
            }
        }
        return  found;
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
