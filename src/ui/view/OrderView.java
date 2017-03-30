package ui.view;

import sql.data.Order;

import javax.swing.*;
import java.util.Vector;

public class OrderView extends JTable {
    public OrderView() {
        super(new ProductTableModel());
    }

    public void setData(Vector<Order> newData) {
        ((ProductTableModel)this.getModel()).setData(newData);
    }

    private static class ProductTableModel extends DataTableModel<Order> {

        static final Vector<DataTableColumn<Order>> columns;
        static {
            columns = new Vector<DataTableColumn<Order>>(5);
            columns.add(createColumn("SKU", (o) -> o.getProduct().getSKU()));
            columns.add(createColumn("Name", (o) -> o.getProduct().getName()));
            columns.add(createColumn("Price per Item", (o) -> o.getProduct().getPrice()));
            columns.add(createColumn("Quantity", Order::getQuantity));
            columns.add(createColumn("Total Price", Order::getTotalPrice));
        }

        ProductTableModel() {
            super(columns);
        }
    }
}
