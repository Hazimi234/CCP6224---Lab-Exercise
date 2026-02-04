package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Session implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String date;
    private String time;
    private String type; // "Oral" or "Poster"
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

    public String getId() { return id; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getType() { return type; }
    public List<String> getAssignedEvaluatorIds() { return assignedEvaluatorIds; }
    public List<String> getAssignedSubmissionIds() { return assignedSubmissionIds; }

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