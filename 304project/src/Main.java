import java.sql.*;
import java.util.Date;

import oracle.jdbc.driver.OracleDriver;



public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world");
        Connection con = null;
        try {
        	System.out.println("Register Driver...");
            DriverManager.registerDriver(new OracleDriver());
            
            System.out.println("Creating Connection...");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:ug", "ora_o2e9", "a40149122");
            
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
    	PreparedStatement update_stmt = null;
    	PreparedStatement insert_stmt = null;
    	String update_str = "update Stock s " +
    						"set quantity = (select s.quantity " +
    										"from Stock s,Employee e " +
    										"where s.BID = e.BID and s.SKU = ?) - 1" +
    						"where s.SKU = ?";
    	String insert_str = "insert into sales values(?, (select max(snum)" +
    						"from sales) + 1, ?, ?, ?, ?)";
    	try{
    		con.setAutoCommit(false);
    		System.out.println("Create Statement...");
    		update_stmt = con.prepareStatement(update_str);
    		insert_stmt = con.prepareStatement(insert_str);
    		
    		update_stmt.setString(1,SKU);
    		update_stmt.setString(2,SKU);
    		update_stmt.executeUpdate();
    		
    		insert_stmt.setString(1, payment);
    		insert_stmt.setString(2, SKU);
    		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		Date date = new Date();
    		insert_stmt.setDate(3, date);
    		insert_stmt.setString(4,CID);
    		insert_stmt.setString(5,EID);
    		
    		con.commit();
    		
    	} catch (SQLException e){
    		e.printStackTrace();
    	} finally{
    		try{
    			if(update_stmt != null){
    				update_stmt.close();
    				con.setAutoCommit(true);
    			}
    			if(insert_stmt != null){
    				insert_stmt.close();
    			}
    			con.setAutoCommit(true);
    			
    		} catch(SQLException se){
    		}
    	}
    }
}