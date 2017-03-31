package ui;

import sql.GameStoreDB;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by cody on 30/03/17.
 */
public class Login {
    public void main() {

        String[] choices = {"Customer", "Employee", "Manager", "Admin"};
        JPanel outerPanel = new JPanel();
        outerPanel.setLayout(new GridLayout(0,1));

        try {
            JPanel imgPanel = new JPanel();
            BufferedImage logo = ImageIO.read(new File("resource/img/logo.png"));
            JLabel picLabel = new JLabel(new ImageIcon(logo));
            //imgPanel.add(picLabel);
            outerPanel.add(picLabel);
        }catch (IOException e){
            System.out.println(e);
        }

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
        loginPanel.add(Box.createHorizontalStrut(15));

        userSelect.setSelectedIndex(3);
        nameField.setText("ora_UNIXID");
        passField.setText("aSTUDENTNUM");

        outerPanel.add(loginPanel);

        int result = JOptionPane.showConfirmDialog(null, outerPanel,
                "FTD Games Login", JOptionPane.OK_CANCEL_OPTION);
        if(result == JOptionPane.OK_OPTION){
            String userLevel = (String) userSelect.getSelectedItem();
            String user = nameField.getText();
            String pass = passField.getText();
            try{
                System.out.println("Test Conection...");
                GameStoreDB.withConnection(user,pass,(con)->{});
                System.out.println("Test Success");
                new AppFrameController(toVal(userLevel),user, pass).setVisible(true);
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, e + "\n\nPlease check your " +
                        "login credentials, and ensure you have created an ssh tunnel created \n" +
                        "   ssh -l UNIXID -L localhost:1522:dbhost.ugrad.cs.ubc.ca:1522 remote.ugrad.cs.ubc.ca", "Oops...", JOptionPane.ERROR_MESSAGE);
            }
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
