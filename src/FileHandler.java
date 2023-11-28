import java.io.*;
import java.util.*;

public class FileHandler {

    public static void saveObjectsToFile(String filename, List<Object> objects) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src\\" + filename))) {
            for (Object object : objects) {
                oos.writeObject(object);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void appendObjectToFile(String filename, Object newObject) {
        List<Object> existingObjects = new ArrayList<>(loadObjectsFromFile(filename));
        existingObjects.add(newObject);

        saveObjectsToFile(filename, existingObjects);
    }

    public static List<Object> loadObjectsFromFile(String filename) {
        List<Object> objects = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("src\\" + filename))) {
            Object object;
            while ((object = ois.readObject()) != null) {
                objects.add(object);
            }
        } catch (EOFException e) {
            // End of file reached, do nothing
        } catch (IOException | ClassNotFoundException e) {

        }
        return objects;
    }

    public static void updateObjectInFile(String filename,  Object objectToUpdate){
        List<Object> objects = loadObjectsFromFile(filename);

        ListIterator<Object> iterator =  objects.listIterator();

        while (iterator.hasNext()) {
            Object object = iterator.next();
            if (object instanceof Member) {
                Member member = (Member) object;
                Member memberToUpdate = (Member) objectToUpdate;

                if (member.getMemberID() == memberToUpdate.getMemberID()) {
                    iterator.remove(); // Remove the existing object
                    Member updatedObject = memberToUpdate; // Add the updated member
                    iterator.add(updatedObject);
                }
        }
            else {
                Coach coach = (Coach) object;
                Coach coachToUpdate = (Coach) objectToUpdate;
            }
        }

    saveObjectsToFile(filename, objects);
}




    public static void createFile(String fileName) {
        try {
            File newFile = new File("src\\"+fileName);
            if (newFile.createNewFile()) {
                System.out.println("File created: " + newFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printFile(String fileName) { //To print file
        try {
            File file = new File("src\\"+fileName);
            Scanner myReader = new Scanner(file); //UI
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createTitlesForFile(String filename, ArrayList<String> titles){
        File file = new File("src\\"+filename);
        try {
            FileOutputStream fos = new FileOutputStream(file, true); //used for tokens
            PrintStream ps = new PrintStream(fos);
            ps.println(String.join("\t", titles));
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");;
        }
    }

    public static void updateMember(String filename, Member member){
        try {
            List<String> lines = readLinesFromFile("src\\" + filename);
            int memberIdToUpdate = member.getMemberID();
            for (int i = 1; i < lines.size(); i++) {
                String[] column = lines.get(i).split("\t");
                int memberId = Integer.parseInt(column[0].trim());
                if (memberId == memberIdToUpdate) {
                    lines.remove(i);
                   appendMemberToFile(filename, member);}
            }

    } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void appendCompetitionScoreForMemberToFile(String filename, CompetitionMember member){
        try {
            List<String> lines = readLinesFromFile("src\\" + filename);
            int memberIdToUpdate = member.getMemberID();

            for (int i = 1; i < lines.size(); i++) {
                String[] column = lines.get(i).split("\t");
                int memberId = Integer.parseInt(column[0].trim());
                if (memberId == memberIdToUpdate) {
                    column[8] = member.getCompetitionScores().toString();
                    lines.set(i, String.join("\t", column));
                }
            }

            // Write the updated content back to the file
            writeLinesToFile("src\\"+filename, lines);
        }catch (IOException e){
            e.printStackTrace();
        }

    }



    public static void appendMemberToFile(String filename, Member member){
            File file = new File("src\\"+filename);
            try {
                FileOutputStream fos = new FileOutputStream(file, true); //used for tokens
                PrintStream ps = new PrintStream(fos);
                ps.append(member.getMemberID() + "\t");
                ps.append(member.getName() + "\t");
                ps.append(member.getBirthdate().toString() + "\t");
                ps.append(member.isActive() + "\t");
                ps.append(member.getHasPaid() + "\t");
                ps.append(member.getMembershipPrice() + "\t");
                ps.append(member.getTeam() + "\t");
                if (member instanceof CompetitionMember) {
                    CompetitionMember competitionMember = (CompetitionMember) member;
                    ps.append((competitionMember.getTrainingScores().toString()) + "\t");
                    ps.append(competitionMember.getCompetitionScores() + "\t");
                }
                  /*
                    //if competitionMember then add extras
                    for (DummyTrainingScore trainingScore : ((DummyCompetitionMember) member).getTrainingScores()) {
                        ps.append(trainingScore + ",");}
                    ps.append("\t");
                    for (DummyCompetitionScore competitionScore: ((DummyCompetitionMember) member).getCompetitionScores()){
                        ps.append(competitionScore + ",");
                    }
                    ps.append("\t");
                }*/
                ps.append("\n");
                ps.close();
                fos.close();

        } catch (FileNotFoundException e) {
                System.out.println("File Not Found");;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    public static void appendCoachToFile(String filename, Coach coach){
        File file = new File("src\\"+filename);
        try {
            FileOutputStream fos = new FileOutputStream(file, true);
            PrintStream ps = new PrintStream(fos);
            ps.append(coach.getName() + "\t");
            //What else?
            ps.append("\n");
            ps.close();
            fos.close();

        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /*
    public ArrayList<Coach> updateCoachList(){
        List<String> lines;
        try {
            lines = readLinesFromFile("src\\Coaches.csv");
            for (int i = 1; i < lines.size(); i++) {
                String[] column = lines.get(i).split("\t");

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
*/

    private static List<String> readLinesFromFile(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }
    private static void writeLinesToFile(String filename, List<String> lines) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        }
    }

    public static void editTrainingScores(String filename, CompetitionMember member) {
        try {
            List<String> lines = readLinesFromFile("src\\" + filename);
            int memberIdToUpdate = member.getMemberID();

            for (int i = 1; i < lines.size(); i++) {
                String[] column = lines.get(i).split("\t");
                int memberId = Integer.parseInt(column[0].trim());
                if (memberId == memberIdToUpdate) {
                    column[7] = member.getTrainingScores().toString();
                    lines.set(i, String.join("\t", column));
                }
            }

        /* try{
        List<String> lines = readLinesFromFile("src\\"+filename);
        int memberIdToUpdate = member.getMemberID();

        List<DummyTrainingScore> newTrainingScores = member.getTrainingScores();
        // Update the crawl score for the specified member ID
        for (int i = 1; i < lines.size(); i++) { // Start from 1 to skip the header
            String[] column = lines.get(i).split("\t");
            int memberId = Integer.parseInt(column[0]); //Find the memberID to edit
            if (memberId == memberIdToUpdate) {
                // Replace the training scores with the new ones
                StringBuilder newTrainingScoresStr = new StringBuilder();
                for (DummyTrainingScore score : newTrainingScores) {
                    newTrainingScoresStr.append(score).append(",");
                }
                column[2] = newTrainingScoresStr.toString(); //It overwrites the third column, make sure it fits
                lines.set(i, String.join("\t", column));
            }
        }
*/
        // Write the updated content back to the file
        writeLinesToFile("src\\"+filename, lines);
    }catch (IOException e){
             e.printStackTrace();
         }

     }
}
