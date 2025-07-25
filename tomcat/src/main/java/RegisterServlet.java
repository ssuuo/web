import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import javax.naming.*;
import javax.sql.DataSource;

public class RegisterServlet extends HttpServlet {
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String name = request.getParameter("name");

    boolean registerSuccess = false;

    try {
      Context initContext = new InitialContext();
      DataSource ds = (DataSource) initContext.lookup("java:comp/env/jdbc/mydb");
      Connection conn = ds.getConnection();

      PreparedStatement ps = conn.prepareStatement(
        "INSERT INTO users (username, password, name) VALUES (?, ?, ?)"
      );
      ps.setString(1, username);
      ps.setString(2, password);
      ps.setString(3, name);

      int result = ps.executeUpdate();
      registerSuccess = result > 0;
      conn.close();
    } catch (Exception e) {
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
