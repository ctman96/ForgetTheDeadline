package ui.dialog;

import data.*;
import ui.field.IntegerTextField;
import ui.field.ObjectSelectField;
import ui.field.StringTextField;

import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

public class NewSaleDialog extends CheckedInputDialog<ISale> {
    private ObjectSelectField<IProduct> productField;
    private ObjectSelectField<ICustomer> customerField;
    private StringTextField paymentField;
    private ObjectSelectField<IEmployee> employeeField;

    private ISale sale = new ISale() {
        @Override
        public String getSaleNumber() {
            return null;
        }

        @Override
        public String getPayment() {
            return paymentField.getInputValue();
        }

        @Override
        public Date getDate() {
            return null;
        }

        @Override
        public IProduct getProduct() {
            return productField.getInputValue();
        }

        @Override
        public ICustomer getCustomer() {
            return customerField.getInputValue();
        }

        @Override
        public IEmployee getEmployee() {
            return employeeField.getInputValue();
        }
    };

    public NewSaleDialog(Frame owner, Vector<IProduct> products, Vector<ICustomer> customers, Vector<IEmployee> employees) {
        super(owner);
        this.setTitle("New Sale...");

        this.productField = new ObjectSelectField<>(products);
        this.productField.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                ObjectSelectField.defaultRenderer.getListCellRendererComponent(list, value.getName(), index, isSelected, cellHasFocus));
        this.customerField = new ObjectSelectField<>(customers);
        this.customerField.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                ObjectSelectField.defaultRenderer.getListCellRendererComponent(list, value.getName(), index, isSelected, cellHasFocus));
        this.employeeField = new ObjectSelectField<>(employees);
        this.employeeField.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                ObjectSelectField.defaultRenderer.getListCellRendererComponent(list, value.getName(), index, isSelected, cellHasFocus));

        paymentField = new StringTextField();

        CheckedInputComponent[] inputComponents = {
                makeCheckedInputComponent("Product:", productField),
                makeCheckedInputComponent("Customer:", customerField),
                makeCheckedInputComponent("Payment:", paymentField),
                makeCheckedInputComponent("Employee:", employeeField)
        };

        this.setInputComponents(inputComponents);

        this.productField.addActionListener(defaultActionListener);
        this.customerField.addActionListener(defaultActionListener);
        this.employeeField.addActionListener(defaultActionListener);

        this.paymentField.getDocument().addDocumentListener(defaultDocumentListener);
    }

    public ISale getInputValue() {
        return this.sale;
    }
}
