package sql.data;

import data.IDeveloper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Developer implements IDeveloper {
    protected String id;
    protected String name;
    protected String phone;
    protected String address;

    protected Developer() {};

    public Developer(String id, String name, String phone, String address) {
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

    public static Developer fromResultSet(ResultSet rs) throws SQLException {
        String id = rs.getString("DID");
        String name = rs.getString("Name");
        String phone = rs.getString("Phone");
        String address = rs.getString("Address");
        return new Developer(id, name, phone, address);
    }
}
