package TraskSolTask.model.technologies;

public enum TechnologyLevel {

    BEGINNER("BEGINNER"), AVERAGE("AVERAGE"), SKILLED("SKILLED"), SPECIALIST("SPECIALIST"),
    EXPERT("EXPERT");

    private final String name;

    TechnologyLevel(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
