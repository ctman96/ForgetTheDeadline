package ui;

import data.IDeveloper;
import data.IProduct;
import ui.view.ProductView;

import javax.swing.*;
import java.math.BigDecimal;

public class AppFrameController {
    private AppFrame appFrame;

    public AppFrameController() {
        setupAppFrame();
    }

    public void setVisible(boolean visible) {
        appFrame.setVisible(visible);
    }

    private void setupAppFrame() {
        this.appFrame = new AppFrame();

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
            public BigDecimal getPrice() {
                return new BigDecimal(12.75);
            }

            @Override
            public IDeveloper getDeveloper() {
                return new IDeveloper() {
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
            public Object getName() {
                return "Product";
            }

            @Override
            public Runnable getCallback() {
                return () -> appFrame.setView(productView);
            }
        };

        // Example of clear
        viewItems[1] = new AppFrame.ViewItem() {
            @Override
            public String getName() {
                return "clear";
            }

            @Override
            public Runnable getCallback() {
                return appFrame::clearView;
            }
        };

        this.appFrame.setViewItems(viewItems);
        this.appFrame.pack();
        this.appFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}