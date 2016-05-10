package connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletException;

public class connect {
	
	
	private static	String loginUser = "classta";
	private static	String loginPasswd = "classta";
	private static	String loginUrl = "jdbc:mysql://localhost:3306/moviedb_project3_grading";
    
    
	public static Connection check(Connection connection) throws SQLException, ServletException{
		
		try 
        {
        	if (connection == null || connection.isClosed())
        	{
        		Class.forName("com.mysql.jdbc.Driver").newInstance();
        		connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
        	}			
        } 
        catch (Exception e) 
        {
        	System.out.println(e.getMessage());
        }
		return connection;
    	}	
	}

