package ui.view;

import sql.data.EmployeeReport;

import javax.swing.*;
import java.util.Vector;

public class EmployeeReportView extends JTable {
    public EmployeeReportView() {
        super(new ProductTableModel());
    }

    public void setData(Vector<EmployeeReport> newData) {
        ((ProductTableModel)this.getModel()).setData(newData);
    }

    private static class ProductTableModel extends DataTableModel<EmployeeReport> {

        static final Vector<DataTableColumn<EmployeeReport>> columns;
        static {
            columns = new Vector<DataTableColumn<EmployeeReport>>(3);
            columns.add(createColumn("ID", (o) -> o.getId()));
            columns.add(createColumn("Count", (o) -> o.getCount()));
            columns.add(createColumn("Sum", (o) -> o.getSum()));
        }

        ProductTableModel() {
            super(columns);
        }
    }

}
