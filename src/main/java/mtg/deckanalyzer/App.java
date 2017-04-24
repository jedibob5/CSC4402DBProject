package mtg.deckanalyzer;
import java.sql.Connection;
import java.sql.DriverManager;

import io.magicthegathering.javasdk.api.*;
import io.magicthegathering.javasdk.resource.*;
import io.magicthegathering.javasdk.exception.*;

public class App
{
    public static void main( String[] args ) throws Exception
    {
        Connection conn = getConnection();
    }
    
    public static Connection getConnection() throws Exception
    {
    	try{
	    	String driver = "com.mysql.jdbc.Driver";
	    	String url = "jdbc:mysql://localhost:3306/cs420234";
	    	String username = "cs420234";
	    	String password = "utgj6x";
	    	Class.forName(driver);
	    	
	    	Connection conn = DriverManager.getConnection(url, username, password);
	    	System.out.println("Connection successful.");
	    	return conn;
    	}
    	catch(Exception e)
    	{
    		System.out.println(e);
    		return null;
    	}
    }
}
