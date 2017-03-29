package ui;

import data.IBranch;
import data.IDeveloper;
import data.IProduct;
import sql.GameStoreDB;
import ui.view.BranchView;
import ui.view.DeveloperView;
import ui.view.ProductView;

import javax.swing.*;
import java.sql.SQLException;
import java.util.*;

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

        ArrayList<AppFrame.ViewItem> viewItems = new ArrayList<>();

        DeveloperView developerView = new DeveloperView();
        BranchView branchView = new BranchView();
        ProductView productView = new ProductView();

        viewItems.add(appFrame.makeViewItem("Product", productView));
        viewItems.add(appFrame.makeViewItem("Developer", developerView));
        viewItems.add(appFrame.makeViewItem("Branch", branchView));

        try {
            Map<String, IDeveloper> idDeveloperMap = new HashMap<>();

            List<IDeveloper> developers = GameStoreDB.getDeveloper();
            developerView.setData(new Vector<>(developers));
            for (IDeveloper developer : developers) {
                idDeveloperMap.put(developer.getId(), developer);
            }

            List<IBranch> branches = GameStoreDB.getBranch();
            branchView.setData(new Vector<>(branches));

            List<IProduct> products = GameStoreDB.getProduct(idDeveloperMap);
            productView.setData(new Vector<>(products));

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
