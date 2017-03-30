package sql.data;

import data.IProduct;

import java.math.BigDecimal;

public class Inventory {
    private IProduct product;
    private int quantity;

    public Inventory(IProduct product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public IProduct getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
