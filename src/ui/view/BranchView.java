package ui.view;

import data.IBranch;

import javax.swing.*;

public class BranchView extends JTable {
    public BranchView() {
        super(new ProductTableModel());
    }

    public void setData(IBranch[] newData) {
        ((ProductTableModel)this.getModel()).setData(newData);
    }

    private static class ProductTableModel extends DataTableModel<IBranch> {

        ProductTableModel() {
            super(IBranch.class, new DataTableColumn[] {
                    createColumn("ID", (IBranch branch) -> branch.getId()),
                    createColumn("Address", (IBranch branch) -> branch.getAddress()),
                    createColumn("Phone", (IBranch branch) -> branch.getPhone())
            });
        }
    }

}
