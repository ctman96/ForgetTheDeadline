import sql.GameStoreDB;
import ui.AppFrameController;
import ui.Login;

import java.sql.SQLException;


public class Main {
    public static void main(String[] args) {

        try {
            GameStoreDB.setupDriver();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        new Login().main();
    }
}