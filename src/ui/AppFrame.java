package ui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class AppFrame extends JFrame {

    private JMenuBar menuBar;
    private JList tableList;
    private JPanel tableView;
    private JLabel console;

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
        this.tableList = new JList<>(data);

        // Table view
        this.tableView = new JPanel();

        // Debug console
        this.console = new JLabel();
        this.console.setHorizontalAlignment(SwingConstants.LEFT);

        this.tableList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.tableList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String table = (String) tableList.getModel().getElementAt(tableList.getSelectedIndex());
                onTableSelect(table);
            }
        });

        JScrollPane listScrollPane = new JScrollPane(tableList);

        this.setJMenuBar(this.menuBar);
        this.getContentPane().add(listScrollPane, BorderLayout.LINE_START);
        this.getContentPane().add(this.tableView, BorderLayout.CENTER);
        this.getContentPane().add(this.console, BorderLayout.PAGE_END);
        this.pack();
    }

    private void onTableSelect(String table) {
        this.log(String.format("%s selected", table));
    }

    private void log(String message) {
        this.console.setText(String.format("<html>%s</html>", message));
    }
}
