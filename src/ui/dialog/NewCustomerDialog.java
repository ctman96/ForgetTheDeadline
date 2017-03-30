package ui.dialog;

import data.ICustomer;
import ui.field.StringTextField;

import java.awt.*;

public class NewCustomerDialog extends CheckedInputDialog<ICustomer> {
    StringTextField nameField;
    StringTextField phoneField;
    StringTextField addressField;

    ICustomer customer = new ICustomer() {
        @Override
        public String getId() {
            return null;
        }

        @Override
        public String getName() {
            return nameField.getInputValue();
        }

        @Override
        public String getPhone() {
            return phoneField.getInputValue();
        }

        @Override
        public String getAddress() {
            return addressField.getInputValue();
        }
    };

    public NewCustomerDialog(Frame owner) {
        super(owner);
        this.setTitle("New Customer...");

        this.nameField = new StringTextField();
        this.phoneField = new StringTextField();
        this.addressField = new StringTextField();

        CheckedInputComponent[] inputComponents = {
                makeCheckedInputComponent("Name:", nameField),
                makeCheckedInputComponent("Phone:", phoneField),
                makeCheckedInputComponent("Address:", addressField),
        };

        this.setInputComponents(inputComponents);

        this.nameField.getDocument().addDocumentListener(defaultDocumentListener);
        this.phoneField.getDocument().addDocumentListener(defaultDocumentListener);
        this.addressField.getDocument().addDocumentListener(defaultDocumentListener);
    }

    public ICustomer getInputValue() {
        return this.customer;
    }
}
