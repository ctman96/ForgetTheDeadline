package ui.dialog;

import data.ICustomer;
import ui.field.StringTextField;

import java.awt.*;

public class SearchCustomerByNameDialog extends CheckedInputDialog<ICustomer> {
    private StringTextField nameField;
    private StringTextField phoneField;

    private ICustomer customer = new ICustomer() {
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
            return null;
        }
    };

    public SearchCustomerByNameDialog(Frame owner) {
        super(owner);
        this.setTitle("Search Customer by Name and Phone...");

        nameField = new StringTextField();
        phoneField = new StringTextField();

        CheckedInputComponent[] inputComponents = {
                makeCheckedInputComponent("Name:", nameField),
                makeCheckedInputComponent("Phone:", phoneField)
        };

        this.setInputComponents(inputComponents);

        this.nameField.getDocument().addDocumentListener(defaultDocumentListener);
        this.phoneField.getDocument().addDocumentListener(defaultDocumentListener);
    }

    public ICustomer getInputValue() {
        return this.customer;
    }
}
