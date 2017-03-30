package ui;

import data.GameStore;
import ui.dialog.NewCustomerDialog;
import ui.dialog.NewProductDialog;
import ui.dialog.UpdateStockDialog;
import ui.view.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.*;

public class AppFrameController {
    private AppFrame appFrame;
    private GameStore gameStore;

    private ProductView productView;
    private DeveloperView developerView;
    private BranchView branchView;
    private CustomerView customerView;
    private EmployeeView employeeView;
    private SaleView saleView;
    private StockView stockView;

    public AppFrameController() {
        setupAppFrame();
    }

    public void setVisible(boolean visible) {
        appFrame.setVisible(visible);
    }

    private void setupAppFrame() {
        this.appFrame = new AppFrame();

        ArrayList<AppFrame.ViewItem> viewItems = new ArrayList<>();

        developerView = new DeveloperView();
        branchView = new BranchView();
        productView = new ProductView();
        customerView = new CustomerView();
        employeeView = new EmployeeView();
        saleView = new SaleView();
        stockView = new StockView();

        viewItems.add(appFrame.makeViewItem("Product", productView));
        viewItems.add(appFrame.makeViewItem("Developer", developerView));
        viewItems.add(appFrame.makeViewItem("Branch", branchView));
        viewItems.add(appFrame.makeViewItem("Customer", customerView));
        viewItems.add(appFrame.makeViewItem("Employee", employeeView));
        viewItems.add(appFrame.makeViewItem("Sale", saleView));
        viewItems.add(appFrame.makeViewItem("Stock", stockView));

        this.refreshVeiws();

        viewItems.add(new AppFrame.ViewItem() {
            @Override
            public String getName() {
                return "clear";
            }

            @Override
            public Runnable getCallback() {
                return appFrame::clearView;
            }
        });

        this.appFrame.setViewItems(viewItems.toArray(new AppFrame.ViewItem[viewItems.size()]));
        this.setupMenuBar();
        this.appFrame.pack();
        this.appFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void setupMenuBar() {
        JMenuBar menuBar = this.appFrame.getJMenuBar();

        JMenu fileMenu = new JMenu("File");
        {
            JMenu newMenu = new JMenu("New");
            {
                JMenuItem newDeveloperMenuItem = makeMenuItem("Product...", () -> {
                    NewProductDialog dialog = new NewProductDialog(this.appFrame, new Vector<>(gameStore.developer));
                    dialog.pack();
                    dialog.setModal(true);
                    dialog.setVisible(true);
                });
                newMenu.add(newDeveloperMenuItem);

                JMenuItem newCustomerMenuItem = makeMenuItem("Customer...", () -> {
                    NewCustomerDialog dialog = new NewCustomerDialog(this.appFrame);
                    dialog.pack();
                    dialog.setModal(true);
                    dialog.setVisible(true);
                });
                newMenu.add(newCustomerMenuItem);
            }
            fileMenu.add(newMenu);

            JMenuItem updateStockMenuItem = new JMenuItem("Update stock...");
            updateStockMenuItem.setActionCommand("update.stock");
            updateStockMenuItem.addActionListener((ActionEvent e) -> {
                if (e.getActionCommand().equals("update.stock")) {
                    UpdateStockDialog dialog = new UpdateStockDialog(this.appFrame, new Vector<>(gameStore.stock));
                    dialog.pack();
                    dialog.setModal(true);
                    dialog.setVisible(true);
                }
            });
            fileMenu.add(updateStockMenuItem);
        }
        menuBar.add(fileMenu);

        JMenuItem refreshMenuItem = makeMenuItem("Refresh", this::refreshVeiws);
        menuBar.add(refreshMenuItem);
    }

    private void refreshVeiws() {
        try {
            this.gameStore = GameStore.getGameStore();
            this.developerView.setData(new Vector<>(gameStore.developer));
            this.branchView.setData(new Vector<>(gameStore.branch));
            this.productView.setData(new Vector<>(gameStore.product));
            this.customerView.setData(new Vector<>(gameStore.customer));
            this.employeeView.setData(new Vector<>(gameStore.employee));
            this.saleView.setData(new Vector<>(gameStore.sale));
            this.stockView.setData(new Vector<>(gameStore.stock));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static JMenuItem makeMenuItem(String text, Runnable onClick) {
        JMenuItem menuItem = new JMenuItem(text);
        String actionCommand = "generated.on.click";
        menuItem.setActionCommand(actionCommand);
        menuItem.addActionListener((ActionEvent e) -> {
            if (e.getActionCommand().equals(actionCommand)) {
                onClick.run();
            }
        });
        return menuItem;
    }
}
