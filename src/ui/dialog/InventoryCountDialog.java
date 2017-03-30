package ui.dialog;

import data.IBranch;
import ui.field.ObjectSelectField;

import java.awt.*;
import java.util.Vector;

public class InventoryCountDialog extends CheckedInputDialog<IBranch> {
    private ObjectSelectField<IBranch> branchField;

    public InventoryCountDialog(Frame owner, Vector<IBranch> branches) {
        super(owner);
        this.setTitle("Inventory...");

        this.branchField = new ObjectSelectField<>(branches);
        this.branchField.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                ObjectSelectField.defaultRenderer.getListCellRendererComponent(list, value.getAddress(), index, isSelected, cellHasFocus));

        CheckedInputComponent[] inputComponents = {
                makeCheckedInputComponent("Branch:", branchField),
        };

        this.setInputComponents(inputComponents);

        this.branchField.addActionListener(defaultActionListener);
    }

    public IBranch getInputValue() {
        return branchField.getInputValue();
    }
}
