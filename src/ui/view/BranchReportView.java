package ui.view;

import sql.data.BranchReport;

import javax.swing.*;
import java.util.Vector;

public class BranchReportView extends JTable {
    public BranchReportView() {
        super(new ProductTableModel());
    }

    public void setData(Vector<BranchReport> newData) {
        ((ProductTableModel)this.getModel()).setData(newData);
    }

    private static class ProductTableModel extends DataTableModel<BranchReport> {

        static final Vector<DataTableColumn<BranchReport>> columns;
        static {
            columns = new Vector<DataTableColumn<BranchReport>>(3);
            columns.add(createColumn("BID", (o) -> o.getId()));
            columns.add(createColumn("SKU", (o) -> o.getSku()));
            columns.add(createColumn("Count", (o) -> o.getCount()));
        }

        ProductTableModel() {
            super(columns);
        }
    }

}
