package ui.dialog;


import data.IBranch;
import data.IEmployee;
import ui.field.DecimalTextField;
import ui.field.ObjectSelectField;
import ui.field.StringTextField;

import java.awt.*;
import java.math.BigDecimal;
import java.util.Vector;

public class NewEmployeeDialog extends CheckedInputDialog<IEmployee> {
    private StringTextField nameField;
    private StringTextField addressField;
    private StringTextField phoneField;
    private DecimalTextField wageField;
    private StringTextField positionNameField;
    
    private ObjectSelectField<IBranch> branchField;

    private IEmployee employee = new IEmployee() {
        @Override
        public String getId() {
            return null;
        }

        @Override
        public String getName() {
            return nameField.getInputValue();
        }
        
        @Override
        public String getAddress() {
            return addressField.getInputValue();
        }
        
        @Override
        public String getPhone() {
            return phoneField.getInputValue();
        }
        
        @Override
        public BigDecimal getWage() throws NumberFormatException {
            return wageField.getInputValue();
        }
        
        @Override
        public String getPositionName() {
            return positionNameField.getInputValue();
        }

        @Override
        public IBranch getBranch() {
            return branchField.getInputValue();
        }
    };

    public NewEmployeeDialog(Frame owner, Vector<IBranch> branch) {
        super(owner);
        this.setTitle("New Employee...");

        this.nameField = new StringTextField();
        this.addressField = new StringTextField();
        this.phoneField = new StringTextField();
        this.wageField = new DecimalTextField(defaultPriceFormat);
        this.positionNameField = new StringTextField();

        this.branchField = new ObjectSelectField<>(branch);
        this.branchField.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                ObjectSelectField.defaultRenderer.getListCellRendererComponent(list, value.getAddress(), index, isSelected, cellHasFocus));

        CheckedInputComponent[] inputComponents = {
                makeCheckedInputComponent("Name:", nameField),
                makeCheckedInputComponent("Address:", addressField),
                makeCheckedInputComponent("Phone:", phoneField),
                makeCheckedInputComponent("Wage:", wageField),
                makeCheckedInputComponent("Position Name:", positionNameField),
                
                makeCheckedInputComponent("Branch:", branchField)
        };

        this.setInputComponents(inputComponents);

        this.nameField.getDocument().addDocumentListener(defaultDocumentListener);
        this.addressField.getDocument().addDocumentListener(defaultDocumentListener);
        this.phoneField.getDocument().addDocumentListener(defaultDocumentListener);
        this.wageField.getDocument().addDocumentListener(defaultDocumentListener);
        this.positionNameField.getDocument().addDocumentListener(defaultDocumentListener);
        
        this.branchField.addActionListener(defaultActionListener);
    }

    public IEmployee getInputValue() {
        return this.employee;
    }
}