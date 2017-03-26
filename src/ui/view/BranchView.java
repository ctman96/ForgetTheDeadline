package ui.view;

import data.IBranch;

import javax.swing.*;

public class BranchView extends JTable {
    public BranchView() {
        super(new BranchTableModel());
    }

    public void setData(IBranch[] newData) {
        ((BranchTableModel)this.getModel()).setData(newData);
    }

    private static class BranchTableModel extends DataTableModel<IBranch, BranchTableModel.ColumnNames>
    {
        enum ColumnNames
        {
            BID, Phone, Address
        }

        BranchTableModel()
        {
            super(IBranch.class, ColumnNames.class);
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex)
        {
            IBranch branch = this.data[rowIndex];
            switch (columnIndex) {
                case 0:
                    return branch.getId();
                case 1:
                    return branch.getPhone();
                case 2:
                    return branch.getAddress();
            }
            return null;
        }
    }

}