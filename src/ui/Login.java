package ui;
import javax.swing.JOptionPane;
/**
 * Created by cody on 30/03/17.
 */
public class Login {
    public void main() {
        String[] choices = {"Customer", "Employee", "Manager", "Admin"};
        int input = toVal((String) JOptionPane.showInputDialog(null, "Login...",
                "FTD Games Login", JOptionPane.QUESTION_MESSAGE,null,choices,choices[1]));
        new AppFrameController(input).setVisible(true);
    }
    private int toVal(String s){
        if(s.equals("Customer")) return 0;
        if(s.equals("Employee")) return 1;
        if(s.equals("Manager")) return 2;
        if(s.equals("Admin")) return 3;
        else return 0;
    }
}
