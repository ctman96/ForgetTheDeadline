import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.text.*;

import oracle.jdbc.driver.OracleDriver;



public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world");
        Connection con = null;
        try {
        	System.out.println("Register Driver...");
            DriverManager.registerDriver(new OracleDriver());
            
            Date date = new Date();
            System.out.println(date);
            
            System.out.println("Creating Connection...");
            //con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:ug", "ora_k2a0b", "a35833145");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:ug", "ora_o2e9", "a40149122");
            String SKU = "10000000";
            String EID = "30000000";
            String payment = "CC123123";
            String CID = "35553916";
            buyProduct(con,SKU,EID,payment,CID);
            
            
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
    

    
    public static void buyProduct(Connection con, String SKU, String EID, String payment, String CID){
    	PreparedStatement update_stmt = null;
    	PreparedStatement insert_stmt = null;
    	String update_str = "UPDATE Stock s " +
				"SET Quantity = (SELECT s.Quantity " +
				"                FROM Stock s, Employee e " +
				"                WHERE e.EID = ? AND s.BID = e.BID AND s.SKU = ?)-1 " +
				"WHERE s.SKU = ? AND s.bid = (SELECT s.BID " +
				"                 FROM Stock s, Employee e " +
				"                 WHERE e.EID = ? AND s.BID = e.BID AND s.SKU = ?)";
    	String insert_str = "insert into Sale values(?, ?"+
    						", ?, ?, ?, ?)";
    	try{
    		con.setAutoCommit(false);
    		System.out.println("Create Statement...");
    		update_stmt = con.prepareStatement(update_str);
    		insert_stmt = con.prepareStatement(insert_str);
    		
    		update_stmt.setString(1,EID);
    		update_stmt.setString(2,SKU);
    		update_stmt.setString(3,SKU);
			update_stmt.setString(4,EID);
			update_stmt.setString(5,SKU);
    		update_stmt.executeUpdate();
    		
    		insert_stmt.setString(1, payment);
    		insert_stmt.setString(2, "50000000");
    		insert_stmt.setString(3, SKU);
    		String stringDate = new String("18/08/01");
    		SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yy");
			java.util.Date utilDate = fm.parse(stringDate);
    		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
    		insert_stmt.setDate(4,sqlDate);
			insert_stmt.setString(5,CID);
    		insert_stmt.setString(6,EID);
    		insert_stmt.executeUpdate();
    		
    		con.commit();
    		
    	} catch (Exception e){
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