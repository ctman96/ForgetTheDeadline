package sql.data;

import data.IBranch;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Branch implements IBranch {
    protected String id;
    protected String phone;
    protected String address;

    protected Branch() {}

    public Branch(String id, String phone, String address) {
        this.id = id;
        this.phone = phone;
        this.address = address;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public String getAddress() {
        return address;
    }

    public static Branch fromResultSet(ResultSet rs) throws SQLException {
        String id = rs.getString("BID");
        String phone = rs.getString("Phone");
        String address = rs.getString("Address");
        return new Branch(id, phone, address);
    }
}
