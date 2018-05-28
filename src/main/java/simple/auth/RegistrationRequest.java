package simple.auth;

public class RegistrationRequest {
    private final String username;
    private final int age;
    private final String password;

    public RegistrationRequest(String username, int age, String password) {
        this.username = username;
        this.age = age;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public int getAge() {
        return age;
    }

    public String getPassword() {
        return password;
    }
}
