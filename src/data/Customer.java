package data;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer implements ICustomer {
    protected String id;
    protected String name;
    protected String phone;
    protected String address;

    protected Customer() {}

    public Customer(String id, String name, String phone, String address) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public String getAddress() {
        return address;
    }

    public static Customer fromResultSet(ResultSet rs) throws SQLException {
        String id = rs.getString("CID");
        String name = rs.getString("Name");
        String phone = rs.getString("Phone");
        String address = rs.getString("Address");
        return new Customer(id, name, phone, address);
    }
}
