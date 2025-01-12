package controlers.lobby;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    // Hardcoded admin credentials (username and hashed password)
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD_HASH = "ec511d74ad0c26a4056b8cadf72a3d97dfca8a6099e67a1898bea527926a6965"; // Hash of "password123"

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get user input
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            // Validate username and password
            if (ADMIN_USERNAME.equals(username) && verifyPassword(password, ADMIN_PASSWORD_HASH)) {
                // Store username in session
                HttpSession session = request.getSession();
                session.setAttribute("username", username);

                // Redirect to welcome.jsp
                response.sendRedirect("welcome.jsp");
            } else {
                // Redirect back to index.jsp with an error message
                response.sendRedirect("index.jsp?error=Invalid credentials. Please try again.");
            }
        } catch (NoSuchAlgorithmException e) {
            throw new ServletException("Error processing password hashing", e);
        }
    }

    // Hashing function (SHA-256)
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = md.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashedBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    // Verify if the hashed input password matches the stored hash
    private boolean verifyPassword(String inputPassword, String storedHashedPassword) throws NoSuchAlgorithmException {
        return hashPassword(inputPassword).equals(storedHashedPassword);
    }
}
