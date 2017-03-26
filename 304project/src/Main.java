import java.sql.*;
import java.text.SimpleDateFormat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import java.math.*;

import java.util.Calendar;

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

            String sku = "";
            String eid = "";
            String payment = "";
            String cid = "";
            String bid = "";
            String did = "";
            BigDecimal price = null;
            String name = "";
            int quantity = 0;
            int maxQuantity = 0;
            BigDecimal wage = null;
            String position = null;
            String phone = null;
            String address = null;

			//1) Test buyProduct
            sku = "10000000";
            eid = "30000000";
            payment = "CC123123";
            cid = "35553916";
            buyProduct(con,sku,eid,payment,cid);
            
            //4) Test add Employee
            eid = null;
            name = null;
            bid = null;
            wage = null;
            position = null;
            phone = null;
            address = null;
            addEmployee(con,eid,name,bid,wage,position,phone,address);
            
            //5) Test remove Employee
            eid = null;
            removeEmployee(con,eid);
            
            //6) Test addGameDatabase
            name = null;
            sku = null;
            price = null;
            did = null;
            addGameDatabase(con,name,sku,price,did);
            
            //7) Test addGameStore
            bid = null;
            sku = null;
            quantity = 0;
            maxQuantity = 0;
            addGameStore(con,bid,sku,quantity,maxQuantity);
            

            //10) Test createPurchaseOrder
			did = "20000000";
			bid = "00000000";
			createPurchaseOrder(con,did,bid);

            //11) Test updateProductQuantity
			int addQuantity = 10;
			bid = "00000000";
			sku = "10000000";
			updateProductQuantity(con,bid,sku,addQuantity);

			//12) Test createInventoryCount
			bid = "00000000";
			createInventoryCount(con,bid);

            //13) Test createSaleReport
			String strStartDate = new String("20/12/2016");
			String strEndDate = new String("01/01/2017");
			java.util.Date startDate = new SimpleDateFormat("dd/MM/yy").parse(strStartDate);
			java.util.Date endDate = new SimpleDateFormat("dd/MM/yy").parse(strEndDate);
			createSaleReport(con,startDate,endDate);



        } catch (Exception e) {
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
			System.out.println("Execute...");
    		update_stmt.executeUpdate();

    		insert_stmt.setString(1, payment);
    		insert_stmt.setString(2, "50000000");
    		insert_stmt.setString(3, sku);
    		//String stringDate = new String("18/08/01");
    		//SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yy");
			//java.util.Date utilDate = fm.parse(stringDate);
    		//java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			java.sql.Date sqlDate = new java.sql.Date(Calendar.getInstance().getTime().getTime());
    		insert_stmt.setDate(4,sqlDate);
			insert_stmt.setString(5,cid);
    		insert_stmt.setString(6,eid);
			System.out.println("Execute...");
    		insert_stmt.executeUpdate();

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
    public static void addEmployee(Connection con, String eid, String name, String bid, BigDecimal wage, String position, String phone, String address){
		PreparedStatement insert_stmt = null;
		String insert_str = "insert into Employee values(?, ?"+
				", ?, ?, ?, ?)";
		try{
			con.setAutoCommit(false);
			System.out.println("Create Statement...");
			insert_stmt = con.prepareStatement(insert_str);


			insert_stmt.setString(1, eid);
			insert_stmt.setString(2, name);
			insert_stmt.setString(3, address);
			insert_stmt.setString(4, phone);
			insert_stmt.setBigDecimal(5, wage);
			insert_stmt.setString(6,position);
			insert_stmt.setString(7,bid);
			insert_stmt.executeUpdate();

			con.commit();

		} catch (Exception e){
			e.printStackTrace();
		} finally{
			try{
				if(insert_stmt != null){
					insert_stmt.close();
				}
				con.setAutoCommit(true);

			} catch(SQLException se){
			}
		}
	}
	//TODO
	public static void removeEmployee(Connection con,String eid){
		Statement drop_stmt = null;
		String drop_str = "delete from Employee" +
				"where EID =" + eid;
		try{
			con.setAutoCommit(false);
			System.out.println("Create Statement...");
			drop_stmt = con.createStatement();

			drop_stmt.executeUpdate(drop_str);

			con.commit();

		} catch (Exception e){
			e.printStackTrace();
		} finally{
			try{
				if(drop_stmt != null){
					drop_stmt.close();
				}
				con.setAutoCommit(true);

			} catch(SQLException se){
			}
		}
	}
	//TODO
	public static void addGameDatabase(Connection con, String name, String sku, BigDecimal price, String did){
		PreparedStatement insert_stmt = null;
		String insert_str = "insert into Product values(?, ?"+
				", ?, ?)";
		try{
			con.setAutoCommit(false);
			System.out.println("Create Statement...");
			insert_stmt = con.prepareStatement(insert_str);


			insert_stmt.setString(1, name);
			insert_stmt.setString(2, sku);
			insert_stmt.setBigDecimal(3, price);
			insert_stmt.setString(4, did);
			insert_stmt.executeUpdate();

			con.commit();

		} catch (Exception e){
			e.printStackTrace();
		} finally{
			try{
				if(insert_stmt != null){
					insert_stmt.close();
				}
				con.setAutoCommit(true);

			} catch(SQLException se){
			}
		}
	}
	//TODO
	public static void addGameStore(Connection con, String bid, String sku, int quantity, int maxQuantity){
		PreparedStatement insert_stmt = null;
		String insert_str = "insert into Stock values(?, ?"+
				", ?, ?)";
		try{
			con.setAutoCommit(false);
			System.out.println("Create Statement...");
			insert_stmt = con.prepareStatement(insert_str);


			insert_stmt.setString(1, bid);
			insert_stmt.setString(2, sku);
			insert_stmt.setInt(3, quantity);
			insert_stmt.setInt(4, maxQuantity);
			insert_stmt.executeUpdate();

			con.commit();

		} catch (Exception e){
			e.printStackTrace();
		} finally{
			try{
				if(insert_stmt != null){
					insert_stmt.close();
				}
				con.setAutoCommit(true);

			} catch(SQLException se){
			}
		}
	}
	//TODO TEST
	public static void changeGamePrice(Connection con, String sku, BigDecimal newPrice){
		PreparedStatement update_stmt = null;
		String update_str =
				"UPDATE Product p " +
						"SET price = ? " +
						"WHERE p.SKU = ?";
		try{
			con.setAutoCommit(false);
			System.out.println("Create Statement...");
			update_stmt = con.prepareStatement(update_str);

			update_stmt.setBigDecimal(1,newPrice);
			update_stmt.setString(2,sku);
			System.out.println("Execute...");
			update_stmt.executeUpdate();

			con.commit();
			System.out.println("Changes commited");

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(update_stmt != null){
					update_stmt.close();
					con.setAutoCommit(true);
				}
			} catch(SQLException se){
			}
		}
	}

	//TODO TEST
	public static void checkCustomerAccount(Connection con, String cid){
		PreparedStatement select_stmt1 = null;
		PreparedStatement select_stmt2 = null;
		String select_str1 = "SELECT * FROM Customer c WHERE c.CID=?";
		String select_str2 = "SELECT * " +
				"FROM Sale s " +
				"WHERE s.CID = ? AND s.saleDate >= ?";
		try{
			con.setAutoCommit(false);
			System.out.println("Create Statement...");
			select_stmt1 = con.prepareStatement(select_str1);
			select_stmt2 = con.prepareStatement(select_str2);

			select_stmt1.setString(1,cid);
			System.out.println("Execute...");
			select_stmt1.executeUpdate();
			//do something

			select_stmt2.setString(1,cid);
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_MONTH, -30);
			java.util.Date date = c.getTime();
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			select_stmt2.setDate(2,sqlDate);
			System.out.println("Execute...");
			select_stmt2.executeUpdate();
			//do something
			con.commit();
			System.out.println("Changes commited");

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(select_stmt1 != null && select_stmt2 != null){
					select_stmt1.close();
					select_stmt2.close();
					con.setAutoCommit(true);
				}
			} catch(SQLException se){
			}
		}
	}

	//TODO TEST
	// need to change, not void, needs to return info. ResultSet?
	public static void checkCustomerAccount(Connection con, String name, String phone){
		PreparedStatement select_stmt1 = null;
		PreparedStatement select_stmt2 = null;
		String select_str1 = "SELECT * FROM Customer c WHERE c.Name = ? AND c.Phone = ?";
		String select_str2 =
				"SELECT * " +
				"FROM Sale s, Customer c " +
				"WHERE s.CID = c.CID AND c.Name = ? AND c.Phone=? " +
				"AND s.saleDate >= ?";
		try{
			con.setAutoCommit(false);
			System.out.println("Create Statement...");
			select_stmt1 = con.prepareStatement(select_str1);
			select_stmt2 = con.prepareStatement(select_str2);

			select_stmt1.setString(1,name);
			select_stmt1.setString(2,phone);
			System.out.println("Execute...");
			select_stmt1.executeUpdate();
			//Do something with result

			select_stmt2.setString(1,name);
			select_stmt2.setString(2,phone);
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_MONTH, -30);
			java.util.Date date = c.getTime();
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			select_stmt2.setDate(3,sqlDate);
			System.out.println("Execute...");
			select_stmt2.executeUpdate();
			//Do something with result

			con.commit();
			System.out.println("Changes commited");

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(select_stmt1 != null && select_stmt2 != null){
					select_stmt1.close();
					select_stmt2.close();
					con.setAutoCommit(true);
				}
			} catch(SQLException se){
			}
		}
	}
	//TODO
	// need to change, not void, needs to return info. ResultSet?
	public static void createPurchaseOrder(Connection con, String did, String bid){
		PreparedStatement select_stmt = null;
		String select_str =
				"SELECT Name, s.SKU, Price, " +
				"  maxquantity - quantity as orderQuantity, " +
				"  Price*(maxquantity - quantity) as orderPrice " +
				"FROM Stock s, Product p " +
				"WHERE p.DID = ? AND s.BID = ? " +
				"  AND s.SKU=p.SKU AND maxquantity-quantity > 0";
		try{
			con.setAutoCommit(false);
			System.out.println("Create Statement...");
			select_stmt = con.prepareStatement(select_str);

			select_stmt.setString(1,did);
			select_stmt.setString(2,bid);

			System.out.println("Execute...");
			select_stmt.executeUpdate();

			con.commit();
			System.out.println("Changes commited");

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(select_stmt != null){
					select_stmt.close();
					con.setAutoCommit(true);
				}
			} catch(SQLException se){
			}
		}
	}

	//
	public static void updateProductQuantity(Connection con, String bid, String sku, int addQuantity){
		PreparedStatement update_stmt = null;
		String update_str =
						"UPDATE Stock s " +
						"SET quantity = s.quantity + ? " +
						"WHERE s.BID = ? AND s.SKU = ?";
		try{
			con.setAutoCommit(false);
			System.out.println("Create Statement...");
			update_stmt = con.prepareStatement(update_str);

			update_stmt.setInt(1,addQuantity);
			update_stmt.setString(2,bid);
			update_stmt.setString(3,sku);

			System.out.println("Execute...");
			update_stmt.executeUpdate();

			con.commit();
			System.out.println("Changes commited");


		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(update_stmt != null){
					update_stmt.close();
					con.setAutoCommit(true);
				}
			}catch(SQLException se){
			}
		}
	}

	// TODO: need to change to return info
	public static void createInventoryCount(Connection con, String bid){
		PreparedStatement select_stmt = null;
		String select_str =
						"SELECT Name, s.SKU, Price, Quantity " +
						"FROM Stock s, Product p " +
						"WHERE s.SKU = p.SKU AND s.BID = ?";
		try{
			con.setAutoCommit(false);
			System.out.println("Create Statement...");
			select_stmt = con.prepareStatement(select_str);

			select_stmt.setString(1,bid);
			System.out.println("Execute...");
			select_stmt.executeUpdate();

			con.commit();
			System.out.println("Changes commited");

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(select_stmt != null){
					select_stmt.close();
					con.setAutoCommit(true);
				}
			} catch(SQLException se){
			}
		}
	}

	// TODO: need to change to return info
	public static void createSaleReport(Connection con, java.util.Date startDate, java.util.Date endDate){
		PreparedStatement select_stmt = null;
		String select_str =
				"SELECT * " +
				"FROM Sale " +
				"WHERE ? <= SALEDATE AND SALEDATE <= ?";
		try{
			con.setAutoCommit(false);
			System.out.println("Create Statement...");
			select_stmt = con.prepareStatement(select_str);
			java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
			java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());
			select_stmt.setDate(1,sqlStartDate);
			select_stmt.setDate(2,sqlEndDate);

			System.out.println("Execute...");
			select_stmt.executeUpdate();

			con.commit();
			System.out.println("Changes commited");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(select_stmt != null){
					select_stmt.close();
					con.setAutoCommit(true);
				}
			} catch(SQLException se){
			}
		}
	}

}