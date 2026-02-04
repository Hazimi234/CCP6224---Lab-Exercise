package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a Project Submission by a student.
 * It contains details of the research, the attached file,
 * and a list of evaluations received from panels.
 */
public class Submission implements Serializable {
    private static final long serialVersionUID = 1L;

    // Core attributes
    private String id;
    private String name;
    private String title;
    private String abstractText;
    private String supervisor;
    private String presentationType;
    private String filePath;

    private String status;
    private String boardId;

    // List of scorecards from evaluators
    private List<Evaluation> evaluations;

    // Voting Counter for People's Choice Award
    private int voteCount = 0;

    public Submission(String name, String title, String abstractText, String supervisor, String presentationType,
            String filePath) {
        // Generate a random unique ID for this submission
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.name = name;
        this.title = title;
        this.abstractText = abstractText;
        this.supervisor = supervisor;
        this.presentationType = presentationType;
        this.filePath = filePath;

        // Default values
        this.status = "Pending";
        this.evaluations = new ArrayList<>();
        this.voteCount = 0;
    }

    // --- GETTERS ---
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public String getPresentationType() {
        return presentationType;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getStatus() {
        return status;
    }

    public String getBoardId() {
        return boardId;
    }

    public List<Evaluation> getEvaluations() {
        if (evaluations == null)
            evaluations = new ArrayList<>();
        return evaluations;
    }

    // --- SETTERS & LOGIC ---

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    /**
     * Adds a new evaluation score to this submission.
     * If an evaluator updates their score, the old one is removed first.
     */
    public void addEvaluation(Evaluation evaluation) {
        if (this.evaluations == null)
            this.evaluations = new ArrayList<>();
        // Remove prior evaluation from same evaluator ID to allow updates/editing
        this.evaluations.removeIf(e -> e.getEvaluatorId().equals(evaluation.getEvaluatorId()));
        this.evaluations.add(evaluation);
    }

    // Methods for People's Choice Voting
    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public String toString() {
        return title + " (" + presentationType + ") by " + name;
    }
}