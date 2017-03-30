package ui.view;

import data.IDeveloper;

import javax.swing.*;
import java.util.Vector;

public class DeveloperView extends JTable {
    public DeveloperView() {
        super(new ProductTableModel());
    }

    public void setData(Vector<IDeveloper> newData) {
        ((ProductTableModel)this.getModel()).setData(newData);
    }

    private static class ProductTableModel extends DataTableModel<IDeveloper> {

        static final Vector<DataTableColumn<IDeveloper>> columns;
        static {
            columns = new Vector<DataTableColumn<IDeveloper>>(3);
            columns.add(createColumn("Name", IDeveloper::getName));
            columns.add(createColumn("Address", IDeveloper::getAddress));
            columns.add(createColumn("Phone", IDeveloper::getPhone));
        }

        ProductTableModel() {
            super(columns);
        }
    }
}
