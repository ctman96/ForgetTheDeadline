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
            columns.add(createColumn("ID", ISale::getSaleNumber));
            columns.add(createColumn("Payment", ISale::getPayment));
            columns.add(createColumn("Date", ISale::getDate));
            columns.add(createColumn("Product", (s) -> s.getProduct().getSKU() + " "+ s.getProduct().getName()));
            columns.add(createColumn("Customer", (s) -> s.getCustomer().getId() + " "+ s.getCustomer().getName()));
            columns.add(createColumn("Employee", (s) -> s.getEmployee().getId() + " "+ s.getEmployee().getName()));
        }

        ProductTableModel() {
            super(columns);
        }
    }

}