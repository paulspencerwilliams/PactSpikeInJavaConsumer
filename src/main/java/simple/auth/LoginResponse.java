package simple.auth;

public class LoginResponse {
    private final User loggedInUser;
    private final Types status;

    public LoginResponse(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.status = Types.SUCCESS;
    }

    public LoginResponse(Types status) {
        this.loggedInUser = null;
        this.status = status;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public Types getStatus() {
        return status;
    }

    public enum Types { SUCCESS, BAD_CREDENTIALS}
}
