package data;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class Product implements IProduct {
    protected String sku;
    protected String name;
    protected BigDecimal price;
    protected IDeveloper developer;

    protected Product() {}

    public Product(String sku, String name, BigDecimal price, IDeveloper developer) {
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.developer = developer;
    }

    @Override
    public String getSKU() {
        return sku;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public IDeveloper getDeveloper() {
        return developer;
    }

    public static Product fromResultSet(ResultSet rs, Map<String, IDeveloper> idDeveloperMap) throws SQLException {
        String sku = rs.getString("SKU");
        String name = rs.getString("Name");
        BigDecimal price = rs.getBigDecimal("Price");
        IDeveloper developer = idDeveloperMap.get(rs.getString("DID"));
        return new Product(sku, name, price, developer);
    }
}
