import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileHandler {

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
    public static void appendCompetitionScoreForMemberToFile(String filename, DummyCompetitionMember member){
        try {
            List<String> lines = readLinesFromFile("src\\" + filename);
            int memberIdToUpdate = member.getMemberID();

            for (int i = 1; i < lines.size(); i++) {
                String[] column = lines.get(i).split("\t");
                int memberId = Integer.parseInt(column[0].trim());
                if (memberId == memberIdToUpdate) {
                    column[3] = member.getCompetitionScores().toString();
                    lines.set(i, String.join("\t", column));
                }
            }

            // Write the updated content back to the file
            writeLinesToFile("src\\"+filename, lines);
        }catch (IOException e){
            e.printStackTrace();
        }

    }


    public static void appendMemberToFile(String filename, DummyMember member){
            File file = new File("src\\"+filename);
            try {
                FileOutputStream fos = new FileOutputStream(file, true); //used for tokens
                PrintStream ps = new PrintStream(fos);
                ps.append(member.getMemberID() + "\t");
                ps.append(member.getName() + "\t");

                if (member instanceof DummyCompetitionMember) {
                    DummyCompetitionMember competitionMember = (DummyCompetitionMember) member;
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

    public static void editTrainingScores(String filename, DummyCompetitionMember member) {
        try {
            List<String> lines = readLinesFromFile("src\\" + filename);
            int memberIdToUpdate = member.getMemberID();

            for (int i = 1; i < lines.size(); i++) {
                String[] column = lines.get(i).split("\t");
                int memberId = Integer.parseInt(column[0].trim());
                if (memberId == memberIdToUpdate) {
                    column[2] = member.getTrainingScores().toString(); // Assuming getTrainingScoresAsString() returns a formatted string
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
