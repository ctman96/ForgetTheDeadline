package data;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class Employee implements IEmployee {
    protected String id;
    protected String name;
    protected String phone;
    protected String address;
    protected BigDecimal wage;
    protected String positionName;
    protected IBranch branch;

    protected Employee() {}

    public Employee(String id, String name, String phone, String address, BigDecimal wage, String positionName, IBranch branch) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.wage = wage;
        this.positionName = positionName;
        this.branch = branch;
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
    public String getAddress() {
        return address;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public BigDecimal getWage() {
        return wage;
    }

    @Override
    public String getPositionName() {
        return positionName;
    }

    @Override
    public IBranch getBranch() {
        return branch;
    }

    public static Employee fromResultSet(ResultSet rs, Map<String, IBranch> branchIdMap) throws SQLException {
        String id = rs.getString("EID");
        String name = rs.getString("Name");
        String phone = rs.getString("Phone");
        String address = rs.getString("Address");
        BigDecimal wage = rs.getBigDecimal("Wage");
        String pname = rs.getString("pname");
        IBranch branch = branchIdMap.get(rs.getString("BID"));
        return new Employee(id, name, phone, address, wage, pname, branch);
    }
}
