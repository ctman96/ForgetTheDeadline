package ui.view;

import sql.data.AggregateBranchReport;
import sql.data.BranchReport;

import javax.swing.*;
import java.util.Vector;

public class AggregateBranchReportView extends JTable {
    public AggregateBranchReportView() {
        super(new ProductTableModel());
    }

    public void setData(Vector<AggregateBranchReport> newData) {
        ((ProductTableModel)this.getModel()).setData(newData);
    }

    private static class ProductTableModel extends DataTableModel<AggregateBranchReport> {

        static final Vector<DataTableColumn<AggregateBranchReport>> columns;
        static {
            columns = new Vector<DataTableColumn<AggregateBranchReport>>(3);
            columns.add(createColumn("SKU", (o) -> o.getSku()));
            columns.add(createColumn("Aggregate", (o) -> o.getNum()));
        }

        ProductTableModel() {
            super(columns);
        }
    }

}
