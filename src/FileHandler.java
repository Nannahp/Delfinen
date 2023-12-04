import java.io.*;
import java.util.*;

public class FileHandler {

    public static void saveObjectsToFile(String fileName, List<Object> objects) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src\\files\\"+fileName))) {
            for (Object object : objects) {
                oos.writeObject(object);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void appendObjectToFile(String fileName, Object newObject) {
        List<Object> existingObjects = new ArrayList<>(loadObjectsFromFile(fileName));
        existingObjects.add(newObject);

        saveObjectsToFile(fileName, existingObjects);
    }

    public static List<Object> loadObjectsFromFile(String fileName) {
        List<Object> objects = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("src\\files\\"+fileName))) {
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

    public static void modifyObjectInFile(String fileName, Object objectToUpdate, boolean update){
        List<Object> objects = loadObjectsFromFile(fileName);

        ListIterator<Object> iterator =  objects.listIterator();

        while (iterator.hasNext()) {
            Object object = iterator.next();
            if (object instanceof Member member) {
                Member memberToUpdate = (Member) objectToUpdate;

                if (member.getMemberID() == memberToUpdate.getMemberID()) {

                    iterator.remove(); // Remove the existing object
                    if (update)
                    iterator.add(memberToUpdate);
                }
        }
            //this does not work
            else if (object instanceof Coach ){
                Coach coachToUpdate = (Coach) objectToUpdate;
                if (((Coach) object).getName().equals(coachToUpdate.getName())) {
                    iterator.remove(); // Remove the existing object
                    if (update)
                    iterator.add(coachToUpdate);
            }
        }

    saveObjectsToFile(fileName, objects);
    }
}

    public static void createFile(String fileName) {
        try {
            File newFile = new File("src\\files\\"+fileName);
            if (newFile.createNewFile()) {
                UI.printText("File created: " + newFile.getName(), ConsoleColor.GREEN);
            } else {
                UI.printText("File already exists!\n", ConsoleColor.RED);
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
        public static void clearFile(String fileName) {
            try (FileWriter fw = new FileWriter("src\\files\\"+fileName, false)) {
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

}
