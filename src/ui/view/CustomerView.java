package ui.view;

import data.ICustomer;
import data.IDeveloper;

import javax.swing.*;
import java.util.Vector;

public class CustomerView extends JTable {
    public CustomerView() {
        super(new CustomerTableModel());
    }

    public void setData(Vector<ICustomer> newData) {
        ((CustomerTableModel)this.getModel()).setData(newData);
    }

    private static class CustomerTableModel extends DataTableModel<ICustomer> {

        static final Vector<DataTableColumn<ICustomer>> columns;
        static {
            columns = new Vector<DataTableColumn<ICustomer>>(4);
            columns.add(createColumn("ID", ICustomer::getId));
            columns.add(createColumn("Name", ICustomer::getName));
            columns.add(createColumn("Address", ICustomer::getAddress));
            columns.add(createColumn("Phone", ICustomer::getPhone));
        }

        CustomerTableModel() {
            super(columns);
        }
    }

}
