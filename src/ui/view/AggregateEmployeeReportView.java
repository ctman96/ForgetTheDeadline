package ui.view;

import sql.data.AggregateEmployeeReport;

import javax.swing.*;
import java.util.Vector;

public class AggregateEmployeeReportView extends JTable {
    public AggregateEmployeeReportView() {
        super(new ProductTableModel());
    }

    public void setData(Vector<AggregateEmployeeReport> newData) {
        ((ProductTableModel)this.getModel()).setData(newData);
    }

    private static class ProductTableModel extends DataTableModel<AggregateEmployeeReport> {

        static final Vector<DataTableColumn<AggregateEmployeeReport>> columns;
        static {
            columns = new Vector<DataTableColumn<AggregateEmployeeReport>>(3);
            columns.add(createColumn("Value", (o) -> o.getNum()));
        }

        ProductTableModel() {
            super(columns);
        }
    }

}
