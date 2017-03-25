package ui.view;

import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Array;

public abstract class DataTableModel<T, E extends Enum<E>> extends AbstractTableModel {
    private Class<T> dataType;
    private Class<E> columnEnum;
    protected T[] data;

    public DataTableModel(Class<T> dataType, Class<E> columnEnum) {
        this.dataType = dataType;
        this.columnEnum = columnEnum;
        this.data = (T[]) Array.newInstance(this.dataType, 0);
    }

    public void setData(T[] newData) {
        this.data = newData;
        this.fireTableDataChanged();
    }

    @Override
    public String getColumnName(int i) {
        return columnEnum.getEnumConstants()[i].name();
    }

    @Override
    public int getRowCount() {
        return this.data.length;
    }

    @Override
    public int getColumnCount() {
        return columnEnum.getEnumConstants().length;
    }
}
