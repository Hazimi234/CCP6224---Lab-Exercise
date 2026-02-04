package controller;

import models.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CoordinatorController {
    private DataStore dataStore;

    public CoordinatorController(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public void createSession(String date, String time, String type) {
        if (date == null || date.trim().isEmpty()) throw new IllegalArgumentException("Date is required");
        if (time == null || time.trim().isEmpty()) throw new IllegalArgumentException("Time is required");
        
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

    public void updateSessionEvaluators(Session session, List<String> evaluatorIds) {
        session.setAssignedEvaluatorIds(evaluatorIds);
        dataStore.saveSessions();
    }

    public void updateSessionSubmissions(Session session, List<String> submissionIds) {
        session.setAssignedSubmissionIds(submissionIds);
        dataStore.saveSessions();

        // Auto-generate Board IDs for Poster sessions
        if ("Poster".equals(session.getType())) {
            List<Submission> allSubmissions = dataStore.getSubmissions();
            int counter = 1;
            for (String subId : submissionIds) {
                for (Submission s : allSubmissions) {
                    if (s.getId().equals(subId)) {
                        s.setBoardId(String.format("B%02d", counter++));
                        break;
                    }
                }
            }
            dataStore.saveSubmissions();
        }
    }
}