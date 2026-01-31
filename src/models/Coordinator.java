package models;

public class Coordinator {
    private String name;
    private String id;

    public Coordinator(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() { return name; }
    public String getId() { return id; }
}