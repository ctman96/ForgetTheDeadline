package ui.dialog;

import data.IBranch;
import data.IDeveloper;
import data.IProduct;
import data.IStock;
import ui.field.ObjectSelectField;

import java.awt.*;
import java.math.BigDecimal;
import java.util.Vector;

/**
 * Warning: Does not really return an IStock. Only developer ID and Branch ID are valid.
 */
public class RestockOrderDialog extends CheckedInputDialog<IStock> {
    private ObjectSelectField<IBranch> branchField;
    private ObjectSelectField<IDeveloper> developerField;

    private IStock stock = new IStock() {
        @Override
        public IBranch getBranch() {
            return branchField.getInputValue();
        }

        @Override
        public IProduct getProduct() {
            return new IProduct() {
                @Override
                public String getSKU() {
                    return null;
                }

                @Override
                public String getName() {
                    return null;
                }

                @Override
                public BigDecimal getPrice() {
                    return null;
                }

                @Override
                public IDeveloper getDeveloper() {
                    return developerField.getInputValue();
                }
            };
        }

        @Override
        public int getQuantity() {
            return 0;
        }

        @Override
        public int getMaxQuantity() {
            return 0;
        }
    };

    public RestockOrderDialog(Frame owner, Vector<IBranch> branches, Vector<IDeveloper> developers) {
        super(owner);
        this.setTitle("Generate Restock Order...");

        this.branchField = new ObjectSelectField<>(branches);
        this.branchField.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                ObjectSelectField.defaultRenderer.getListCellRendererComponent(list, value.getId()+" "+value.getAddress(), index, isSelected, cellHasFocus));
        this.developerField = new ObjectSelectField<>(developers);
        this.developerField.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                ObjectSelectField.defaultRenderer.getListCellRendererComponent(list, value.getId()+" "+value.getName(), index, isSelected, cellHasFocus));

        CheckedInputComponent[] inputComponents = {
                makeCheckedInputComponent("Branch:", branchField),
                makeCheckedInputComponent("Developer:", developerField),
        };

        this.setInputComponents(inputComponents);

        this.branchField.addActionListener(defaultActionListener);
        this.developerField.addActionListener(defaultActionListener);
    }

    public IStock getInputValue() {
        return this.stock;
    }
}
