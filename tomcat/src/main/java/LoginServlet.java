import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import javax.naming.*;
import javax.sql.DataSource;

public class LoginServlet extends HttpServlet {
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

    String username = request.getParameter("username");
    String password = request.getParameter("password");

    boolean loginSuccess = false;
    String name = "";

    try {
      Context initContext = new InitialContext();
      DataSource ds = (DataSource) initContext.lookup("java:comp/env/jdbc/mydb");
      Connection conn = ds.getConnection();

      PreparedStatement ps = conn.prepareStatement(
        "SELECT name FROM users WHERE username=? AND password=?"
      );
      ps.setString(1, username);
      ps.setString(2, password);

      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        loginSuccess = true;
        name = rs.getString("name");
      }
      conn.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    response.setContentType("text/html; charset=UTF-8");
    PrintWriter out = response.getWriter();
    if (loginSuccess) {
      out.println("<h2>로그인 성공! " + name + "님 환영합니다.</h2>");
    } else {
      out.println("<script>alert('로그인 정보를 확인해 주세요.'); history.back();</script>");
    }
  }
}
