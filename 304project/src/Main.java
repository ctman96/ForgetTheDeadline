import java.sql.*;
import java.text.SimpleDateFormat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import oracle.jdbc.driver.OracleDriver;



public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world");
        Connection con = null;
        try {
        	System.out.println("Register Driver...");
            DriverManager.registerDriver(new OracleDriver());
            
            System.out.println("Creating Connection...");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:ug", "ora_k2a0b", "a35833145");
			System.out.println("Building Database...");
            createDatabase(con);

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
	//Buffer code based on code found at
	//coderance.com/t/306966/databases/Execute-sql-file-java
	public static void createDatabase(Connection con) throws SQLException{
		System.out.println("Running Script...");
    	String s = new String();
    	StringBuffer sb = new StringBuffer();
    	try{
    		FileReader fr = new FileReader(new File("createDatabase.sql"));
			BufferedReader br = new BufferedReader(fr);

			while((s=br.readLine()) != null){
				sb.append(s);
			}
			br.close();
			String[] inst = sb.toString().split(";");

			Statement stmt = con.createStatement();
			for(int i=0;i<inst.length;i++){
				if(!inst[i].trim().equals("")){
					System.out.println(">>"+inst[i]);
					stmt.executeUpdate(inst[i]);
					System.out.println(">>"+inst[i]);
				}
			}
		}catch(Exception e) {
			System.out.println("Building Database Failed");
			e.printStackTrace();
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