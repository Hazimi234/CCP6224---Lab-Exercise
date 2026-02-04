package controller;

import models.Submission;

/**
 * SubmissionController handles the logic for student proposals.
 * It ensures all required fields are present before saving a submission.
 */
public class SubmissionController {
    private DataStore dataStore;

    public SubmissionController(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    /**
     * Validates input and creates a new Submission object.
     * This method is called when a student clicks "Submit Proposal".
     */
    public void submitProposal(String name, String title, String abstractText, String supervisor,
            String presentationType, String filePath) {
        // 1. Validate Inputs (Business Rules)
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
        // Ensure a file has actually been selected
        if (filePath == null || filePath.trim().isEmpty() || filePath.equals("No file selected")) {
            throw new IllegalArgumentException("Please select a project file.");
        }

        // 2. Create Model
        Submission submission = new Submission(name, title, abstractText, supervisor, presentationType, filePath);

        // 3. Store Data (Save to file via DataStore)
        dataStore.addSubmission(submission);
    }

    // Helper method to force a save (used when updating votes or evaluations)
    public void saveSubmissions() {
        dataStore.saveSubmissions();
    }

    public DataStore getDataStore() {
        return dataStore;
    }
}