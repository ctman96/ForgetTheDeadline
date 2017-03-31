package ui.dialog;

import data.IEmployee;
import data.IStock;
import ui.field.ObjectSelectField;

import java.awt.*;
import java.util.Vector;

public class RemoveStockDialog extends CheckedInputDialog<IStock> {
    private ObjectSelectField<IStock> stockField;


    public RemoveStockDialog(Frame owner, Vector<IStock> stock) {
        super(owner);
        this.setTitle("Remove Employee...");

        this.stockField = new ObjectSelectField<>(stock);
        this.stockField.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                ObjectSelectField.defaultRenderer.getListCellRendererComponent(list, String.format("%s@%s", value.getProduct().getName(), value.getBranch().getAddress()), index, isSelected, cellHasFocus));
      
        CheckedInputComponent[] inputComponents = {
                makeCheckedInputComponent("Stock:", stockField)
        };

        this.setInputComponents(inputComponents);
        
        this.stockField.addActionListener(defaultActionListener);
    }

    public IStock getInputValue() {
        return this.stockField.getInputValue();
    }
}
