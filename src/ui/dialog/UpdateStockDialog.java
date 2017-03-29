//package ui.dialog;
//
//import data.IBranch;
//import data.IProduct;
//import data.IStock;
//import ui.field.IntegerTextField;
//import ui.field.ObjectSelectField;
//
//import javax.swing.*;
//import java.awt.*;
//import java.util.Vector;
//
//public class UpdateStockDialog extends CheckedInputDialog<IStock> {
//    private ObjectSelectField<IStock> stockField;
//    private IntegerTextField quantityField;
//
//    private IStock stock = new IStock() {
//        @Override
//        public IBranch getBranch() {
//            return stockField.getInputValue().getBranch();
//        }
//
//        @Override
//        public IProduct getProduct() {
//            return stockField.getInputValue().getProduct();
//        }
//
//        @Override
//        public int getQuantity() {
//            return quantityField.getInputValue();
//        }
//
//        @Override
//        public int getMaxQuantity() {
//            return stockField.getInputValue().getMaxQuantity();
//        }
//    };
//
//    public UpdateStockDialog(Frame owner, Vector<IStock> stocks) {
//        super(owner);
//        this.setTitle("Update Stock...");
//
//        this.stockField = new ObjectSelectField<>(stocks);
//        this.quantityField = new IntegerTextField();
//        this.stockField.setRenderer(new ListCellRenderer<IStock>() {
//            @Override
//            public Component getListCellRendererComponent(JList<? extends IStock> list, IStock value, int index, boolean isSelected, boolean cellHasFocus) {
//                return new JLabel(String.format("Branch %s:%s", value.getBranch().getId(), value.getProduct().getName()));
//            }
//        });
//
//        CheckedInputComponent[] inputComponents = {
//                makeCheckedInputComponent("Stock:", stockField),
//                makeCheckedInputComponent("Quantity:", quantityField)
//        };
//
//        this.setInputComponents(inputComponents);
//
//        this.stockField.addActionListener(defaultActionListener);
//        this.quantityField.getDocument().addDocumentListener(defaultDocumentListener);
//    }
//
//    public IStock getInputValue() {
//        return this.stock;
//    }
//}
