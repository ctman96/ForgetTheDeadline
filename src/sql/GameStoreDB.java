package sql;

import data.*;
import oracle.jdbc.driver.OracleDriver;
import sql.data.*;
import sql.function.SQLConsumer;
import sql.function.SQLFunction;

import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class GameStoreDB {

    public enum Aggregate {
        AVERAGE, MIN, MAX, COUNT
    }

    public static void setupDriver() throws SQLException {
        System.out.println("Registering Driver...");
        DriverManager.registerDriver(new OracleDriver());
        System.out.println("Registering Driver... Success");
    }

    /**
     * Creates a connection to be used for callback, closes the connection afterwards
     * @param callback
     */
    public static void withConnection(String user, String pass, SQLConsumer<Connection> callback) throws SQLException {
        System.out.println("Creating Connection...");
        try (//Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:ug", "ora_j2d0b", "a12222148")) {
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:ug", user, pass))  {
            connection.setAutoCommit(false);
            System.out.println("Creating Connection... Success");
            callback.accept(connection);
        }
    }

    public static void createDatabase(Connection connection) throws SQLException {
        try {
            SQLUtil.executeFile(connection, new File("resource/sql/create_db.sql"));
            createTriggers(connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createTriggers(Connection con) throws SQLException{
        Statement stmt = con.createStatement();
        stmt.executeQuery("CREATE OR REPLACE TRIGGER branch_on_insert " +
                "  BEFORE INSERT ON Branch " +
                "  FOR EACH ROW " +
                " BEGIN " +
                "  :new.BID := trim(to_char(branch_sequence.nextval, '00000000')); " +
                " END;");
        stmt.executeQuery("CREATE OR REPLACE TRIGGER employee_on_insert " +
                "BEFORE INSERT ON Employee " +
                "FOR EACH ROW " +
                " BEGIN " +
                "  :new.EID := trim(to_char(employee_sequence.nextval, '00000000')); " +
                " END;");
        stmt.executeQuery("CREATE OR REPLACE TRIGGER developer_on_insert " +
                "  BEFORE INSERT ON Developer " +
                "  FOR EACH ROW " +
                " BEGIN " +
                "  :new.DID := trim(to_char(developer_sequence.nextval, '00000000')); " +
                " END;");
        stmt.executeQuery("CREATE OR REPLACE TRIGGER customer_on_insert " +
                "BEFORE INSERT ON Customer " +
                "FOR EACH ROW " +
                " BEGIN " +
                "  :new.CID := trim(to_char(customer_sequence.nextval, '00000000')); " +
                " END;");
        stmt.executeQuery("CREATE OR REPLACE TRIGGER sale_on_insert " +
                "BEFORE INSERT ON Sale " +
                "FOR EACH ROW " +
                " BEGIN " +
                "  :new.snum := trim(to_char(sale_sequence.nextval, '00000000')); " +
                " END;");
        con.commit();
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
        return SQLUtil.getAllFromTableQuery(connection, "Branch", "BID");
    }

    public static List<IBranch> getBranch(String user, String pass) throws SQLException {
        return getData(user,pass,GameStoreDB::getBranchQuery, Branch::fromResultSet);
    }

    private static PreparedStatement getCustomerQuery(Connection connection) throws SQLException {
        return SQLUtil.getAllFromTableQuery(connection, "Customer", "CID");
    }

    public static List<ICustomer> getCustomer(String user, String pass) throws SQLException {
        return getData(user,pass,GameStoreDB::getCustomerQuery, Customer::fromResultSet);
    }

    private static PreparedStatement getDeveloperQuery(Connection connection) throws SQLException {
        return SQLUtil.getAllFromTableQuery(connection, "Developer", "DID");
    }

    public static List<IDeveloper> getDeveloper(String user, String pass) throws SQLException {
        return getData(user,pass,GameStoreDB::getDeveloperQuery, Developer::fromResultSet);
    }

    private static PreparedStatement getEmployeeQuery(Connection connection) throws SQLException {
        return SQLUtil.getAllFromTableQuery(connection, "Employee", "EID");
    }

    public static List<IEmployee> getEmployee(String user, String pass,Map<String, IBranch> idBranchMap) throws SQLException {
        return getData(user,pass,GameStoreDB::getEmployeeQuery, (rs) -> Employee.fromResultSet(rs, idBranchMap));
    }

    private static PreparedStatement getProductQuery(Connection connection) throws SQLException {
        return SQLUtil.getAllFromTableQuery(connection, "Product", "SKU");
    }

    public static List<IProduct> getProduct(String user, String pass,Map<String, IDeveloper> idDeveloperMap) throws SQLException {
        return getData(user,pass,GameStoreDB::getProductQuery, (rs) -> Product.fromResultSet(rs, idDeveloperMap));
    }

    private static PreparedStatement getSaleQuery(Connection connection) throws SQLException {
        return SQLUtil.getAllFromTableQuery(connection, "Sale", "saleDate");
    }

    public static List<ISale> getSale(String user, String pass,Map<String, IProduct> skuProductMap, Map<String, ICustomer> idCustomerMap, Map<String, IEmployee> idEmployeeMap) throws SQLException {
        return getData(user,pass,GameStoreDB::getSaleQuery, (rs) -> Sale.fromResultSet(rs, skuProductMap, idCustomerMap, idEmployeeMap));
    }

    private static PreparedStatement getStockQuery(Connection connection) throws SQLException {
        return SQLUtil.getAllFromTableQuery(connection, "Stock", "BID");
    }

    public static List<IStock> getStock(String user, String pass,Map<String, IProduct> skuProductMap, Map<String, IBranch> idBranchMap) throws SQLException {
        return getData(user,pass,GameStoreDB::getStockQuery, (rs) -> Stock.fromResultSet(rs, skuProductMap, idBranchMap));
    }

    /**
     * Provided as an optimization as establishing connection is not desirable
     * @return A fully populated and joined GameStore
     * @throws SQLException
     */
    public static GameStore getGameStore(String user, String pass) throws SQLException {
        GameStore gameStore = new GameStore();

        withConnection(user,pass,(con) -> {
            PreparedStatement branchStmt = getBranchQuery(con);
            PreparedStatement customerStmt = getCustomerQuery(con);
            PreparedStatement developerStmt = getDeveloperQuery(con);
            PreparedStatement employeeStmt = getEmployeeQuery(con);
            PreparedStatement productStmt = getProductQuery(con);
            PreparedStatement saleStmt = getSaleQuery(con);
            PreparedStatement stockStmt = getStockQuery(con);

            ResultSet branchRs = branchStmt.executeQuery();
            ResultSet customerRs = customerStmt.executeQuery();
            ResultSet developerRs = developerStmt.executeQuery();
            ResultSet employeeRs = employeeStmt.executeQuery();
            ResultSet productRs = productStmt.executeQuery();
            ResultSet saleRs = saleStmt.executeQuery();
            ResultSet stockRs = stockStmt.executeQuery();

            con.commit();

            gameStore.branch = getDataFromRs(branchRs, Branch::fromResultSet);
            gameStore.customer = getDataFromRs(customerRs, Customer::fromResultSet);
            gameStore.developer = getDataFromRs(developerRs, Developer::fromResultSet);

            Map<String, IDeveloper> idDeveloperMap = new HashMap<>();
            for (IDeveloper developer : gameStore.developer) {
                idDeveloperMap.put(developer.getId(), developer);
            }

            Map<String, ICustomer> idCustomerMap = new HashMap<>();
            for (ICustomer customer : gameStore.customer) {
                idCustomerMap.put(customer.getId(), customer);
            }

            Map<String, IBranch> idBranchMap = new HashMap<>();
            for (IBranch branch : gameStore.branch) {
                idBranchMap.put(branch.getId(), branch);
            }

            gameStore.employee = getDataFromRs(employeeRs, (rs) -> Employee.fromResultSet(rs, idBranchMap));
            gameStore.product = getDataFromRs(productRs, (rs) -> Product.fromResultSet(rs, idDeveloperMap));

            Map<String, IEmployee> idEmployeeMap = new HashMap<>();
            for (IEmployee employee : gameStore.employee) {
                idEmployeeMap.put(employee.getId(), employee);
            }

            Map<String, IProduct> idProductMap = new HashMap<>();
            for (IProduct product : gameStore.product) {
                idProductMap.put(product.getSKU(), product);
            }

            gameStore.sale = getDataFromRs(saleRs, (rs) -> Sale.fromResultSet(rs, idProductMap, idCustomerMap, idEmployeeMap));
            gameStore.stock = getDataFromRs(stockRs, (rs) -> Stock.fromResultSet(rs, idProductMap, idBranchMap));
        });

        return gameStore;
    }

    private static <T> List<T> getData(String user, String pass, SQLFunction<Connection, PreparedStatement> toQuery, SQLFunction<ResultSet, T> resultParser) throws SQLException {
        ArrayList<T> data = new ArrayList<>();
        withConnection(user, pass, (con) -> {
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

    private static <T> List<T> getDataFromRs(ResultSet rs, SQLFunction<ResultSet, T> resultParser) throws SQLException {
        ArrayList<T> data = new ArrayList<>();
        while(rs.next()) {
            data.add(resultParser.apply(rs));
        }
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
            insert_stmt.setString(2, "[PH]ID"); // Place holder SID for generated PK
            insert_stmt.setString(3, sku);
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
    //Query 2
    public static void addCustomer(Connection connection, String name, String cid, String phone, String address) throws SQLException {
        String insert_str = "insert into Customer values(?, ?, ?, ?)";

        System.out.println("Create Statement...");
        try (PreparedStatement stmt = connection.prepareStatement(insert_str)) {
            stmt.setString(1, name);
            stmt.setString(2, cid);
            stmt.setString(3, phone);
            stmt.setString(4, address);
            System.out.println("Execute...");
            stmt.executeUpdate();

            connection.commit();
            System.out.println("Changes Commited");
        }
    }

    //Query 3
    public static List<IProduct> searchProduct(String user, String pass,BigDecimal price) throws SQLException {
        String select_str = "SELECT * FROM Product WHERE price <= ?";

        return getData(user,pass,(con) -> {
            PreparedStatement stmt = con.prepareStatement(select_str);
            stmt.setBigDecimal(1, price);
            return stmt;
        }, (rs) -> {
            String sku = rs.getString("SKU");
            String pname = rs.getString("name");
            BigDecimal pprice = rs.getBigDecimal("price");
            String did = rs.getString("did");
            Product product = new Product(sku, pname, pprice, new Developer(did, "", null, null));
            return product;
        });
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
    public static List<ICustomer> getCustomerInfo(String user, String pass,String cid) throws SQLException {
        String select_str = "SELECT * FROM Customer c WHERE c.CID = ?";

        return getData(user,pass,(con) -> {
            PreparedStatement stmt = con.prepareStatement(select_str);
            stmt.setString(1, cid);
            return stmt;
        }, Customer::fromResultSet);
    }

    // Query 9 Part 1 - name&phone
    public static List<ICustomer> getCustomerInfo(String user, String pass,String name, String phone) throws SQLException {
        String select_str = "SELECT * FROM Customer c WHERE c.Name = ? AND c.Phone = ?";

        return getData(user,pass,(con) -> {
            PreparedStatement stmt = con.prepareStatement(select_str);
            stmt.setString(1, name);
            stmt.setString(2, phone);
            return stmt;
        }, Customer::fromResultSet);
    }

    //Query 9 part 2 - cid
    public static List<ISale> checkCustomerAccount(String user, String pass,String cid) throws SQLException {
        String select_str =
                "SELECT s.snum, s.Payment, s.saleDate, c.Name AS cname, p.Name AS pname, e.Name AS ename " +
                "FROM Sale s, Customer c, Product p, Employee e " +
                "WHERE s.CID = ? AND s.saleDate >= ? AND " +
                        "s.CID = c.CID AND s.SKU = p.SKU AND s.EID = e.EID";

        return getData(user,pass,(con) -> {
            PreparedStatement stmt = con.prepareStatement(select_str);
            stmt.setString(1, cid);
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_MONTH, -30);
            java.sql.Date date = new java.sql.Date(c.getTime().getTime());
            stmt.setDate(2, date);
            return stmt;
        }, (rs) -> {
            String snum = rs.getString("Snum");
            String payment = rs.getString("Payment");
            java.sql.Date date = rs.getDate("saleDate");
            String cname = rs.getString("cname");
            String pname = rs.getString("pname");
            String ename = rs.getString("ename");
            Sale sale = new Sale(snum, payment, date, // Imagination shall fill the blank
                    new Product(null, pname, null, null),
                    new Customer(null, cname, null, null),
                    new Employee(null, ename, null, null, null, null, null));
            return sale;
        });
    }

    //Query 9 part 2 - name&phone
    public static List<ISale> checkCustomerAccount(String user, String pass,String name, String phone) throws SQLException {
        String select_str =
                "SELECT s.snum, s.Payment, s.saleDate, c.Name AS cname, p.Name AS pname, e.Name AS ename " +
                        "FROM Sale s, Customer c, Product p, Employee e " +
                        "WHERE c.Name = ? AND c.Phone = ?  AND s.saleDate >= ? AND " +
                        "s.CID = c.CID AND s.SKU = p.SKU AND s.EID = e.EID";

        return getData(user,pass,(con) -> {
            PreparedStatement stmt = con.prepareStatement(select_str);
            stmt.setString(1, name);
            stmt.setString(2, phone);
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_MONTH, -30);
            java.sql.Date date = new java.sql.Date(c.getTime().getTime());
            stmt.setDate(3, date);
            return stmt;
        }, (rs) -> {
            String snum = rs.getString("Snum");
            String payment = rs.getString("Payment");
            java.sql.Date date = rs.getDate("saleDate");
            String cname = rs.getString("cname");
            String pname = rs.getString("pname");
            String ename = rs.getString("ename");
            Sale sale = new Sale(snum, payment, date,
                    new Product(null, pname, null, null),
                    new Customer(null, cname, null, null),
                    new Employee(null, ename, null, null, null, null, null));
            return sale;
        });
    }

    //Query 10
    public static List<Order> createPurchaseOrder(String user, String pass,String did, String bid) throws SQLException {
        String select_str =
                "SELECT Name, s.SKU, Price, " +
                        "  maxquantity - quantity as orderQuantity, " +
                        "  Price*(maxquantity - quantity) as orderPrice " +
                        "FROM Stock s, Product p " +
                        "WHERE p.DID = ? AND s.BID = ? " +
                        "  AND s.SKU=p.SKU AND maxquantity-quantity > 0";

        return getData(user,pass,(con) -> {
            PreparedStatement stmt = con.prepareStatement(select_str);
            stmt.setString(1, did);
            stmt.setString(2, bid);
            return stmt;
        }, (rs) -> {
            String name = rs.getString("Name");
            String sku = rs.getString("SKU");
            BigDecimal price = rs.getBigDecimal("Price");
            int orderQuantity = rs.getInt("orderQuantity");
            BigDecimal orderPrice = rs.getBigDecimal("orderPrice");
            return new Order(new Product(sku, name, price, null),
                    orderQuantity, orderPrice);
        });
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
    public static List<Inventory> createInventoryCount(String user, String pass,String bid) throws SQLException {
        String select_str =
                "SELECT Name, s.SKU, Price, Quantity " +
                        "FROM Stock s, Product p " +
                        "WHERE s.SKU = p.SKU AND s.BID = ?";

        System.out.println("Create Statement...");

        return getData(user,pass,(con) -> {
            PreparedStatement stmt = con.prepareStatement(select_str);
            stmt.setString(1, bid);
            return stmt;
        }, (rs) -> {
            String name = rs.getString("Name");
            String sku = rs.getString("SKU");
            BigDecimal price = rs.getBigDecimal("Price");
            int quantity = rs.getInt("Quantity");
            return new Inventory(new Product(sku, name, price, null),
                    quantity);
        });
    }

    //Query 13
    public static List<ISale> createSaleReport(String user, String pass,java.util.Date startDate, java.util.Date endDate) throws SQLException {
        String select_str =
                "SELECT S.Snum, s.Payment, s.saleDate, c.cid as cid, " +
                        "c.Name AS cname, p.SKU as sku,p.Name AS pname, " +
                        "e.eid as eid, e.Name AS ename " +
                        "FROM Sale s, Customer c, Product p, Employee e " +
                        "WHERE ? <= s.SALEDATE AND s.SALEDATE <= ? AND " +
                        "s.CID = c.CID AND s.SKU = p.SKU AND s.EID = e.EID " +
                        "ORDER BY s.saleDate ASC";

        return getData(user,pass,(con) -> {
            PreparedStatement stmt = con.prepareStatement(select_str);
            java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
            java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());
            stmt.setDate(1,sqlStartDate);
            stmt.setDate(2,sqlEndDate);
            return stmt;
        }, (rs) -> {
            String snum = rs.getString("Snum");
            String payment = rs.getString("Payment");
            java.sql.Date date = rs.getDate("saleDate");
            String cname = rs.getString("cname");
            String cid = rs.getString("cid");
            String pname = rs.getString("pname");
            String sku = rs.getString("sku");
            String ename = rs.getString("ename");
            String eid = rs.getString("eid");
            Sale sale = new Sale(snum, payment, date,
                    new Product(sku, pname, null, null),
                    new Customer(cid, cname, null, null),
                    new Employee(eid, ename, null, null, null, null, null));
            return sale;
        });
    }

    //Query 14a
    public static List<EmployeeReport> createEmployeeSaleReport(String user, String pass,java.util.Date startDate, java.util.Date endDate) throws SQLException {
        String select_str =
                "SELECT e.EID, COUNT(e.EID) count, SUM(price) sum " +
                        "FROM Sale s, Employee e, Product p " +
                        "WHERE s.eid = e.eid AND s.SKU = p.SKU " +
                        "AND ? <= SALEDATE AND SALEDATE <= ? " +
                        "GROUP BY e.EID ORDER BY COUNT(e.EID), SUM(price) DESC";

        return getData(user,pass,(con) -> {
            PreparedStatement stmt = con.prepareStatement(select_str);
            java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
            java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());
            stmt.setDate(1,sqlStartDate);
            stmt.setDate(2,sqlEndDate);
            return stmt;
        }, (rs) -> {
            String eid = rs.getString("EID");
            int count = rs.getInt("count");
            BigDecimal  price = rs.getBigDecimal("sum");
            EmployeeReport er = new EmployeeReport(eid, count, price);
            return er;
        });
    }

    //Query 14b
    public static List<AggregateEmployeeReport> createEmployeeSaleReport(String user, String pass,java.util.Date startDate, java.util.Date endDate, Aggregate agg) throws SQLException {
        String inner_select_str =
                "SELECT e.EID, COUNT(e.EID) count, SUM(price) sum " +
                        "FROM Sale s, Employee e, Product p " +
                        "WHERE s.eid = e.eid AND s.SKU = p.SKU " +
                        "AND ? <= SALEDATE AND SALEDATE <= ? " +
                        "GROUP BY e.EID ORDER BY COUNT(e.EID) DESC";
        String select_str;
        switch (agg) {
            case AVERAGE:
                select_str = "SELECT AVG(sum) FROM ("+inner_select_str+")";
                break;

            case MIN:
                select_str = "SELECT MIN(sum) FROM ("+inner_select_str+")";
                break;

            case MAX:
                select_str = "SELECT MAX(sum) FROM ("+inner_select_str+")";
                break;

            case COUNT:
                select_str = "SELECT COUNT(sum) FROM ("+inner_select_str+")";
                break;
            default:
                throw new IllegalArgumentException("Unsupported Aggregation");
        }

        return getData(user,pass,(con) -> {
            PreparedStatement stmt = con.prepareStatement(select_str);
            java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
            java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());
            stmt.setDate(1,sqlStartDate);
            stmt.setDate(2,sqlEndDate);
            return stmt;
        }, (rs) -> {
            return new AggregateEmployeeReport(rs.getBigDecimal(1));
        });
    }

    //Query 15a
    public static List<BranchReport> createProductBranchSaleReport(String user, String pass,java.util.Date startDate, java.util.Date endDate) throws SQLException {
        String select_str =
                "SELECT SKU, BID, COUNT(SKU) count " +
                        "FROM Sale s, Employee e " +
                        "WHERE s.eid = e.eid " +
                        "AND ? <= SALEDATE AND SALEDATE <= ? " +
                        "GROUP BY SKU, BID ORDER BY BID, COUNT(SKU) DESC";

        return getData(user,pass,(con) -> {
            PreparedStatement stmt = con.prepareStatement(select_str);
            java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
            java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());
            stmt.setDate(1,sqlStartDate);
            stmt.setDate(2,sqlEndDate);
            return stmt;
        }, (rs) -> {
            String sku = rs.getString("SKU");
            String bid = rs.getString("BID");
            int count = rs.getInt("count");
            BranchReport br = new BranchReport(sku, bid, count);
            return br;
        });
    }

    //Query 15b
    public static List<AggregateBranchReport> createProductBranchSaleReport(String user, String pass,java.util.Date startDate, java.util.Date endDate, Aggregate agg) throws SQLException {
        String inner_select_str =
                "SELECT SKU, BID, COUNT(SKU) count " +
                        "FROM Sale s, Employee e " +
                        "WHERE s.eid = e.eid " +
                        "AND ? <= SALEDATE AND SALEDATE <= ? " +
                        "GROUP BY SKU, BID ORDER BY BID, COUNT(SKU) DESC";
        String select_str;
        switch(agg){
            case AVERAGE:
                select_str = "SELECT SKU, AVG(count) agg FROM ("+inner_select_str+") GROUP BY SKU ORDER BY AVG(count) DESC";
                break;

            case MIN:
                select_str = "SELECT SKU, MIN(count) agg FROM ("+inner_select_str+") GROUP BY SKU ORDER BY MIN(count)";
                break;

            case MAX:
                select_str = "SELECT SKU, MAX(count) agg FROM ("+inner_select_str+") GROUP BY SKU ORDER BY MAX(count)";
                break;

            case COUNT:
                select_str = "SELECT SKU, COUNT(count) agg FROM ("+inner_select_str+") GROUP BY SKU ORDER BY COUNT(count)";
                break;
            default:
                throw new IllegalArgumentException("Unsupported Aggregation");

        }
        System.out.println("Create Statement...");

        return getData(user,pass,(con) -> {
            PreparedStatement stmt = con.prepareStatement(select_str);
            java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
            java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());
            stmt.setDate(1,sqlStartDate);
            stmt.setDate(2,sqlEndDate);
            return stmt;
        }, (rs) -> {
            String sku = rs.getString("SKU");
            BigDecimal num = rs.getBigDecimal("agg");
            AggregateBranchReport br = new AggregateBranchReport(sku,num);
            return br;
        });
    }

    //Query 16
    public static List<IBranch> stocksAllProducts(String user, String pass) throws SQLException {
        String select_str =
                "SELECT * FROM Branch b " +
                        "WHERE NOT EXISTS " +
                        "((SELECT p.SKU FROM Product p)" +
                        "MINUS" +
                        "(SELECT s.SKU FROM Stock s " +
                        "WHERE s.BID = b.BID))";
        return getData(user,pass,(con) -> con.prepareStatement(select_str), Branch::fromResultSet);
    }

    //Query 17
    public static void removeProduct(Connection connection, String sku) throws SQLException{
        String remove_str = "DELETE FROM PRODUCT WHERE SKU=? ";
        System.out.println("Create Statement...");
        PreparedStatement stmt = connection.prepareStatement(remove_str);
        stmt.setString(1,sku);
        System.out.println("Execute...");
        stmt.executeUpdate();
        connection.commit();
        System.out.println("Changes Commited");
    }
    //Query 18
    public static void removeStock(Connection connection, String bid, String sku) throws SQLException{
        String remove_str = "DELETE FROM STOCK WHERE BID=? AND SKU=? ";
        System.out.println("Create Statement...");
        PreparedStatement stmt = connection.prepareStatement(remove_str);
        stmt.setString(1,bid);
        stmt.setString(2,sku);
        System.out.println("Execute...");
        stmt.executeUpdate();
        connection.commit();
        System.out.println("Changes Commited");
    }
}
