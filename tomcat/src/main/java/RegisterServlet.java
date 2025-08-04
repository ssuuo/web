import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class RegisterServlet extends HttpServlet {
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

    // 0. 파라미터 출력
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String name = request.getParameter("name");

    System.out.println("🔸 RegisterServlet 시작");
    System.out.println("📌 입력값: username=" + username + ", password=" + password + ", name=" + name);

    boolean registerSuccess = false;

    try {
      // 1. JDBC 드라이버 로딩
      System.out.println("🔍 JDBC 드라이버 로딩 시도");
      Class.forName("org.mariadb.jdbc.Driver");
      System.out.println("✅ JDBC 드라이버 로딩 성공");

      // 2. DB 연결 시도
      String url = "jdbc:mariadb://db-service:3306/mydatabase";
      String dbUser = "root";
      String dbPassword = "A12345!";
      System.out.println("🔍 DB 연결 시도: " + url);

      Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
      System.out.println("✅ DB 연결 성공");

      // 3. INSERT 쿼리 준비 및 실행
      String sql = "INSERT INTO users (username, password, name) VALUES (?, ?, ?)";
      System.out.println("🛠 SQL 준비: " + sql);

      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1, username);
      ps.setString(2, password);
      ps.setString(3, name);

      System.out.println("🚀 SQL 실행 시도");
      int result = ps.executeUpdate();
      System.out.println("✅ SQL 실행 완료. 삽입된 행 수: " + result);

      registerSuccess = result > 0;

      ps.close();
      conn.close();
    } catch (Exception e) {
      registerSuccess = false;
      System.out.println("❌ 예외 발생!");
      e.printStackTrace();
    }

    // 4. 응답 전송
    response.setContentType("text/html; charset=UTF-8");
    PrintWriter out = response.getWriter();

    if (registerSuccess) {
      out.println("<script>alert('회원가입 성공! 로그인 해주세요.'); location.href='/login.html';</script>");
    } else {
      out.println("<script>alert('회원가입 실패! 아이디가 중복되었거나 오류 발생'); history.back();</script>");
    }

    System.out.println("🔚 RegisterServlet 종료");
  }
}
