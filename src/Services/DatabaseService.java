package Services;

import Components.CustomJOptionPane;
import Models.Student;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class DatabaseService {
    public static final String databasePath = "students.bin";
    public static ArrayList<Student> students = new ArrayList<>();

    public static void saveToDatabase() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(databasePath))) {
            outputStream.writeObject(students);
        } catch (IOException ex) {
            ex.printStackTrace();
            CustomJOptionPane.showErrorDialog(null, ex.getMessage());
        }
    }

    public static void readFromDatabase() {
        if (!Files.exists(Path.of(databasePath))) {
            saveToDatabase();
        }

        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(databasePath))) {
            students = (ArrayList<Student>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            CustomJOptionPane.showErrorDialog(null, ex.getMessage());
        }
    }
}
