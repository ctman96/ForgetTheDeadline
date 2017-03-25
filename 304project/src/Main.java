import java.sql.*;

import oracle.jdbc.driver.OracleDriver;



public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world");
        Connection con = null;
        try {
        	System.out.println("Register Driver...");
            DriverManager.registerDriver(new OracleDriver());
            
            System.out.println("Creating Connection...");
            con = DriverManager.getConnection("jdbc:oracle:thin:@dbhost.ugrad.cs.ubc.ca:1522:ug", "ora_o2e9", "a40149122");
            
            createDatabase(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
        	try{
        		if(con != null)
        			con.close();
        	} catch(SQLException se){
        		
        	}
        }

    }
    
    public static void createDatabase(Connection con){
    	Statement stmt = null;
    	try{
    	stmt = con.createStatement();
    	} catch (SQLException e){
    		e.printStackTrace();
    	} finally{
    		try{
    			if(stmt != null)
    				stmt.close();
    		} catch(SQLException se){
    		}
    	}
    	System.out.println("Database Created and Populated");
    }
}