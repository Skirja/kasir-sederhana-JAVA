import java.util.HashMap;
import java.util.Map;

public class Authentication {
    private static final int MAX_LOGIN_ATTEMPTS = 3; // Maximum login attempts
    private int loginAttempts;
    private boolean authenticated;

    private final Map<String, String> users = new HashMap<>();

    public Authentication() {
        this.authenticated = false;
        this.loginAttempts = 0;

        // Initialize user accounts
        users.put("OWNER1", "SAYAOWNER");
        users.put("MANAGER1", "SAYAMANAGER");
        users.put("KASIR1", "SAYAKASIR");
    }

    public String authenticate(String username, String password) {
        if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
            System.out.println("Maximum login attempts reached. Exiting application.");
            System.exit(0); // Exit the program if maximum login attempts are reached
        }

        if (!authenticated) {
            if (users.containsKey(username) && users.get(username).equals(password)) {
                authenticated = true;
                return getUserRole(username);
            } else {
                loginAttempts++; // Increment login attempts
                return null;
            }
        } else {
            return "Already Authenticated";
        }
    }

    // Map usernames to roles
    private String getUserRole(String username) {
        switch (username) {
            case "OWNER1":
                return "OWNER";
            case "MANAGER1":
                return "MANAGER";
            case "KASIR1":
                return "KASIR";
            default:
                return null;
        }
    }
}
