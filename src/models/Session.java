package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Seminar Session (Time slot).
 * A session has a specific date, time, and type (Oral/Poster).
 * It acts as a container linking Evaluators to Submissions.
 */
public class Session implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String date;
    private String time;
    private String type;

    /**
     * Store IDs instead of full objects to keep the file size small
     * and avoid circular reference issues during serialization.
     */

    private List<String> assignedEvaluatorIds;
    private List<String> assignedSubmissionIds;

    public Session(String id, String date, String time, String type) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.type = type;
        this.assignedEvaluatorIds = new ArrayList<>();
        this.assignedSubmissionIds = new ArrayList<>();
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public List<String> getAssignedEvaluatorIds() {
        return assignedEvaluatorIds;
    }

    public List<String> getAssignedSubmissionIds() {
        return assignedSubmissionIds;
    }

    // Setters for Assignments
    public void setAssignedEvaluatorIds(List<String> ids) {
        this.assignedEvaluatorIds = ids;
    }

    public void setAssignedSubmissionIds(List<String> ids) {
        this.assignedSubmissionIds = ids;
    }

    @Override
    public String toString() {
        return date + " " + time + " (" + type + ")";
    }
}