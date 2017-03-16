import java.lang.System;
import java.sql.DriverManager;
import java.sql.SQLException;

import oracle.jdbc.driver.OracleDriver;
import ui.AppFrame;

public class Main {
    public static void main(String[] args) {
        try {
            DriverManager.registerDriver(new OracleDriver());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        AppFrame frame = new AppFrame();
        frame.setDefaultCloseOperation(AppFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}