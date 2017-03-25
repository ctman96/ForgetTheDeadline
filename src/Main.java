import java.sql.DriverManager;
import java.sql.SQLException;

import oracle.jdbc.driver.OracleDriver;
import ui.AppFrameController;

public class Main {
    public static void main(String[] args) {
        try {
            DriverManager.registerDriver(new OracleDriver());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        new AppFrameController().setVisible(true);
    }
}