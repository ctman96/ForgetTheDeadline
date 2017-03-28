package ui.view;

import data.IDeveloper;

import javax.swing.*;

public class DeveloperView extends JTable {
    public DeveloperView() {
        super(new ProductTableModel());
    }

    public void setData(IDeveloper[] newData) {
        ((ProductTableModel)this.getModel()).setData(newData);
    }

    private static class ProductTableModel extends DataTableModel<IDeveloper> {

        ProductTableModel() {
            super(IDeveloper.class, new DataTableColumn[] {
                    createColumn("Name", (IDeveloper developer) -> developer.getName()),
                    createColumn("Address", (IDeveloper developer) -> developer.getAddress()),
                    createColumn("Phone", (IDeveloper developer) -> developer.getPhone())
            });
        }
    }

}
