package ui.view;

import sql.data.Inventory;

import javax.swing.*;
import java.util.Vector;

public class InventoryView extends JTable {
    public InventoryView() {
        super(new ProductTableModel());
    }

    public void setData(Vector<Inventory> newData) {
        ((ProductTableModel)this.getModel()).setData(newData);
    }

    private static class ProductTableModel extends DataTableModel<Inventory> {

        static final Vector<DataTableColumn<Inventory>> columns;
        static {
            columns = new Vector<DataTableColumn<Inventory>>(5);
            columns.add(createColumn("SKU", (o) -> o.getProduct().getSKU()));
            columns.add(createColumn("Name", (o) -> o.getProduct().getName()));
            columns.add(createColumn("Price per Item", (o) -> o.getProduct().getPrice()));
            columns.add(createColumn("Quantity On Hand", Inventory::getQuantity));
        }

        ProductTableModel() {
            super(columns);
        }
    }
}
