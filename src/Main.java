import sql.GameStoreDB;
import ui.AppFrameController;

import java.sql.SQLException;


public class Main {
    public static void main(String[] args) {

        try {
            GameStoreDB.setupDriver();
//            GameStoreDB.withConnection(connection ->  {
//                GameStoreDB.createDatabase(connection);
//                GameStoreDB.populateDatabase(connection);
//            });
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        new AppFrameController().setVisible(true);
    }
}