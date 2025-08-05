import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class LoginServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    // 0) 입력 파라미터
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    System.out.println("🔧 LoginServlet 동작 시작");
    System.out.println("📌 입력값: username=" + username + ", password=" + password);

    boolean loginSuccess = false;
    String name = "";

    // 1) JDBC 정보 (RegisterServlet과 동일)
    String url = "jdbc:mariadb://db-service:3306/mydatabase";
    String dbUser = "root";
    String dbPassword = "A12345!";

    try {
      // 2) 드라이버 로딩
      System.out.println("🔍 JDBC 드라이버 로딩 시도");
      Class.forName("org.mariadb.jdbc.Driver");
      System.out.println("✅ JDBC 드라이버 로딩 성공");

      // 3) DB 연결 & 조회
      System.out.println("🔍 DB 연결 시도: " + url);
      try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword)) {
        System.out.println("✅ DB 연결 성공");

        String sql = "SELECT name FROM users WHERE username=? AND password=?";
        System.out.println("🛠 SQL 준비: " + sql);

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
          ps.setString(1, username);
          ps.setString(2, password);

          System.out.println("🚀 SQL 실행 시도");
          try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
              loginSuccess = true;
              name = rs.getString("name");
              System.out.println("✅ 로그인 성공: " + username + " / name=" + name);
            } else {
              System.out.println("❌ 로그인 실패: 일치하는 사용자 없음");
            }
          }
        }
      }
    } catch (Exception e) {
      loginSuccess = false;
      System.out.println("❌ 예외 발생!");
      e.printStackTrace();
    }

    // 4) 응답
    response.setContentType("text/html; charset=UTF-8");
    PrintWriter out = response.getWriter();
    if (loginSuccess) {
      out.println("<h2>로그인 성공! " + name + "님 환영합니다.</h2>");
      // 필요하면 세션 처리:
      // HttpSession session = request.getSession(true);
      // session.setAttribute("username", username);
      // session.setAttribute("name", name);
      // out.println("<script>location.href='/board.html';</script>");
    } else {
      out.println("<script>alert('로그인 정보를 확인해 주세요.'); history.back();</script>");
    }

    System.out.println("🔚 LoginServlet 종료");
  }
}
