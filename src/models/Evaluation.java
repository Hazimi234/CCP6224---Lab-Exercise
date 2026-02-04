package models;

import java.io.Serializable;

public class Evaluation implements Serializable {
    private static final long serialVersionUID = 1L;
    private String evaluatorId;
    private String evaluatorName;
    private int score1;
    private int score2;
    private int score3;
    private int score4;
    private String comments;

    public Evaluation(String evaluatorId, String evaluatorName, int score1, int score2, int score3, int score4, String comments) {
        this.evaluatorId = evaluatorId;
        this.evaluatorName = evaluatorName;
        this.score1 = score1;
        this.score2 = score2;
        this.score3 = score3;
        this.score4 = score4;
        this.comments = comments;
    }

    public String getEvaluatorId() { return evaluatorId; }
    public String getEvaluatorName() { return evaluatorName; }
    public int getScore1() { return score1; }
    public int getScore2() { return score2; }
    public int getScore3() { return score3; }
    public int getScore4() { return score4; }
    public String getComments() { return comments; }
    public int getTotalScore() { return score1 + score2 + score3 + score4; }

    @Override
    public String toString() {
        return evaluatorName + ": " + getTotalScore() + "/20";
    }
}