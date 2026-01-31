package models;
import java.io.Serializable;

public class Submission implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String title;
    private String abstractText;
    private String supervisor;
    private String presentationType;
    private String filePath;
    private String status;

    public Submission(String name, String title, String abstractText, String supervisor, String presentationType, String filePath) {
        this.name = name;
        this.title = title;
        this.abstractText = abstractText;
        this.supervisor = supervisor;
        this.presentationType = presentationType;
        this.filePath = filePath;
        this.status = "Pending";
    }

    // Getters
    public String getName() { return name; }
    public String getTitle() { return title; }
    public String getAbstractText() { return abstractText; }
    public String getSupervisor() { return supervisor; }
    public String getPresentationType() { return presentationType; }
    public String getFilePath() { return filePath; }
    public String getStatus() { return status; }

    // Setter for status
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return title + " (" + presentationType + ") by " + name;
    }
}