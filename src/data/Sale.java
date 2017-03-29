package data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

public class Sale implements ISale {
    String snum;
    String payment;
    Date date;
    IProduct product;
    ICustomer customer;
    IEmployee employee;

    protected Sale() {}

    public Sale(String snum, String payment, Date date, IProduct product, ICustomer customer, IEmployee employee) {
        this.snum = snum;
        this.payment = payment;
        this.date = date;
        this.product = product;
        this.customer = customer;
        this.employee = employee;
    }

    @Override
    public String getSaleNumber() {
        return snum;
    }

    @Override
    public String getPayment() {
        return payment;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public IProduct getProduct() {
        return product;
    }

    @Override
    public ICustomer getCustomer() {
        return customer;
    }

    @Override
    public IEmployee getEmployee() {
        return employee;
    }

    public static Sale fromResultSet(ResultSet rs, Map<String, IProduct> skuProductMap, Map<String, ICustomer> idCustomerMap, Map<String, IEmployee> idEmployeeMap) throws SQLException {
        String snum = rs.getString("snum");
        String payment = rs.getString("payment");
        Date date = rs.getDate("saleDate");
        IProduct product = skuProductMap.get(rs.getString("SKU"));
        ICustomer customer = idCustomerMap.get(rs.getString("CID"));
        IEmployee employee = idEmployeeMap.get(rs.getString("EID"));
        return new Sale(snum, payment, date, product, customer, employee);
    }
}
