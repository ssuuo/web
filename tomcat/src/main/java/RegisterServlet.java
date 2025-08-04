import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class RegisterServlet extends HttpServlet {
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String name = request.getParameter("name");

    boolean registerSuccess = false;

    try {
      // ✅ JDBC 드라이버 수동 로딩
      Class.forName("org.mariadb.jdbc.Driver");

      // ✅ 직접 JDBC 연결
      String url = "jdbc:mariadb://db-service:3306/mydatabase";
      String dbUser = "root";
      String dbPassword = "A12345!";

      Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);

      PreparedStatement ps = conn.prepareStatement(
        "INSERT INTO users (username, password, name) VALUES (?, ?, ?)"
      );
      ps.setString(1, username);
      ps.setString(2, password);
      ps.setString(3, name);

      int result = ps.executeUpdate();
      registerSuccess = result > 0;

      ps.close();
      conn.close();
    } catch (Exception e) {
      e.printStackTrace(); // 개발 시 디버깅에 도움
      registerSuccess = false;
    }

    response.setContentType("text/html; charset=UTF-8");
    PrintWriter out = response.getWriter();
    if (registerSuccess) {
      out.println("<script>alert('회원가입 성공! 로그인 해주세요.'); location.href='/login.html';</script>");
    } else {
      out.println("<script>alert('회원가입 실패! 아이디가 중복되었거나 오류 발생'); history.back();</script>");
    }
  }
}
