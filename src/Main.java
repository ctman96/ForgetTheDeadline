import java.lang.System;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Consumer;

import data.IDistributor;
import data.IProduct;
import oracle.jdbc.driver.OracleDriver;
import ui.AppFrame;
import ui.view.ProductView;

public class Main {
    public static void main(String[] args) {
        try {
            DriverManager.registerDriver(new OracleDriver());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        AppFrame frame = new AppFrame();
        frame.setDefaultCloseOperation(AppFrame.EXIT_ON_CLOSE);

        AppFrame.ViewItem[] viewItems = new AppFrame.ViewItem[2];
        // Example of Product view

        ProductView productView = new ProductView();
        IProduct[] data = new IProduct[1];
        data[0] = new IProduct() {

            @Override
            public String getSKU() {
                return "0001";
            }

            @Override
            public String getName() {
                return "name";
            }

            @Override
            public double getPrice() {
                return 12.75;
            }

            @Override
            public IDistributor getDistributor() {
                return new IDistributor() {
                    @Override
                    public String getId() {
                        return null;
                    }

                    @Override
                    public String getName() {
                        return "dummy";
                    }

                    @Override
                    public String getAddress() {
                        return null;
                    }

                    @Override
                    public String getPhone() {
                        return null;
                    }
                };
            }
        };
        productView.setData(data);

        viewItems[0] = new AppFrame.ViewItem() {
            @Override
            public String getName() {
                return "Product";
            }

            @Override
            public Consumer<AppFrame> getCallback() {
                return (AppFrame frame) -> frame.setView(productView);
            }
        };

        // Example of clear
        viewItems[1] = new AppFrame.ViewItem() {
            @Override
            public String getName() {
                return "clear";
            }

            @Override
            public Consumer<AppFrame> getCallback() {
                return AppFrame::clearView;
            }
        };

        frame.setViewItems(viewItems);

        frame.setDefaultCloseOperation(AppFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}