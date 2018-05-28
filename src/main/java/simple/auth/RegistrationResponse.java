package simple.auth;

public class RegistrationResponse {
    private final Types status;
    private final User registered;

    public RegistrationResponse(Types status) {
        this.status = status;
        this.registered = null;
    }

    public RegistrationResponse(User registered) {
        this.status = Types.SUCCESS;
        this.registered = registered;
    }

    public Types getStatus() {
        return status;
    }

    public User getRegistered() {
        return registered;
    }

    public enum Types { SUCCESS }
}
