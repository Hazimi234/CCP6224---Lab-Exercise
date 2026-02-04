package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
    private List<Evaluation> evaluations;
    private String boardId;
    private int voteCount = 0;

    public Submission(String name, String title, String abstractText, String supervisor, String presentationType,
            String filePath) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.name = name;
        this.title = title;
        this.abstractText = abstractText;
        this.supervisor = supervisor;
        this.presentationType = presentationType;
        this.filePath = filePath;
        this.status = "Pending";
        this.evaluations = new ArrayList<>();
        this.voteCount = 0;
    }

    // Getters
    public String getId() {
        if (id == null) {
            id = UUID.randomUUID().toString().substring(0, 8);
        }
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
        if (evaluations == null) {
            evaluations = new ArrayList<>();
        }
        return evaluations;
    }

    // Setter for status
    public void setStatus(String status) {
        this.status = status;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public void addEvaluation(Evaluation evaluation) {
        if (this.evaluations == null) {
            this.evaluations = new ArrayList<>();
        }
        // Remove existing evaluation by the same evaluator to allow updates
        this.evaluations.removeIf(e -> e.getEvaluatorId().equals(evaluation.getEvaluatorId()));
        this.evaluations.add(evaluation);
    }

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