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
            // End of file reached, do nothing, IDK if this is good code
        } catch (IOException | ClassNotFoundException e) {

        }
        return objects;
    }

    public static void updateObjectInFile(String filename,  Object objectToUpdate){
        List<Object> objects = loadObjectsFromFile(filename);

        ListIterator<Object> iterator =  objects.listIterator();

        while (iterator.hasNext()) {
            Object object = iterator.next();
            if (object instanceof Member member) {
                Member memberToUpdate = (Member) objectToUpdate;

                if (member.getMemberID() == memberToUpdate.getMemberID()) {
                    iterator.remove(); // Remove the existing object
                    iterator.add(memberToUpdate);
                }
        }
            else if (object instanceof Coach coach){
                Coach coachToUpdate = (Coach) objectToUpdate;
                if (coach.equals(coachToUpdate)) {
                    iterator.remove(); // Remove the existing object
                    iterator.add(coachToUpdate);
            }
        }

    saveObjectsToFile(filename, objects);
    }
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
        public static void clearFile(String filename) {
            try (FileWriter fw = new FileWriter("src\\"+filename, false)) {
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

}
