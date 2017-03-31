package ui.dialog;

import data.IDeveloper;
import data.IProduct;
import ui.field.DecimalTextField;
import ui.field.ObjectSelectField;
import ui.field.StringTextField;

import java.awt.*;
import java.math.BigDecimal;
import java.util.Vector;

public class UpdateProductPriceDialog extends CheckedInputDialog<IProduct> {
    private ObjectSelectField<IProduct> productField;
    private DecimalTextField priceField;

    private IProduct product = new IProduct() {
        @Override
        public String getSKU() {
            return productField.getInputValue().getSKU();
        }

        @Override
        public String getName() {
            return productField.getInputValue().getSKU();
        }

        @Override
        public BigDecimal getPrice() throws NumberFormatException {
            return priceField.getInputValue();
        }

        @Override
        public IDeveloper getDeveloper() {
            return productField.getInputValue().getDeveloper();
        }
    };

    public UpdateProductPriceDialog(Frame owner, Vector<IProduct> products) {
        super(owner);
        this.setTitle("Change Product Price...");

        this.productField = new ObjectSelectField<>(products);
        this.productField.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                ObjectSelectField.defaultRenderer.getListCellRendererComponent(list, value.getSKU()+" "+value.getName(), index, isSelected, cellHasFocus));
        this.priceField = new DecimalTextField(defaultPriceFormat);

        CheckedInputComponent[] inputComponents = {
                makeCheckedInputComponent("Product:", productField),
                makeCheckedInputComponent("New Price:", priceField)
        };

        this.setInputComponents(inputComponents);

        this.productField.addActionListener(defaultActionListener);
        this.priceField.getDocument().addDocumentListener(defaultDocumentListener);
    }

    public IProduct getInputValue() {
        return this.product;
    }
}
