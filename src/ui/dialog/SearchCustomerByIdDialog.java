package ui.dialog;

import ui.field.StringTextField;

import java.awt.*;

public class SearchCustomerByIdDialog extends CheckedInputDialog<String> {
    private StringTextField idField;

    public SearchCustomerByIdDialog(Frame owner) {
        super(owner);
        this.setTitle("Search Customer by ID...");

        idField = new StringTextField();

        CheckedInputComponent[] inputComponents = {
                makeCheckedInputComponent("Customer ID:", idField)
        };

        this.setInputComponents(inputComponents);

        this.idField.getDocument().addDocumentListener(defaultDocumentListener);
    }

    public String getInputValue() {
        return this.idField.getInputValue();
    }
}
