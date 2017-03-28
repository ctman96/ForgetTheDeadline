package ui.view;

import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Array;
import java.util.function.Function;

public abstract class DataTableModel<T> extends AbstractTableModel {
    interface DataTableColumn<T> {
        String getColumnName();
        Function<T, Object> getRenderer();
    }

    private DataTableColumn[] columns;
    protected T[] data;

    public DataTableModel(Class<T> dataType, DataTableColumn<T>[] columns) {
        this.columns = columns;
        this.data = (T[]) Array.newInstance(dataType, 0);
    }

    public void setData(T[] newData) {
        this.data = newData;
        this.fireTableDataChanged();
    }

    static public <R> DataTableColumn<R> createColumn(String name, Function<R, Object> renderer) {
        return new DataTableColumn<R>() {
            @Override
            public String getColumnName() {
                return name;
            }

            @Override
            public Function<R, Object> getRenderer() {
                return renderer;
            }
        };
    }

    @Override
    public String getColumnName(int i) {
        return columns[i].getColumnName();
    }

    @Override
    public int getRowCount() {
        return this.data.length;
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        T value = data[rowIndex];
        return columns[columnIndex].getRenderer().apply(value);
    }
}
