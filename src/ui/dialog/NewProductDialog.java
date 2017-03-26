package ui.dialog;

import data.IDistributor;
import data.IProduct;
import ui.util.CheckedInput;
import ui.field.DecimalTextField;
import ui.field.ObjectSelectField;
import ui.field.StringTextField;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class NewProductDialog extends CheckedInputDialog<IProduct> {
    private StringTextField skuField;
    private StringTextField nameField;
    private DecimalTextField priceField;
    private ObjectSelectField<IDistributor> distributorField;

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
        public IDistributor getDistributor() {
            return distributorField.getInputValue();
        }
    };

    public NewProductDialog(Frame owner, IDistributor[] distributors) {
        super(owner, new CheckedInputComponent[0]); // TODO
        this.setTitle("New Product...");

        this.skuField = new StringTextField();
        this.nameField = new StringTextField();
        this.priceField = new DecimalTextField(defaultPriceFormat);
        this.distributorField = new ObjectSelectField<>(distributors);

        CheckedInputComponent[] inputComponents = {
            makeCheckedInputComponent("SKU:", skuField),
                makeCheckedInputComponent("Name:", nameField),
                makeCheckedInputComponent("Price:", priceField),
                makeCheckedInputComponent("Distributor:", distributorField),
        };
        
        this.skuField.getDocument().addDocumentListener(defaultDocumentListener);
        this.nameField.getDocument().addDocumentListener(defaultDocumentListener);
        this.priceField.getDocument().addDocumentListener(defaultDocumentListener);
        this.distributorField.addActionListener(defaultActionListener);

    }

    public IProduct getInputValue() {
        return this.product;
    }
}
