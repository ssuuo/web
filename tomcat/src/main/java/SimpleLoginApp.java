import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class SimpleLoginApp extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getRequestURI();
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        resp.setContentType("text/html");
        try (PrintWriter out = resp.getWriter()) {
            try (Connection conn = DriverManager.getConnection(
                    "jdbc:mariadb://db-service:3306/mydatabase", "root", "A12345!")) {
                if (path.endsWith("/register")) {
                    PreparedStatement ps = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");
                    ps.setString(1, username);
                    ps.setString(2, password);
                    ps.executeUpdate();
                    out.println("Registered.<br><a href='/login.jsp'>Go to Login</a>");
                } else if (path.endsWith("/login")) {
                    PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
                    ps.setString(1, username);
                    ps.setString(2, password);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next())
                        out.println("Login Success!");
                    else
                        out.println("Login Fail!");
                }
            } catch (Exception e) {
                out.println("Error: " + e.getMessage());
            }
        }
    }
}
