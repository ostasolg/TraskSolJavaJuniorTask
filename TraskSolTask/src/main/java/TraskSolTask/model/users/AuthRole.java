package TraskSolTask.model.users;


public enum AuthRole {
    USER("ROLE_USER"), ADMIN("ROLE_ADMIN");

    private final String name;

    AuthRole(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}