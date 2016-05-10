package parser;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnect {
	private static String loginUser = "root";
	private static String loginPasswd = "cs122b";
	private static String loginUrl = "jdbc:mysql:///moviedb";

	public static Connection get(Connection conn) {
		try {
			if (conn == null || conn.isClosed()) {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				conn = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
				conn.setAutoCommit(false);
			}
		} catch (Exception e)

		{
			e.printStackTrace();
		}
		return conn;
	}

	public static void close(Connection conn) {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
