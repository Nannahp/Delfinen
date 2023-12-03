import java.io.Serializable;
import java.util.*;

public class Coach implements Serializable {
    String name;
    UI ui = new UI();
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
    //Adds a member to coach
    public void addMemberToCoach(CompetitionMember member){
        checkCompetitionMemberTeam(member);
    }

    //Deletes a member by team
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

    //Deletes a member by Id
    public void deleteMemberInList(ArrayList<CompetitionMember> list, CompetitionMember memberToDelete) {
        list.removeIf(member -> member.getMemberID() == memberToDelete.getMemberID());
    }

    //Deletes a member by disciplin. Used when a member's disciplin is deleted
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
      ui.printText(" Added to team",ConsoleColor.RESET);}
    }

    //Checks if a member is a junior or a senior
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

    public String getName() {
        return name;
    }

    //Returns junior list
    public ArrayList<CompetitionMember> getJuniorList(Discipline discipline) {
        switch(discipline) {
            case CRAWL -> {return juniorCrawl;}
            case BACKCRAWL -> {return juniorBackcrawl;}
            case BUTTERFLY -> {return juniorButterfly;}
            case BREASTSTROKE -> {return juniorBreaststroke;}
            case MEDLEY -> {return juniorMedley;}
        }
        UI.printText("Something went wrong while retrieving the junior team list!", ConsoleColor.RED);
        return null;
    }

    //Adds trainingScore to member
    public void addTrainingScoreToMember(CompetitionMember member, TrainingScore trainingScore) {
       boolean memberExists = checkIfMemberExist(member);
        if (memberExists){
            member.editTrainingScores(trainingScore);}
        else UI.printText("Member is not assigned to this coach!", ConsoleColor.RED);
    }

    //Goes through one list and checks if there is a member
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

    //Adds competitionScore to member
    public void addCompetitionScoreToMember(CompetitionMember member, CompetitionScore competitionScore) {
        boolean memberExists = false;
        for (Discipline discipline : member.getDisciplines()){
            if (findMemberInCoach(member, getJuniorList(discipline) )||
                    findMemberInCoach(member, getSeniorList(discipline))){
                memberExists = true;
            }
        }
        if (memberExists){
            member.addCompetitionScore(competitionScore);}
        else UI.printText("Member is not assigned to this coach!\n", ConsoleColor.RED);
    }

    //Returns senior list based on disciplin
    public ArrayList<CompetitionMember> getSeniorList(Discipline discipline) {
        switch(discipline) {
            case CRAWL -> {return seniorCrawl;}
            case BACKCRAWL -> {return seniorBackcrawl;}
            case BUTTERFLY -> {return seniorButterfly;}
            case BREASTSTROKE -> {return seniorBreaststroke;}
            case MEDLEY -> {return seniorMedley;}
        }
        UI.printText("Something went wrong while retrieving the senior team list!\n", ConsoleColor.RED);
        return null;
    }

    //Gets all the members in a specific list
    public ArrayList<String> getMemberInList(ArrayList<CompetitionMember> members){
        ArrayList<String> names = new ArrayList<>();
        for (Member member: members) {
           names.add(member.getFirstName() + " " + member.getLastName());
        }
        return names;
    }

    //Gets all the members assigned to coach, but only once per member
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

    //Used for hardcoded members
    private void addMemberNamesToSet(Set<Member> uniqueNames, ArrayList<CompetitionMember> members) {
        for (Member member : members) {
            uniqueNames.add(member);
        }
    }

    //Used to update the member every time there is an edit for member
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

    //Used to update member
    public void updateList(ArrayList<CompetitionMember> list, CompetitionMember updatedMember){
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getMemberID() == updatedMember.getMemberID()) {
                list.set(i, updatedMember); // Replace the old Member with the updated one
            }
        }
    }

    //Used to find a specific member in a specific list
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
                ", juniorCrawl=" + getMemberInList(juniorCrawl) +
                ", juniorBackcrawl=" + getMemberInList(juniorBackcrawl) +
                ", juniorBreaststroke=" + getMemberInList(juniorBreaststroke) +
                ", juniorButterfly=" + getMemberInList(juniorButterfly) +
                ", juniorMedley=" + getMemberInList(juniorMedley) +
                ", seniorCrawl=" + getMemberInList(seniorCrawl) +
                ", seniorBackcrawl=" + getMemberInList(seniorBackcrawl) +
                ", seniorBreaststroke=" + getMemberInList(seniorBreaststroke) +
                ", seniorButterfly=" + getMemberInList(seniorButterfly) +
                ", seniorMedley=" + getMemberInList(seniorMedley) +
                '}';
    }


}
