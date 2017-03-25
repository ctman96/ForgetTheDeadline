package ui.view;

import data.IProduct;

import javax.swing.*;

public class ProductView extends JTable {
    public ProductView() {
        super(new ProductTableModel());
    }

    public void setData(IProduct[] newData) {
        ((ProductTableModel)this.getModel()).setData(newData);
    }

    private static class ProductTableModel extends DataTableModel<IProduct, ProductTableModel.ColumnNames> {
        enum ColumnNames {
            SKU, Name, Price, Distributor
        }

        ProductTableModel() {
            super(IProduct.class, ColumnNames.class);
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            IProduct product = this.data[rowIndex];
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
