package ui.view;

import data.IBranch;

import javax.swing.*;
import java.util.Vector;

public class BranchView extends JTable {
    public BranchView() {
        super(new ProductTableModel());
    }

    public void setData(Vector<IBranch> newData) {
        ((ProductTableModel)this.getModel()).setData(newData);
    }

    private static class ProductTableModel extends DataTableModel<IBranch> {

        static final Vector<DataTableColumn<IBranch>> columns;
        static {
            columns = new Vector<DataTableColumn<IBranch>>(3);
            columns.add(createColumn("ID", IBranch::getId));
            columns.add(createColumn("Address", IBranch::getAddress));
            columns.add(createColumn("Phone", IBranch::getPhone));
        }

        ProductTableModel() {
            super(columns);
        }
    }

}
