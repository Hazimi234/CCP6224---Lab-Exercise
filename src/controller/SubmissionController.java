package controller;
import models.Submission;

public class SubmissionController {
    private DataStore dataStore;

    public SubmissionController(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public void submitProposal(String name, String title, String abstractText, String supervisor, String presentationType, String filePath) {
        // 1. Validate Inputs
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Student Name cannot be empty.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Research Title cannot be empty.");
        }
        if (abstractText == null || abstractText.trim().isEmpty()) {
            throw new IllegalArgumentException("Abstract cannot be empty.");
        }
        if (supervisor == null || supervisor.trim().isEmpty()) {
            throw new IllegalArgumentException("Supervisor cannot be empty.");
        }
        if (filePath == null || filePath.trim().isEmpty() || filePath.equals("No file selected")) {
            throw new IllegalArgumentException("Please select a project file.");
        }
        // 2. Create Model
        Submission submission = new Submission(name, title, abstractText, supervisor, presentationType, filePath);
        
        // 3. Store Data
        dataStore.addSubmission(submission);
    }
}