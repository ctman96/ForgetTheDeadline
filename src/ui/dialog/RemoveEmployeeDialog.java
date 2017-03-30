package ui.dialog;

import data.IEmployee;
import ui.field.ObjectSelectField;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class RemoveEmployeeDialog extends CheckedInputDialog<IEmployee> {
    private ObjectSelectField<IEmployee> employeeField;


    public RemoveEmployeeDialog(Frame owner, Vector<IEmployee> employees) {
        super(owner);
        this.setTitle("Remove Employee...");

        this.employeeField = new ObjectSelectField<>(employees);
        this.employeeField.setRenderer((list, value, index, isSelected, cellHasFocus) -> new JLabel(value.getName()));
      
        CheckedInputComponent[] inputComponents = {
                makeCheckedInputComponent("Employee:", employeeField)
        };

        this.setInputComponents(inputComponents);
        
        this.employeeField.addActionListener(defaultActionListener);
    }

    public IEmployee getInputValue() {
        return this.employeeField.getInputValue();
    }
}
