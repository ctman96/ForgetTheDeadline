package ui;

import ui.view.ProductView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.function.Consumer;

public class AppFrame extends JFrame {

    private JMenuBar menuBar;
    private JList viewList;
    private JPanel viewPanel;
    private JLabel console;

    public interface ViewItem {
        /**
         * @return The name to display as a view item
         */
        public String getName();

        /**
         * @return The callback to call when view item is selected
         */
        public Consumer<AppFrame> getCallback();
    }

    private ViewItem[] viewItems;

    public AppFrame() {
        super("ForgetTheDeadline");

        this.menuBar = new JMenuBar();

        // View list
        this.viewList = new JList();
        this.viewList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.viewList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                onViewSelect(viewList.getSelectedIndex());
            }
        });
        this.setViewItems(new ViewItem[0]);

        // View container
        this.viewPanel = new JPanel();

        // Debug status bar
        this.console = new JLabel();
        this.console.setHorizontalAlignment(SwingConstants.LEFT);

        JScrollPane listScrollPane = new JScrollPane(viewList);

        this.setJMenuBar(this.menuBar);
        this.getContentPane().add(listScrollPane, BorderLayout.LINE_START);
        this.getContentPane().add(this.viewPanel, BorderLayout.CENTER);
        this.getContentPane().add(this.console, BorderLayout.PAGE_END);
        this.pack();
    }

    public void setViewItems(ViewItem[] viewItems) {
        String[] viewNames = new String[viewItems.length];
        for (int i = 0; i < viewItems.length; i++) {
            viewNames[i] = viewItems[i].getName();
        }
        this.viewList.setListData(viewNames);
        this.viewItems = viewItems;
    }

    private void onViewSelect(int index) {
        this.log(String.format("%s selected", this.viewItems[index].getName()));
        this.viewItems[index].getCallback().accept(this);
    }

    public void clearView() {
        this.viewPanel.removeAll();
        this.pack();
    }

    public void setView(JComponent component) {
        this.viewPanel.removeAll();
        this.viewPanel.add(component);
        this.pack();
    }

    /**
     * @param message an html and plain string
     */
    public void log(String message) {
        this.console.setText(message);
    }
}
