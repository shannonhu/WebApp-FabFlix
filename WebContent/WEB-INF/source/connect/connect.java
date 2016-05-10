package connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletException;

public class connect {
	
	
	private static	String loginUser = "root";
	private static	String loginPasswd = "cs122b";
	private static	String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
    
    
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

