package models;

public class Evaluator {
    private String name;
    private String id;

    public Evaluator(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() { return name; }
    public String getId() { return id; }
}