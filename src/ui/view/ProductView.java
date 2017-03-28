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

    private static class ProductTableModel extends DataTableModel<IProduct> {

        ProductTableModel() {
            super(IProduct.class, new DataTableColumn[] {
                    createColumn("SKU", (IProduct product) -> product.getSKU()),
                    createColumn("Name", (IProduct product) -> product.getName()),
                    createColumn("Price", (IProduct product) -> product.getPrice()),
                    createColumn("Developer", (IProduct product) -> product.getDeveloper().getName())
            });
        }
    }

}
