package ui.dialog;

import data.IDistributor;
import data.IProduct;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.math.BigDecimal;
import java.security.acl.Group;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;

public class NewProductDialog extends JDialog {

    static final DecimalFormat priceFormat = new DecimalFormat("#0.00");
    static {
        priceFormat.setMinimumFractionDigits(2);
        priceFormat.setParseBigDecimal(true);
    }

    private JTextField skuField;
    private JTextField nameField;
    private JTextField priceField;
    private JComboBox distributorField;

    private JButton okButton;
    private JButton cancelButton;
    private boolean ok;

    private IProduct product = new IProduct() {
        @Override
        public String getSKU() {
            return skuField.getText();
        }

        @Override
        public String getName() {
            return nameField.getText();
        }

        @Override
        public BigDecimal getPrice() throws NumberFormatException {
            return new BigDecimal(priceField.getText());
        }

        @Override
        public IDistributor getDistributor() {
            return null; // TODO
        }
    };

    public NewProductDialog(Frame owner) {
        super(owner);
        this.setTitle("New Product...");

        JLabel skuLabel = new JLabel("SKU:");
        JLabel nameLabel = new JLabel("Name:");
        JLabel priceLabel = new JLabel("Price:");
        JLabel distributorLabel = new JLabel("Distributor:");

        this.skuField = new JTextField();
        this.nameField = new JTextField();
        this.priceField = new JFormattedTextField(priceFormat);
        this.distributorField = new JComboBox(); // TODO

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
        // TODO

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

    public void onValueChange() {
        boolean ok = true;
        if (this.product.getName().isEmpty()) {
            ok = false;
        }

        if (this.product.getSKU().isEmpty()) {
            ok = false;
        }

        try {
            this.product.getPrice();
        } catch (NumberFormatException e) {
            ok = false;
        }

        // TODO handle distributor
        this.setOk(ok);
    }

    private void setOk(boolean ok) {
        this.ok = ok;
        this.okButton.setEnabled(this.ok);
    }

    public boolean isOk() {
        return this.ok;
    }

    public IProduct getInput() {
        return this.product;
    }
}
