package controller;

import models.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * CoordinatorController handles administrative tasks.
 * It manages the creation of Sessions and the assignment of Evaluators
 * and Submissions to those sessions.
 */
public class CoordinatorController {
    private DataStore dataStore;

    public CoordinatorController(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    /**
     * Creates a new Seminar Session (Oral or Poster).
     * Generates a unique ID for the session automatically.
     */
    public void createSession(String date, String time, String type) {
        // Input Validation
        if (date == null || date.trim().isEmpty())
            throw new IllegalArgumentException("Date is required");
        if (time == null || time.trim().isEmpty())
            throw new IllegalArgumentException("Time is required");

        // Generate random 8-char ID
        String id = UUID.randomUUID().toString().substring(0, 8);

        Session session = new Session(id, date, time, type);
        dataStore.addSession(session);
    }

    public void deleteSession(String sessionId) {
        Session toRemove = null;
        for (Session s : dataStore.getSessions()) {
            if (s.getId().equals(sessionId)) {
                toRemove = s;
                break;
            }
        }
        if (toRemove != null) {
            dataStore.removeSession(toRemove);
        }
    }

    public List<Session> getAllSessions() {
        return dataStore.getSessions();
    }

    // Helper to filter only Evaluators from the main User list
    public List<User> getAllEvaluators() {
        List<User> evaluators = new ArrayList<>();
        for (User u : dataStore.getUsers()) {
            if (u instanceof Evaluator) {
                evaluators.add(u);
            }
        }
        return evaluators;
    }

    public List<Submission> getAllSubmissions() {
        return dataStore.getSubmissions();
    }

    /**
     * Updates which Evaluators are assigned to a specific Session.
     * We store only the IDs of the evaluators in the Session object.
     */
    public void updateSessionEvaluators(Session session, List<String> evaluatorIds) {
        session.setAssignedEvaluatorIds(evaluatorIds);
        dataStore.saveSessions();
    }

    /**
     * Updates which Submissions are assigned to a specific Session.
     * Also handles the auto-generation of Board IDs (B01, B02...) for Poster
     * sessions.
     */
    public void updateSessionSubmissions(Session session, List<String> submissionIds) {
        session.setAssignedSubmissionIds(submissionIds);
        dataStore.saveSessions();

        // Business Rule: If this is a Poster session, assign Board IDs automatically
        if ("Poster".equals(session.getType())) {
            List<Submission> allSubmissions = dataStore.getSubmissions();
            int counter = 1;

            // Loop through assigned submission IDs
            for (String subId : submissionIds) {
                // Find the actual submission object
                for (Submission s : allSubmissions) {
                    if (s.getId().equals(subId)) {
                        // Format: B01, B02, etc.
                        s.setBoardId(String.format("B%02d", counter++));
                        break;
                    }
                }
            }
            // Save changes to submissions (since we modified Board IDs)
            dataStore.saveSubmissions();
        }
    }
}