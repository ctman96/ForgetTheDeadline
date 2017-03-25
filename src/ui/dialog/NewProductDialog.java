package ui.dialog;

import javax.swing.*;
import java.awt.*;
import java.security.acl.Group;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;

public class NewProductDialog extends JDialog {

    JTextField skuField;
    JTextField nameField;
    JTextField priceField;
    JComboBox distributorField;

    public NewProductDialog(Frame owner) {
        super(owner);
        this.setTitle("New Product...");

        JPanel labelPane = new JPanel(new GridLayout(0, 1));
        JLabel skuLabel = new JLabel("SKU:");
        JLabel nameLabel = new JLabel("Name:");
        JLabel priceLabel = new JLabel("Price:");
        JLabel distributorLabel = new JLabel("Distributor:");
        labelPane.add(skuLabel);
        labelPane.add(nameLabel);
        labelPane.add(priceLabel);
        labelPane.add(distributorLabel);

        JPanel fieldPane = new JPanel(new GridLayout(0, 1));
        this.skuField = new JTextField();
        this.nameField = new JTextField();
        DecimalFormat priceFormat = new DecimalFormat("#0.00");
        priceFormat.setMaximumIntegerDigits(2);
        this.priceField = new JFormattedTextField(priceFormat);
        this.distributorField = new JComboBox(); // TODO
        fieldPane.add(skuField);
        fieldPane.add(nameField);
        fieldPane.add(priceField);
        fieldPane.add(distributorField);

        Container contentPane = this.getContentPane();
        GroupLayout layout = new GroupLayout(contentPane);
        contentPane.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(skuLabel)
                                .addComponent(nameLabel)
                                .addComponent(priceLabel)
                                .addComponent(distributorLabel))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(skuField)
                                .addComponent(nameField)
                                .addComponent(priceField)
                                .addComponent(distributorField))
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
        );
    }
}
