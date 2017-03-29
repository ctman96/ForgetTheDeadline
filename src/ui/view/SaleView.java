package ui.view;

import data.ISale;

import javax.swing.*;
import java.util.Vector;

public class SaleView extends JTable {
    public SaleView() {
        super(new ProductTableModel());
    }

    public void setData(Vector<ISale> newData) {
        ((ProductTableModel)this.getModel()).setData(newData);
    }

    private static class ProductTableModel extends DataTableModel<ISale> {

        static final Vector<DataTableColumn<ISale>> columns;
        static {
            columns = new Vector<DataTableColumn<ISale>>(6);
            columns.add(createColumn("SaleNumber", ISale::getSaleNumber));
            columns.add(createColumn("Payment", ISale::getPayment));
            columns.add(createColumn("Date", ISale::getDate));
            columns.add(createColumn("Product", ISale::getProduct));
            columns.add(createColumn("Customer", ISale::getCustomer));
            columns.add(createColumn("Employee", ISale::getEmployee));
        }

        ProductTableModel() {
            super(columns);
        }
    }

}