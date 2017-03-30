package sql.data;

import data.IProduct;

import java.math.BigDecimal;

public class Order {
    private IProduct product;
    private int quantity;
    private BigDecimal totalPrice;

    public Order(IProduct product, int quantity, BigDecimal totalPrice) {
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public IProduct getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
}
