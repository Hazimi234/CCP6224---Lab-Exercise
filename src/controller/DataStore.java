package controller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataStore {
    private List<Submission> submissions;
    private static final String FILE_NAME = "submissions.dat";

    public DataStore() {
        submissions = new ArrayList<>();
        load();
    }

    public void addSubmission(Submission submission) {
        submissions.add(submission);
        save();
    }

    public List<Submission> getSubmissions() {
        return submissions;
    }

    private void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(submissions);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void load() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                submissions = (List<Submission>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading data: " + e.getMessage());
            }
        }
    }
}