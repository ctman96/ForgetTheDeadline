package ui.view;

import data.IStock;

import javax.swing.*;
import java.util.Vector;

public class StockView extends JTable {
    public StockView() {
        super(new ProductTableModel());
    }

    public void setData(Vector<IStock> newData) {
        ((ProductTableModel)this.getModel()).setData(newData);
    }

    private static class ProductTableModel extends DataTableModel<IStock> {

        static final Vector<DataTableColumn<IStock>> columns;
        static {
            columns = new Vector<DataTableColumn<IStock>>(4);
            columns.add(createColumn("Branch", (s) -> s.getBranch().getId() + " "+ s.getBranch().getAddress()));
            columns.add(createColumn("Product", (s) -> s.getProduct().getSKU() + " "+ s.getProduct().getName()));
            columns.add(createColumn("Quantity", IStock::getQuantity));
            columns.add(createColumn("MaxQuantity", IStock::getMaxQuantity));
        }

        ProductTableModel() {
            super(columns);
        }
    }

}