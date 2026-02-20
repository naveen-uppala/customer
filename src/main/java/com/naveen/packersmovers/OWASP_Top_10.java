import java.io.*;
import java.sql.*;
import java.util.Base64;
import javax.servlet.http.HttpServletRequest;

public class VulnerableExamples {

    // ============================================================
    // A01:2021 – Broken Access Control
    // ============================================================
    // No authorization check before performing admin action
    public void deleteUser(String userId) throws Exception {
        // ❌ No role validation
        // Any authenticated (or even unauthenticated) user could delete accounts
        System.out.println("Deleting user: " + userId);
    }


    // ============================================================
    // A02:2021 – Cryptographic Failures (Sensitive Data Exposure)
    // ============================================================
    public void storePassword(String password) {
        // ❌ Storing password in Base64 (NOT encryption, NOT hashing)
        String encoded = Base64.getEncoder().encodeToString(password.getBytes());
        System.out.println("Stored password: " + encoded);

        // Proper approach:
        // Use bcrypt / PBKDF2 / Argon2 for hashing
    }


    // ============================================================
    // A03:2021 – Injection (SQL Injection)
    // ============================================================
    public void sqlInjection(String userInput) throws Exception {
        Connection conn = null;

        // ❌ Direct string concatenation = SQL Injection
        String query = "SELECT * FROM users WHERE name = '" + userInput + "'";

        conn.createStatement().executeQuery(query);

        // Proper approach:
        // Use PreparedStatement with parameter binding
    }


    // ============================================================
    // A04:2021 – Insecure Design
    // ============================================================
    public boolean login(String username, String password) {
        // ❌ Hardcoded admin password
        if (username.equals("admin") && password.equals("admin123")) {
            return true;
        }
        return false;

        // Proper approach:
        // Store securely hashed passwords in DB
    }


    // ============================================================
    // A05:2021 – Security Misconfiguration
    // ============================================================
    public void debugMode() {
        // ❌ Stack trace exposure
        try {
            int x = 5 / 0;
        } catch (Exception e) {
            e.printStackTrace();  // exposes internal details
        }
    }


    // ============================================================
    // A06:2021 – Vulnerable and Outdated Components
    // ============================================================
    // (This one is dependency-related, not code-based)
    // Example:
    // Using outdated Log4j 2.14.1 → Log4Shell vulnerability
    //
    // <dependency>
    //   <groupId>org.apache.logging.log4j</groupId>
    //   <artifactId>log4j-core</artifactId>
    //   <version>2.14.1</version>  ❌ vulnerable
    // </dependency>


    // ============================================================
    // A07:2021 – Identification and Authentication Failures
    // ============================================================
    public void weakSessionToken() {
        // ❌ Predictable session ID
        String sessionId = "USER_" + System.currentTimeMillis();
        System.out.println("Session ID: " + sessionId);

        // Proper approach:
        // Use secure random token generators
    }


    // ============================================================
    // A08:2021 – Software and Data Integrity Failures
    // ============================================================
    public void unsafeDeserialization(String filePath) throws Exception {
        // ❌ Unsafe Java deserialization
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath));
        Object obj = ois.readObject();

        // This can lead to Remote Code Execution
        ois.close();
    }


    // ============================================================
    // A09:2021 – Security Logging and Monitoring Failures
    // ============================================================
    public void failedLogin(String username) {
        // ❌ No logging of failed login attempts
        System.out.println("Login failed");

        // Proper approach:
        // Log with structured logging and monitoring system
    }


    // ============================================================
    // A10:2021 – Server-Side Request Forgery (SSRF)
    // ============================================================
    public void ssrf(HttpServletRequest request) throws Exception {
        String url = request.getParameter("url");

        // ❌ Unvalidated URL fetch
        java.net.URL site = new java.net.URL(url);
        java.io.BufferedReader in = new java.io.BufferedReader(
                new java.io.InputStreamReader(site.openStream()));

        in.close();

        // Proper approach:
        // Validate allowed domains, block internal IP ranges
    }
}
