package ui;

import data.*;
import sql.GameStoreDB;
import sql.function.SQLConsumer;
import ui.dialog.*;
import ui.view.*;

import javax.swing.*;
import java.awt.*;
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

        this.refreshViews();

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
                    showDialog(dialog, true);

                    if (dialog.isInputValid()) {
                        try {
                            IProduct product = dialog.getInputValue();
                            GameStoreDB.withConnection((con) -> {
                                GameStoreDB.addGameDatabase(con, product.getName(), product.getSKU(), product.getPrice(), product.getDeveloper().getId());
                            });
                        } catch (SQLException e) {
                            showErrorDialog(e.getMessage());
                        }
                    } else {
                        appFrame.log("Canceled");
                    }
                });
                newMenu.add(newDeveloperMenuItem);

                JMenuItem newCustomerMenuItem = makeMenuItem("Customer...", () -> {
                    NewCustomerDialog dialog = new NewCustomerDialog(this.appFrame);
                    showDialog(dialog, true);
                    if (dialog.isInputValid()) {
                        try {
                            ICustomer customer = dialog.getInputValue();
                            GameStoreDB.withConnection((con) -> { // Place holder CID for generated PK
                                GameStoreDB.addCustomer(con, customer.getName(), "[PH]ID", customer.getPhone(), customer.getAddress());
                            });
                        } catch (SQLException e) {
                            showErrorDialog(e.getMessage());
                        }
                    } else {
                        appFrame.log("Canceled");
                    }
                });
                newMenu.add(newCustomerMenuItem);

                JMenuItem newEmployeeMenuItem = makeMenuItem("Employee...", () -> {
                    NewEmployeeDialog dialog = new NewEmployeeDialog(this.appFrame, new Vector<>(gameStore.branch));
                    showDialog(dialog, true);
                    if (dialog.isInputValid()) {
                        try {
                            IEmployee employee = dialog.getInputValue();
                            GameStoreDB.withConnection((con) -> { // Place holder EID for generated PK
                                GameStoreDB.addEmployee(con, "[PH]ID", employee.getName(), employee.getBranch().getId(), employee.getWage(), employee.getPositionName(), employee.getPhone(), employee.getAddress());
                            });
                        } catch (SQLException e) {
                            showErrorDialog(e.getMessage());
                        }
                    } else {
                        appFrame.log("Canceled");
                    }
                });
                newMenu.add(newEmployeeMenuItem);

                JMenuItem newStockMenuItem = makeMenuItem("Stock...", () -> {
                    NewStockDialog dialog = new NewStockDialog(this.appFrame, new Vector<>(gameStore.branch), new Vector<>(gameStore.product));
                    showDialog(dialog, true);
                    if (dialog.isInputValid()) {
                        try {
                            IStock stock = dialog.getInputValue();
                            GameStoreDB.withConnection((con) -> {
                                GameStoreDB.addGameStore(con, stock.getBranch().getId(), stock.getProduct().getSKU(), stock.getQuantity(), stock.getMaxQuantity());
                            });
                        } catch (SQLException e) {
                            showErrorDialog(e.getMessage());
                        }
                    } else {
                        appFrame.log("Canceled");
                    }
                });
                newMenu.add(newEmployeeMenuItem);
            }
            fileMenu.add(newMenu);

            JMenuItem removeEmployeeMenuItem = makeMenuItem("Remove employee...", () -> {
                RemoveEmployeeDialog dialog = new RemoveEmployeeDialog (this.appFrame, new Vector<>(gameStore.employee));
                showDialog(dialog, true);

                if (dialog.isInputValid()) {
                    IEmployee employee = dialog.getInputValue();
                    try {
                        GameStoreDB.withConnection((con) -> {
                            GameStoreDB.removeEmployee(con, employee.getId());
                        });
                    } catch (SQLException e) {
                        showErrorDialog(e.getMessage());
                    }
                } else {
                    appFrame.log("Canceled");
                }
            });
            fileMenu.add(removeEmployeeMenuItem);

            JMenuItem makePurchaseMenuItem = makeMenuItem("Make Purchase...", () -> {
                NewSaleDialog dialog = new NewSaleDialog(this.appFrame, new Vector<>(gameStore.product), new Vector<>(gameStore.customer), new Vector<>(gameStore.employee));
                showDialog(dialog, true);

                if (dialog.isInputValid()) {
                    ISale sale = dialog.getInputValue();
                    try {
                        GameStoreDB.withConnection((con) -> {
                            GameStoreDB.buyProduct(con, sale.getProduct().getSKU(), sale.getEmployee().getId(), sale.getPayment(), sale.getCustomer().getId());
                        });
                    } catch (SQLException e) {
                        showErrorDialog(e.getMessage());
                    }
                } else {
                    appFrame.log("Canceled");
                }
            });

            JMenuItem updateStockMenuItem = makeMenuItem("Add stock to...", () -> {
                UpdateStockDialog dialog = new UpdateStockDialog(this.appFrame, new Vector<>(gameStore.stock));
                showDialog(dialog, true);

                if (dialog.isInputValid()) {
                    IStock stock = dialog.getInputValue();
                    try {
                        GameStoreDB.withConnection((con) -> {
                            GameStoreDB.updateProductQuantity(con, stock.getBranch().getId(), stock.getProduct().getSKU(), stock.getQuantity());
                        });
                    } catch (SQLException e) {
                        showErrorDialog(e.getMessage());
                    }
                } else {
                    appFrame.log("Canceled");
                }
            });
            fileMenu.add(updateStockMenuItem);
        }
        menuBar.add(fileMenu);

        JMenuItem refreshMenuItem = makeMenuItem("Refresh", this::refreshViews);
        menuBar.add(refreshMenuItem);
    }

    private void refreshViews() {
        try {
            this.gameStore = GameStoreDB.getGameStore();
            this.developerView.setData(new Vector<>(gameStore.developer));
            this.branchView.setData(new Vector<>(gameStore.branch));
            this.productView.setData(new Vector<>(gameStore.product));
            this.customerView.setData(new Vector<>(gameStore.customer));
            this.employeeView.setData(new Vector<>(gameStore.employee));
            this.saleView.setData(new Vector<>(gameStore.sale));
            this.stockView.setData(new Vector<>(gameStore.stock));
        } catch (SQLException e) {
            showErrorDialog("Refresh failed:\n" + e.getMessage());
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

    private void showErrorDialog(String text) {
        JOptionPane.showMessageDialog(this.appFrame, text, "Oops...", JOptionPane.ERROR_MESSAGE);
    }

    private static void showDialog(Dialog dialog, boolean modal) {
        dialog.pack();
        dialog.setModal(modal);
        dialog.setVisible(true);
    }
}
