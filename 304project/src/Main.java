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
            
            System.out.println("Database Create and Populate...");
            createpopDatabase(con);
            
            String SKU = null;
            String EID = null;
            String payment = null;
            String CID = null;
            //buyProduct(con,SKU,EID,payment,CID);
            
            
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
    
    public static void createpopDatabase(Connection con){
    	Statement stmt = null;
    	try{
    	System.out.println("Create Statement...");
    	stmt = con.createStatement();
    	
    	String sql = "CREATE TABLE Branch";
    	stmt.executeUpdate(sql);
    	
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
    
    public static void buyProduct(Connection con, String SKU, String EID, String payment, String CID){
    	Statement stmt = null;
    	try{
    		con.setAutoCommit(false);
    		System.out.println("Create Statement...");
    		//String sql = "Select BID from "
    		//stmt = con.prepareStatement(sql);
    		
    	} catch (SQLException e){
    		e.printStackTrace();
    	} finally{
    		try{
    			if(stmt != null){
    				stmt.close();
    				con.setAutoCommit(true);
    			}
    		} catch(SQLException se){
    		}
    	}
    }
}