package ui;

import data.Developer;
import data.Employee;
import data.GameStore;
import data.IDeveloper;
import ui.dialog.NewCustomerDialog;
import ui.dialog.NewProductDialog;
//import ui.dialog.UpdateStockDialog;
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

        this.refreshView();

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

        {
            JMenu fileMenu = new JMenu("File");

            JMenuItem newMenuItem = new JMenuItem("New...");
            newMenuItem.setActionCommand("dialog");
            newMenuItem.addActionListener((ActionEvent e) -> {
                if (e.getActionCommand().equals("dialog")) {
                    NewProductDialog dialog = new NewProductDialog(this.appFrame, new Vector<>(gameStore.developer));
                    dialog.pack();
                    dialog.setModal(true);
                    dialog.setVisible(true);
                }
            });
            fileMenu.add(newMenuItem);

            JMenuItem newCustomerMenuItem = new JMenuItem("New Customer...");
            newCustomerMenuItem.setActionCommand("dialog");
            newCustomerMenuItem.addActionListener((ActionEvent e) -> {
                if (e.getActionCommand().equals("dialog")) {
                    NewCustomerDialog dialog = new NewCustomerDialog(this.appFrame);
                    dialog.pack();
                    dialog.setModal(true);
                    dialog.setVisible(true);
                }
            });
            fileMenu.add(newCustomerMenuItem);

//            JMenuItem updateStockMenuItem = new JMenuItem("Update stock...");
//            newMenuItem.setActionCommand("dialog");
//            newMenuItem.addActionListener((ActionEvent e) -> {
//                if (e.getActionCommand().equals("dialog")) {
//                    UpdateStockDialog dialog = new UpdateStockDialog(this.appFrame, new Vector<>(gameStore.stock));
//                    dialog.pack();
//                    dialog.setModal(true);
//                    dialog.setVisible(true);
//                }
//            });
//            fileMenu.add(newMenuItem);

            menuBar.add(fileMenu);
        }

        JMenuItem refreshMenuItem = new JMenuItem("Refresh");
        refreshMenuItem.setActionCommand("refresh");;
        refreshMenuItem.addActionListener((ActionEvent e) -> {
            if (e.getActionCommand().equals("refresh")) {
                this.refreshView();
            }
        });
        menuBar.add(refreshMenuItem);

    }

    private void refreshView() {
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
}
