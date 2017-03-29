package ui;

import data.GameStore;
import ui.view.BranchView;
import ui.view.DeveloperView;
import ui.view.ProductView;

import javax.swing.*;
import java.sql.SQLException;
import java.util.*;

public class AppFrameController {
    private AppFrame appFrame;
    private GameStore gameStore;

    public AppFrameController() {
        setupAppFrame();
    }

    public void setVisible(boolean visible) {
        appFrame.setVisible(visible);
    }

    private void setupAppFrame() {
        this.appFrame = new AppFrame();

        ArrayList<AppFrame.ViewItem> viewItems = new ArrayList<>();

        DeveloperView developerView = new DeveloperView();
        BranchView branchView = new BranchView();
        ProductView productView = new ProductView();

        viewItems.add(appFrame.makeViewItem("Product", productView));
        viewItems.add(appFrame.makeViewItem("Developer", developerView));
        viewItems.add(appFrame.makeViewItem("Branch", branchView));

        try {
            this.gameStore = GameStore.getGameStore();
            developerView.setData(new Vector<>(this.gameStore.developer));
            branchView.setData(new Vector<>(this.gameStore.branch));
            productView.setData(new Vector<>(this.gameStore.product));
        } catch (SQLException e) {
            e.printStackTrace();
        }

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
        this.appFrame.pack();
        this.appFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
