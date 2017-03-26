import java.sql.*;
import java.text.SimpleDateFormat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import java.math.*;

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

            String sku = "10000000";
            String eid = "30000000";
            String payment = "CC123123";
            String cid = "35553916";
            buyProduct(con,sku,eid,payment,cid);
            
            
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
	//coderanch.com/t/306966/databases/Execute-sql-file-java
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
					//System.out.println(">>"+inst[i]);
					stmt.executeUpdate(inst[i]);
					System.out.println(">>"+inst[i]);
				}
			}
		}catch(Exception e) {
			System.out.println("Building Database Failed");
			e.printStackTrace();
		}
	}

    public static void buyProduct(Connection con, String sku, String eid, String payment, String cid){
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
    		
    		update_stmt.setString(1,eid);
    		update_stmt.setString(2,sku);
    		update_stmt.setString(3,sku);
			update_stmt.setString(4,eid);
			update_stmt.setString(5,sku);
			System.out.println("Executing statement: "+update_stmt);
    		update_stmt.executeUpdate();
			System.out.println("Executed");
    		
    		insert_stmt.setString(1, payment);
    		insert_stmt.setString(2, "50000000");
    		insert_stmt.setString(3, sku);
    		String stringDate = new String("18/08/01");
    		SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yy");
			java.util.Date utilDate = fm.parse(stringDate);
    		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
    		insert_stmt.setDate(4,sqlDate);
			insert_stmt.setString(5,cid);
    		insert_stmt.setString(6,eid);
			System.out.println("Executing statement: "+insert_stmt);
    		insert_stmt.executeUpdate();
			System.out.println("Executed");

    		con.commit();
			System.out.println("Changes commited");
    		
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
	//TODO
    public static void addEmployee(Connection con, String eid, String bid, BigDecimal wage, String position, String phone, String address){

	}
	//TODO
	public static void removeEmployee(Connection con, String eid){

	}
	//TODO
	public static void addGameDatabase(Connection con, String sku, String did, String name, BigDecimal Price){

	}
	//TODO
	public static void addGameStore(Connection con, String bid, String sku, int quantity, int maxQuantity){

	}
	//TODO
	public static void changeGamePrice(Connection con, String sku, BigDecimal newPrice){

	}
	//TODO
	public static void checkCustomerAccount(Connection con, String cid){

	}

	//TODO
	// need to change, not void, needs to return info. ResultSet?
	public static void checkCustomerAccount(Connection con, String name, String phone){

	}
	//TODO
	// need to change, not void, needs to return info. ResultSet?
	public static void createPurchaseOrder(Connection con, String did, String bid){

	}
	//TODO
	public static void updateProductQuantity(Connection con, String bid, String sku, int addQuantity){

	}

	//TODO
	// need to change, not void, needs to return info. ResultSet?
	public static void createInventoryCount(Connection con, String bid){

	}

	//TODO
	// need to change, not void, needs to return info. ResultSet?
	public static void createSaleReport(Connection con, Date startDate, Date endDate){

	}

}