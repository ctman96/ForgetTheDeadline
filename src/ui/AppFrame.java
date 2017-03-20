package ui;

import data.IDistributor;
import data.IProduct;
import ui.view.ProductView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class AppFrame extends JFrame {

    private JMenuBar menuBar;
    private JList viewList;
    private JPanel viewPanel;
    private JLabel console;

    private ProductView productView = new ProductView();

    public AppFrame() {
        super("ForgetTheDeadline");

        this.menuBar = new JMenuBar();

        // List of tables
        String[] data = {
                "Branch",
                "Employee",
                "Distributor",
                "Product",
                "Stock",
                "Customer",
                "Membership",
                "Sale"
        };
        this.viewList = new JList<>(data);

        // Table view PLACEHOLDER
        this.viewPanel = new JPanel();

        // Debug console
        this.console = new JLabel();
        this.console.setHorizontalAlignment(SwingConstants.LEFT);

        this.viewList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.viewList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String table = (String) viewList.getModel().getElementAt(viewList.getSelectedIndex());
                onTableSelect(table);
            }
        });

        JScrollPane listScrollPane = new JScrollPane(viewList);

        this.setJMenuBar(this.menuBar);
        this.getContentPane().add(listScrollPane, BorderLayout.LINE_START);
        this.getContentPane().add(this.viewPanel, BorderLayout.CENTER);
        this.getContentPane().add(this.console, BorderLayout.PAGE_END);
        this.pack();
    }

    private void onTableSelect(String table) {
        this.log(String.format("%s selected", table));

        this.viewPanel.removeAll();
        switch (table) {
            case "Product":
                this.viewPanel.add(this.productView);
                break;
        }

        this.pack();
    }

    private void log(String message) {
        this.console.setText(String.format("<html>%s</html>", message));
    }
}
