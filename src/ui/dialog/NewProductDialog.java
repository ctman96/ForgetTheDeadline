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

public class NewProductDialog extends JDialog implements CheckedInput<IProduct> {

    private static final DecimalFormat priceFormat = new DecimalFormat("#0.00");
    static {
        priceFormat.setMaximumFractionDigits(2);
        priceFormat.setParseBigDecimal(true);
    }

    private StringTextField skuField;
    private StringTextField nameField;
    private DecimalTextField priceField;
    private ObjectSelectField<IDistributor> distributorField;

    private JButton okButton;
    private JButton cancelButton;
    private boolean ok;

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
        super(owner);
        this.setTitle("New Product...");

        JLabel skuLabel = new JLabel("SKU:");
        JLabel nameLabel = new JLabel("Name:");
        JLabel priceLabel = new JLabel("Price:");
        JLabel distributorLabel = new JLabel("Distributor:");

        this.skuField = new StringTextField();
        this.nameField = new StringTextField();
        this.priceField = new DecimalTextField(priceFormat);
        this.distributorField = new ObjectSelectField<>(distributors);

        this.okButton = new JButton("OK");
        this.okButton.addActionListener((e) -> dispose());
        this.okButton.setEnabled(false);

        this.cancelButton = new JButton("Cancel");
        this.cancelButton.addActionListener((e) -> {
            setOk(false);
            dispose();
        });

        DocumentListener listener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onValueChange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onValueChange();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                onValueChange();
            }
        };
        this.skuField.getDocument().addDocumentListener(listener);
        this.nameField.getDocument().addDocumentListener(listener);
        this.priceField.getDocument().addDocumentListener(listener);
        this.distributorField.addActionListener((e) -> onValueChange());

        Container contentPane = this.getContentPane();
        GroupLayout layout = new GroupLayout(contentPane);
        contentPane.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(
                layout.createParallelGroup()
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(skuLabel)
                                        .addComponent(nameLabel)
                                        .addComponent(priceLabel)
                                        .addComponent(distributorLabel))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                        .addComponent(skuField)
                                        .addComponent(nameField)
                                        .addComponent(priceField)
                                        .addComponent(distributorField)))
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(okButton)
                                .addComponent(cancelButton))

        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(skuLabel)
                                .addComponent(skuField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(nameLabel)
                                .addComponent(nameField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(priceLabel)
                                .addComponent(priceField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(distributorLabel)
                                .addComponent(distributorField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(okButton)
                                .addComponent(cancelButton))
        );
    }

    private void onValueChange() {
        this.setOk(
            skuField.isInputValid() &&
            nameField.isInputValid() &&
            priceField.isInputValid() &&
            distributorField.isInputValid()
        );
    }

    private void setOk(boolean ok) {
        this.ok = ok;
        this.okButton.setEnabled(this.ok);
    }

    public boolean isInputValid() {
        return this.ok;
    }

    public IProduct getInputValue() {
        return this.product;
    }
}
