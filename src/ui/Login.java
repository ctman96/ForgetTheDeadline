package ui;

import sql.GameStoreDB;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * Created by cody on 30/03/17.
 */
public class Login {
    public void main() {
        String[] choices = {"Customer", "Employee", "Manager", "Admin"};
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(0,1));
        JTextField nameField = new JTextField();
        JTextField passField = new JTextField();
        loginPanel.add(new JLabel("Connection login"));
        loginPanel.add(new JLabel("Username: "));
        loginPanel.add(nameField);
        loginPanel.add(new JLabel("Password: "));
        loginPanel.add(passField);
        loginPanel.add(Box.createHorizontalStrut(15));
        JComboBox userSelect = new JComboBox(choices);
        loginPanel.add(new JLabel("User Level"));
        loginPanel.add(userSelect);

        int result = JOptionPane.showConfirmDialog(null, loginPanel,
                "FTD Games Login", JOptionPane.OK_CANCEL_OPTION);
        if(result == JOptionPane.OK_OPTION){
            //TODO: Something with nameField.getText() and passField.getText()
            String user = (String) userSelect.getSelectedItem();
            new AppFrameController(toVal(user)).setVisible(true);
        }
    }
    private int toVal(String s){
        if(s.equals("Customer")) return 0;
        if(s.equals("Employee")) return 1;
        if(s.equals("Manager")) return 2;
        if(s.equals("Admin")) return 3;
        else return 0;
    }
}
