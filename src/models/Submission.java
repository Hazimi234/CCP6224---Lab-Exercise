package models;
import java.io.Serializable;
import java.util.UUID;

public class Submission implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String title;
    private String abstractText;
    private String supervisor;
    private String presentationType;
    private String filePath;
    private String status;
    private int score1; //scores in evaluator panel
    private int score2; //scores in evaluator panel
    private int score3; //scores in evaluator panel
    private int score4; //scores in evaluator panel
    private String comments; // Add this field if not present

    public Submission(String name, String title, String abstractText, String supervisor, String presentationType, String filePath) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.name = name;
        this.title = title;
        this.abstractText = abstractText;
        this.supervisor = supervisor;
        this.presentationType = presentationType;
        this.filePath = filePath;
        this.status = "Pending";
    }

    // Getters
    public String getId() { 
        if (id == null) {
            id = UUID.randomUUID().toString().substring(0, 8);
        }
        return id; 
    }
    public String getName() { return name; }
    public String getTitle() { return title; }
    public String getAbstractText() { return abstractText; }
    public String getSupervisor() { return supervisor; }
    public String getPresentationType() { return presentationType; }
    public String getFilePath() { return filePath; }
    public String getStatus() { return status; }
    public String getComments() { return comments; } //comments in evaluator panel

    // Setter for status
    public void setStatus(String status) { this.status = status; }
    public void setComments(String comments) { this.comments = comments; } //comments in evaluator panel

    public void setScores(int score1, int score2, int score3, int score4) {
        this.score1 = score1;
        this.score2 = score2;
        this.score3 = score3;
        this.score4 = score4;
    } //scores in evaluator panel

    @Override
    public String toString() {
        return title + " (" + presentationType + ") by " + name;
    }
}