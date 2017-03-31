package ui.dialog;

import ui.field.DecimalTextField;

import java.awt.*;
import java.math.BigDecimal;

public class SearchProductPriceDialog extends CheckedInputDialog {
    private DecimalTextField priceField;

    public SearchProductPriceDialog(Frame owner) {
        super(owner);
        this.setTitle("Search Products Below Price...");

        priceField = new DecimalTextField(defaultPriceFormat);

        CheckedInputComponent[] inputComponents = {
                makeCheckedInputComponent("Price:", priceField)
        };

        this.setInputComponents(inputComponents);

        this.priceField.getDocument().addDocumentListener(defaultDocumentListener);
    }

    public BigDecimal getInputValue() throws NumberFormatException{
        return priceField.getInputValue();
    }
}
