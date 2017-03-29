package sql;

import data.*;
import oracle.jdbc.driver.OracleDriver;
import sql.function.SQLConsumer;
import sql.function.SQLFunction;

import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class GameStoreDB {

    public static void setupDriver() throws SQLException {
        System.out.println("Registering Driver...");
        DriverManager.registerDriver(new OracleDriver());
        System.out.println("Registering Driver... Success");
    }

    /**
     * Creates a connection to be used for callback, closes the connection afterwards
     * @param callback
     */
    public static void withConnection(SQLConsumer<Connection> callback) throws SQLException {
        System.out.println("Creating Connection...");
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:ug", "ora_k2a0b", "a35833145")) {
            connection.setAutoCommit(false);
            System.out.println("Creating Connection... Success");
            callback.accept(connection);
        }
    }

    public static void main(String[] args) {
        SQLConsumer<Connection> callback = (con) -> {
            try {
                System.out.println("Building Database...");
                createDatabase(con);
                populateDatabase(con);

                //Test basic queries
//                getBranchResultSet(con);
//                getCustomerResultSet(con);
//                getEmployeeResultSet(con);
//                getProduct(con);
//                getSale(con);
//                getStock(con);

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
                int addQuantity = 0;
                BigDecimal wage = null;
                BigDecimal newPrice = null;
                String position = "";
                String phone = "";
                String address = "";

                //1) Test buyProduct
                sku = "10000000";
                eid = "30000000";
                payment = "CC123123";
                cid = "35553916";
                System.out.println("Test buyProduct");
                buyProduct(con, sku, eid, payment, cid);

                //4) Test add Employee
                //TODO
                eid = "33330000";
                name = "Tester Man";
                bid = "00000000";
                wage = new BigDecimal(10.00);
                position = "Janitor";
                phone = "2501011011";
                address = "1234 test st";
                System.out.println("Test addEmployee");
                addEmployee(con, eid, name, bid, wage, position, phone, address);

                //5) Test remove Employee
                eid = "30000000";
                System.out.println("Test removeEmployee");
                removeEmployee(con, eid);

                //6) Test addGameDatabase
                name = "Tester: Gold";
                sku = "33300000";
                price = new BigDecimal(10.00);
                did = "20000000";
                System.out.println("Test addGameDatabase");
                addGameDatabase(con, name, sku, price, did);

                //7) Test addGameStore
                bid = "00000000";
                sku = "33300000";
                quantity = 100;
                maxQuantity = 100;
                System.out.println("Test addGameStore");
                addGameStore(con, bid, sku, quantity, maxQuantity);

                //8) Test changeGamePrice
                sku = "";
                newPrice = new BigDecimal(90.00);
                changeGamePrice(con, sku, newPrice);

                //9) Test getCustomerInfo, checkCustomerAccount
                cid = "";
                name = "";
                phone = "";
                getCustomerInfo(con, cid);
                checkCustomerAccount(con, cid);
                getCustomerInfo(con, name, phone);
                checkCustomerAccount(con, name, phone);

                //10) Test createPurchaseOrder
                did = "20000000";
                bid = "00000000";
                System.out.println("Test createPurchaseOrder");
                createPurchaseOrder(con, did, bid);

                //11) Test updateProductQuantity
                addQuantity = 10;
                bid = "00000000";
                sku = "10000000";
                System.out.println("Test updateProductQuantity");
                updateProductQuantity(con, bid, sku, addQuantity);

                //12) Test createInventoryCount
                bid = "00000000";
                System.out.println("Test createInventoryCount");
                createInventoryCount(con, bid);

                //13) Test createSaleReport
                String strStartDate = new String("20/12/2016");
                String strEndDate = new String("01/01/2017");
                java.util.Date startDate = new SimpleDateFormat("dd/MM/yy").parse(strStartDate);
                java.util.Date endDate = new SimpleDateFormat("dd/MM/yy").parse(strEndDate);
                System.out.println("Test createSaleReport");
                createSaleReport(con, startDate, endDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        };

        try {
            setupDriver();
            withConnection(callback);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void createDatabase(Connection connection) throws SQLException {
        try {
            SQLUtil.executeFile(connection, new File("resource/sql/create_db.sql"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void populateDatabase(Connection connection) throws SQLException {
        try {
            SQLUtil.executeFile(connection, new File("resource/sql/populate_db.sql"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Basic Queries
    private static PreparedStatement getBranchQuery(Connection connection) throws SQLException {
        return SQLUtil.getAllFromTableQuery(connection, "Branch");
    }

    public static List<IBranch> getBranch() throws SQLException {
        return getData(GameStoreDB::getBranchQuery, Branch::fromResultSet);
    }

    private static PreparedStatement getCustomerQuery(Connection connection) throws SQLException {
        return SQLUtil.getAllFromTableQuery(connection, "Customer");
    }

    public static List<ICustomer> getCustomer() throws SQLException {
        return getData(GameStoreDB::getCustomerQuery, Customer::fromResultSet);
    }

    private static PreparedStatement getDeveloperQuery(Connection connection) throws SQLException {
        return SQLUtil.getAllFromTableQuery(connection, "Developer");
    }

    public static List<IDeveloper> getDeveloper() throws SQLException {
        return getData(GameStoreDB::getDeveloperQuery, Developer::fromResultSet);
    }

    private static PreparedStatement getEmployeeQuery(Connection connection) throws SQLException {
        return SQLUtil.getAllFromTableQuery(connection, "Employee");
    }

    public static List<IEmployee> getEmployee(Map<String, IBranch> idBranchMap) throws SQLException {
        return getData(GameStoreDB::getEmployeeQuery, (rs) -> Employee.fromResultSet(rs, idBranchMap));
    }

    private static PreparedStatement getProductQuery(Connection connection) throws SQLException {
        return SQLUtil.getAllFromTableQuery(connection, "Product");
    }

    public static List<IProduct> getProduct(Map<String, IDeveloper> idDeveloperMap) throws SQLException {
        return getData(GameStoreDB::getProductQuery, (rs) -> Product.fromResultSet(rs, idDeveloperMap));
    }

    private static PreparedStatement getSaleQuery(Connection connection) throws SQLException {
        return SQLUtil.getAllFromTableQuery(connection, "Sale");
    }

    public static List<ISale> getSale(Map<String, IProduct> skuProductMap, Map<String, ICustomer> idCustomerMap, Map<String, IEmployee> idEmployeeMap) throws SQLException {
        return getData(GameStoreDB::getSaleQuery, (rs) -> Sale.fromResultSet(rs, skuProductMap, idCustomerMap, idEmployeeMap));
    }

    private static PreparedStatement getStockQuery(Connection connection) throws SQLException {
        return SQLUtil.getAllFromTableQuery(connection, "Stock");
    }

    public static List<Stock> getStock(Map<String, IProduct> skuProductMap, Map<String, IBranch> idBranchMap) throws SQLException {
        return getData(GameStoreDB::getStockQuery, (rs) -> Stock.fromResultSet(rs, skuProductMap, idBranchMap));
    }

    private static <T> List<T> getData(SQLFunction<Connection, PreparedStatement> toQuery, SQLFunction<ResultSet, T> resultParser) throws SQLException {
        ArrayList<T> data = new ArrayList<>();
        withConnection((con) -> {
            try (PreparedStatement stmt = toQuery.apply(con)) {
                ResultSet rs = stmt.executeQuery();
                con.commit();
                while (rs.next()) {
                    data.add(resultParser.apply(rs));
                }
            }
        });
        return data;
    }

	// Program Queries

    //Query 1
    public static void buyProduct(Connection connection, String sku, String eid, String payment, String cid) throws SQLException {
        String update_str = "UPDATE Stock s " +
                "SET Quantity = (SELECT s.Quantity " +
                "                FROM Stock s, Employee e " +
                "                WHERE e.EID = ? AND s.BID = e.BID AND s.SKU = ?)-1 " +
                "WHERE s.SKU = ? AND s.BID = (SELECT s.BID " +
                "                 FROM Stock s, Employee e " +
                "                 WHERE e.EID = ? AND s.BID = e.BID AND s.SKU = ?)";

        String insert_str = "insert into Sale values(?, ?, ?, ?, ?, ?)";

        System.out.println("Create Statement...");
        try (PreparedStatement update_stmt = connection.prepareStatement(update_str);
            PreparedStatement insert_stmt = connection.prepareStatement(insert_str)){
            update_stmt.setString(1,eid);
            update_stmt.setString(2,sku);
            update_stmt.setString(3,sku);
            update_stmt.setString(4,eid);
            update_stmt.setString(5,sku);
            System.out.println("Execute...");
            update_stmt.executeUpdate();

            insert_stmt.setString(1, payment);
            insert_stmt.setString(2, "50000000"); // TODO auto increment? UUID?
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

            connection.commit();
            System.out.println("Changes commited");
        }
    }

    //Query 4
    public static void addEmployee(Connection connection, String eid, String name, String bid, BigDecimal wage, String position, String phone, String address) throws SQLException {
        String insert_str = "insert into Employee values(?, ?, ?, ?, ?, ?, ?)";

        System.out.println("Create Statement...");
        try (PreparedStatement stmt = connection.prepareStatement(insert_str)) {
            stmt.setString(1, eid);
            stmt.setString(2, name);
            stmt.setString(3, address);
            stmt.setString(4, phone);
            stmt.setBigDecimal(5, wage);
            stmt.setString(6,position);
            stmt.setString(7,bid);
            System.out.println("Execute...");
            stmt.executeUpdate();

            connection.commit();
            System.out.println("Changes Commited");
        }
    }

    //Query 5
    public static void removeEmployee(Connection con, String eid) throws SQLException {
        String delete_str = "DELETE FROM Employee WHERE EID = ?";

        System.out.println("Create Statement...");
        try (PreparedStatement stmt = con.prepareStatement(delete_str)){
            stmt.setString(1, eid);
            System.out.println("Execute...");
            stmt.executeUpdate();

            con.commit();
            System.out.println("Changes Commited");

        }
    }

    //Query 6
    public static void addGameDatabase(Connection con, String name, String sku, BigDecimal price, String did) throws SQLException {
        String insert_str = "insert into Product values(?, ?, ?, ?)";

        System.out.println("Create Statement...");
        try (PreparedStatement stmt = con.prepareStatement(insert_str)) {
            stmt.setString(1, name);
            stmt.setString(2, sku);
            stmt.setBigDecimal(3, price);
            stmt.setString(4, did);
            System.out.println("Execute...");
            stmt.executeUpdate();

            con.commit();
            System.out.println("Changes Commited");
        }
    }

    //Query 7
    public static void addGameStore(Connection con, String bid, String sku, int quantity, int maxQuantity) throws SQLException {
        String insert_str = "insert into Stock values(?, ?, ?, ?)";

        System.out.println("Create Statement...");
        try (PreparedStatement stmt = con.prepareStatement(insert_str)){
            stmt.setString(1, bid);
            stmt.setString(2, sku);
            stmt.setInt(3, quantity);
            stmt.setInt(4, maxQuantity);
            stmt.executeUpdate();

            con.commit();
            System.out.println("Changes Commited");
        }
    }

    //Query 8
    public static void changeGamePrice(Connection con, String sku, BigDecimal newPrice) throws SQLException {
        String update_str =
                "UPDATE Product p " +
                        "SET price = ? " +
                        "WHERE p.SKU = ?";

        System.out.println("Create Statement...");
        try (PreparedStatement stmt = con.prepareStatement(update_str);) {
            stmt.setBigDecimal(1,newPrice);
            stmt.setString(2,sku);
            System.out.println("Execute...");
            stmt.executeUpdate();

            con.commit();
            System.out.println("Changes commited");
        }
    }


    // Query 9 Part 1 - cid
    //Returns result set of customer information
    public static ResultSet getCustomerInfo(Connection con, String cid) throws SQLException {
        ResultSet rs;
        String select_str = "SELECT * FROM Customer c WHERE c.CID = ?";

        System.out.println("Create Statement...");
        try (PreparedStatement stmt = con.prepareStatement(select_str)) {
            stmt.setString(1,cid);
            System.out.println("Execute...");
            rs = stmt.executeQuery();

            con.commit();
            System.out.println("Changes commited");
        }
        return rs;
    }


    // Query 9 Part 1 - name&phone
    //Returns result set of customer information
    public static ResultSet getCustomerInfo(Connection con, String name, String phone) throws SQLException {
        ResultSet rs;
        String select_str = "SELECT * FROM Customer c WHERE c.Name = ? AND c.Phone = ?";

        System.out.println("Create Statement...");
        try (PreparedStatement stmt = con.prepareStatement(select_str)) {
            stmt.setString(1,name);
            stmt.setString(2,phone);
            System.out.println("Execute...");
            rs = stmt.executeQuery();

            con.commit();
            System.out.println("Changes commited");
        }
        return rs;
    }

    //Query 9 part 2 - cid
    //Returns result set of payment,snum,sku,saledate
    public static ResultSet checkCustomerAccount(Connection con, String cid) throws SQLException {
        ResultSet rs;
        String select_str = "SELECT Payment, Snum, SKU, saleDate " +
                "FROM Sale s " +
                "WHERE s.CID = ? AND s.saleDate >= ?";

        System.out.println("Create Statement...");
        try (PreparedStatement stmt = con.prepareStatement(select_str)) {
            stmt.setString(1,cid);
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_MONTH, -30);
            java.util.Date date = c.getTime();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            stmt.setDate(2,sqlDate);
            System.out.println("Execute...");
            rs = stmt.executeQuery();

            con.commit();
            System.out.println("Changes commited");
        }

        return rs;
    }

    //Query 9 part 2 - name&phone
    //Returns result set of payment,snum,sku,saledate
    public static ResultSet checkCustomerAccount(Connection con, String name, String phone) throws SQLException {
        ResultSet rs = null;
        String select_str =
                "SELECT Payment, Snum, SKU, saleDate " +
                        "FROM Sale s, Customer c " +
                        "WHERE s.CID = c.CID AND c.Name = ? AND c.Phone=? " +
                        "AND s.saleDate >= ?";

        System.out.println("Create Statement...");
        try (PreparedStatement select_stmt = con.prepareStatement(select_str)) {
            select_stmt.setString(1,name);
            select_stmt.setString(2,phone);
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_MONTH, -30);
            java.util.Date date = c.getTime();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            select_stmt.setDate(3,sqlDate);
            System.out.println("Execute...");
            rs = select_stmt.executeQuery();

            con.commit();
            System.out.println("Changes commited");
        }
        return rs;
    }

    //Query 10
    //Returns result set of name,sku,price
    public static ResultSet createPurchaseOrder(Connection con, String did, String bid) throws SQLException {
        ResultSet rs;
        String select_str =
                "SELECT Name, s.SKU, Price, " +
                        "  maxquantity - quantity as orderQuantity, " +
                        "  Price*(maxquantity - quantity) as orderPrice " +
                        "FROM Stock s, Product p " +
                        "WHERE p.DID = ? AND s.BID = ? " +
                        "  AND s.SKU=p.SKU AND maxquantity-quantity > 0";

        System.out.println("Create Statement...");
        try (PreparedStatement stmt = con.prepareStatement(select_str)){
            stmt.setString(1,did);
            stmt.setString(2,bid);

            System.out.println("Execute...");
            rs = stmt.executeQuery();

            con.commit();
            System.out.println("Changes commited");
        }
        return rs;
    }

    //Query 11
    public static void updateProductQuantity(Connection con, String bid, String sku, int addQuantity) throws SQLException {
        String update_str =
                "UPDATE Stock s " +
                        "SET quantity = s.quantity + ? " +
                        "WHERE s.BID = ? AND s.SKU = ?";

        System.out.println("Create Statement...");
        try (PreparedStatement stmt = con.prepareStatement(update_str)) {
            stmt.setInt(1,addQuantity);
            stmt.setString(2,bid);
            stmt.setString(3,sku);

            System.out.println("Execute...");
            stmt.executeUpdate();

            con.commit();
            System.out.println("Changes commited");
        }
    }

    //Query 12
    //Returns result set of pname,sku,price,quantity
    public static ResultSet createInventoryCount(Connection con, String bid) throws SQLException {
        ResultSet rs;
        String select_str =
                "SELECT Name, s.SKU, Price, Quantity " +
                        "FROM Stock s, Product p " +
                        "WHERE s.SKU = p.SKU AND s.BID = ?";

        System.out.println("Create Statement...");
        try (PreparedStatement stmt = con.prepareStatement(select_str)) {
            stmt.setString(1, bid);
            System.out.println("Execute...");
            rs = stmt.executeQuery();

            con.commit();
            System.out.println("Changes commited");
        }
        return rs;
    }

    //
    public static ResultSet createSaleReport(Connection con, java.util.Date startDate, java.util.Date endDate) throws SQLException {
        ResultSet rs;
        String select_str =
                "SELECT * " +
                        "FROM Sale " +
                        "WHERE ? <= SALEDATE AND SALEDATE <= ?";

        System.out.println("Create Statement...");
        try (PreparedStatement stmt = con.prepareStatement(select_str)){
            java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
            java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());
            stmt.setDate(1,sqlStartDate);
            stmt.setDate(2,sqlEndDate);

            System.out.println("Execute...");
            rs = stmt.executeQuery();

            con.commit();
            System.out.println("Changes commited");
        }
        return rs;
    }
}
