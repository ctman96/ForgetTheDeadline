package ui.view;

import data.IProduct;

import javax.swing.*;
import java.util.Vector;

public class ProductView extends JTable {
    public ProductView() {
        super(new ProductTableModel());
    }

    public void setData(Vector<IProduct> newData) {
        ((ProductTableModel)this.getModel()).setData(newData);
    }

    private static class ProductTableModel extends DataTableModel<IProduct> {

        static final Vector<DataTableColumn<IProduct>> columns;
        static {
            columns = new Vector<>(4);
            columns.add(createColumn("SKU", IProduct::getSKU));
            columns.add(createColumn("Name", IProduct::getName));
            columns.add(createColumn("Price", IProduct::getPrice));
            columns.add(createColumn("Developer", (p) -> p.getDeveloper().getName()));
        }

        ProductTableModel() {
            super(columns);
        }
    }

}
