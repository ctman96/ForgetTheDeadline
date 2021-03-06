package ui.dialog;

import data.IDeveloper;
import data.IProduct;
import ui.field.DecimalTextField;
import ui.field.ObjectSelectField;
import ui.field.StringTextField;

import java.awt.*;
import java.math.BigDecimal;
import java.util.Vector;

public class NewProductDialog extends CheckedInputDialog<IProduct> {
    private StringTextField skuField;
    private StringTextField nameField;
    private DecimalTextField priceField;
    private ObjectSelectField<IDeveloper> developerField;

    private IProduct product = new IProduct() {
        @Override
        public String getSKU() {
            return skuField.getInputValue();
        }

        @Override
        public String getName() {
            return nameField.getInputValue();
        }

        @Override
        public BigDecimal getPrice() throws NumberFormatException {
            return priceField.getInputValue();
        }

        @Override
        public IDeveloper getDeveloper() {
            return developerField.getInputValue();
        }
    };

    public NewProductDialog(Frame owner, Vector<IDeveloper> developers) {
        super(owner);
        this.setTitle("New Product...");

        this.skuField = new StringTextField();
        this.nameField = new StringTextField();
        this.priceField = new DecimalTextField(defaultPriceFormat);
        this.developerField = new ObjectSelectField<>(developers);
        this.developerField.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                ObjectSelectField.defaultRenderer.getListCellRendererComponent(list, value.getId()+" "+value.getName(), index, isSelected, cellHasFocus));

        CheckedInputComponent[] inputComponents = {
                makeCheckedInputComponent("SKU:", skuField),
                makeCheckedInputComponent("Name:", nameField),
                makeCheckedInputComponent("Price:", priceField),
                makeCheckedInputComponent("Developer:", developerField),
        };

        this.setInputComponents(inputComponents);
        
        this.skuField.getDocument().addDocumentListener(defaultDocumentListener);
        this.nameField.getDocument().addDocumentListener(defaultDocumentListener);
        this.priceField.getDocument().addDocumentListener(defaultDocumentListener);
        this.developerField.addActionListener(defaultActionListener);

    }

    public IProduct getInputValue() {
        return this.product;
    }
}
