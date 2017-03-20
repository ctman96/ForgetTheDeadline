package ui.view;

import data.IProduct;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

public class ProductView extends JScrollPane {

    static String[] columnNames = {
            "SKU",
            "Name",
            "Price",
            "Distributor"
    };

    private IProduct[] data;
    private JTable table;
    private ProductTableModel tableModel;

    public ProductView() {
        super();
        this.data = new IProduct[0];
        this.tableModel = new ProductTableModel();
        this.table = new JTable(this.tableModel);
        this.setViewportView(this.table);
    }

    public void setData(IProduct[] newData) {
        this.data = newData;
        this.tableModel.fireTableDataChanged();
    }

    private class ProductTableModel extends AbstractTableModel {
        @Override
        public String getColumnName(int i) {
            return columnNames[i];
        }

        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            IProduct product = data[rowIndex];
            switch (columnIndex) {
                case 0:
                    return product.getSKU();
                case 1:
                    return product.getName();
                case 2:
                    return product.getPrice();
                case 3:
                    return product.getDistributor().getName();
            }
            return null;
        }
    }

}
