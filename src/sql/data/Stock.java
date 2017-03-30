package sql.data;

import data.IBranch;
import data.IProduct;
import data.IStock;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class Stock implements IStock {
    IProduct product;
    IBranch branch;
    int quantity;
    int maxQuantity;

    protected Stock() {}

    public Stock(IProduct product, IBranch branch, int quantity, int maxQuantity) {
        this.product = product;
        this.branch = branch;
        this.quantity = quantity;
        this.maxQuantity = maxQuantity;
    }

    @Override
    public IBranch getBranch() {
        return branch;
    }

    @Override
    public IProduct getProduct() {
        return product;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public int getMaxQuantity() {
        return maxQuantity;
    }

    public static Stock fromResultSet(ResultSet rs, Map<String, IProduct> skuProductMap, Map<String, IBranch> idBranchMap) throws SQLException {
        IProduct product = skuProductMap.get(rs.getString("SKU"));
        IBranch branch = idBranchMap.get(rs.getString("BID"));
        int quantity = rs.getInt("Quantity");
        int maxQuantity = rs.getInt("maxQuantity");
        return new Stock(product, branch, quantity, maxQuantity);
    }
}
