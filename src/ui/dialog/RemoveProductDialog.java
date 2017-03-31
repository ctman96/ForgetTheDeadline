package ui.dialog;

import data.IProduct;
import ui.field.ObjectSelectField;

import java.awt.*;
import java.util.Vector;

public class RemoveProductDialog extends CheckedInputDialog<IProduct> {
    private ObjectSelectField<IProduct> productField;


    public RemoveProductDialog(Frame owner, Vector<IProduct> stock) {
        super(owner);
        this.setTitle("Remove Employee...");

        this.productField = new ObjectSelectField<>(stock);
        this.productField.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                ObjectSelectField.defaultRenderer.getListCellRendererComponent(list, value.getSKU()+" "+value.getName(), index, isSelected, cellHasFocus));
      
        CheckedInputComponent[] inputComponents = {
                makeCheckedInputComponent("Stock:", productField)
        };

        this.setInputComponents(inputComponents);
        
        this.productField.addActionListener(defaultActionListener);
    }

    public IProduct getInputValue() {
        return this.productField.getInputValue();
    }
}
