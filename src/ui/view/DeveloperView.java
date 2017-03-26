package ui.view;

import data.IDeveloper;

import javax.swing.*;

public class DeveloperView extends JTable {
    public DeveloperView() {
        super(new DeveloperTableModel());
    }

    public void setData(IDeveloper[] newData) {
        ((DeveloperTableModel)this.getModel()).setData(newData);
    }

    private static class DeveloperTableModel extends DataTableModel<IDeveloper, DeveloperTableModel.ColumnNames>
    {
        enum ColumnNames
        {
            BID, Phone, Address
        }

        DeveloperTableModel()
        {
            super(IDeveloper.class, ColumnNames.class);
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex)
        {
            IDeveloper developer = this.data[rowIndex];
            switch (columnIndex) {
                case 0:
                    return developer.getId();
                case 1:
                    return developer.getName();
                case 2:
                    return developer.getAddress();
                case 3:
                    return developer.getPhone();
            }
            return null;
        }
    }

}