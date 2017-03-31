package ui.dialog;

import data.IBranch;
import data.IProduct;
import data.IStock;
import ui.field.IntegerTextField;
import ui.field.ObjectSelectField;

import java.awt.*;
import java.util.Vector;

public class NewStockDialog extends CheckedInputDialog<IStock> {
    private ObjectSelectField<IBranch> branchField;
    private ObjectSelectField<IProduct> productField;
    private IntegerTextField quantityField;
    private IntegerTextField maxQuantityField;

    private IStock stock = new IStock() {
        @Override
        public IBranch getBranch() {
            return branchField.getInputValue();
        }

        @Override
        public IProduct getProduct() {
            return productField.getInputValue();
        }

        @Override
        public int getQuantity() {
            return quantityField.getInputValue();
        }

        @Override
        public int getMaxQuantity() {
            return maxQuantityField.getInputValue();
        }
    };

    public NewStockDialog(Frame owner, Vector<IBranch> branches, Vector<IProduct> products) {
        super(owner);
        this.setTitle("Update Stock...");

        this.branchField = new ObjectSelectField<>(branches);
        this.branchField.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                ObjectSelectField.defaultRenderer.getListCellRendererComponent(list, value.getId()+" "+value.getAddress(), index, isSelected, cellHasFocus));
        this.productField = new ObjectSelectField<>(products);
        this.productField.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                ObjectSelectField.defaultRenderer.getListCellRendererComponent(list, value.getSKU()+" "+value.getName(), index, isSelected, cellHasFocus));
        this.quantityField = new IntegerTextField();
        this.maxQuantityField = new IntegerTextField();

        CheckedInputComponent[] inputComponents = {
                makeCheckedInputComponent("Branch:", branchField),
                makeCheckedInputComponent("Product:", productField),
                makeCheckedInputComponent("Quantity:", quantityField),
                makeCheckedInputComponent("Max Quantity:", maxQuantityField)
        };

        this.setInputComponents(inputComponents);

        this.branchField.addActionListener(defaultActionListener);
        this.productField.addActionListener(defaultActionListener);
        this.quantityField.getDocument().addDocumentListener(defaultDocumentListener);
        this.maxQuantityField.getDocument().addDocumentListener(defaultDocumentListener);
    }

    public IStock getInputValue() {
        return this.stock;
    }
}
