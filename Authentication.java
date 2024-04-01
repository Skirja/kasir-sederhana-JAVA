public class Authentication {
    private static final String OWNER_USERNAME = "OWNER1";
    private static final String OWNER_PASSWORD = "SAYAOWNER";
    private static final String MANAGER_USERNAME = "MANAGER1";
    private static final String MANAGER_PASSWORD = "SAYAMANAGER";
    private static final String KASIR_USERNAME = "KASIR1";
    private static final String KASIR_PASSWORD = "SAYAKASIR";

    private boolean authenticated;
    private int loginAttempts;
    private static final int MAX_LOGIN_ATTEMPTS = 3; // Batas maksimum gagal login

    public Authentication() {
        this.authenticated = false;
        this.loginAttempts = 0;
    }

    public String authenticate(String username, String password) {
        if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
            System.out.println("Batas maksimum percobaan login telah tercapai. Aplikasi akan keluar.");
            System.exit(0); // Keluar dari program jika batas login tercapai
        }

        if (!authenticated) {
            if (username.equals(OWNER_USERNAME) && password.equals(OWNER_PASSWORD)) {
                authenticated = true;
                return "OWNER";
            } else if (username.equals(MANAGER_USERNAME) && password.equals(MANAGER_PASSWORD)) {
                authenticated = true;
                return "MANAGER";
            } else if (username.equals(KASIR_USERNAME) && password.equals(KASIR_PASSWORD)) {
                authenticated = true;
                return "KASIR";
            } else {
                loginAttempts++; // Tambahkan jumlah percobaan login
                return null;
            }
        } else {
            return "Already Authenticated";
        }
    }
}
