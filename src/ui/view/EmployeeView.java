package ui.view;

import data.IEmployee;

import javax.swing.*;
import java.util.Vector;

public class EmployeeView extends JTable {
    public EmployeeView() {
        super(new ProductTableModel());
    }

    public void setData(Vector<IEmployee> newData) {
        ((ProductTableModel)this.getModel()).setData(newData);
    }

    private static class ProductTableModel extends DataTableModel<IEmployee> {

        static final Vector<DataTableColumn<IEmployee>> columns;
        static {
            columns = new Vector<DataTableColumn<IEmployee>>(7);
            columns.add(createColumn("Id", IEmployee::getId));
            columns.add(createColumn("Name", IEmployee::getName));
            columns.add(createColumn("Address", IEmployee::getAddress));
            columns.add(createColumn("Phone", IEmployee::getPhone));
            columns.add(createColumn("Wage", IEmployee::getWage));
            columns.add(createColumn("PositionName", IEmployee::getPositionName));
            columns.add(createColumn("Branch", IEmployee::getBranch));
        }

        ProductTableModel() {
            super(columns);
        }
    }

}