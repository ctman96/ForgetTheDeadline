package ui.view;

import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.function.Function;

public abstract class DataTableModel<T> extends AbstractTableModel {
    interface DataTableColumn<T> {
        String getColumnName();
        Function<T, Object> getRenderer();
    }

    private Vector<DataTableColumn<T>> columns;
    private Vector<T> data;

    public DataTableModel(Vector<DataTableColumn<T>> columns) {
        this.columns = columns;
        this.data = new Vector<T>(0);
    }

    public void setData(Vector<T> newData) {
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
        return columns.get(i).getColumnName();
    }

    @Override
    public int getRowCount() {
        return this.data.size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        T value = data.get(rowIndex);
        return columns.get(columnIndex).getRenderer().apply(value);
    }
}
