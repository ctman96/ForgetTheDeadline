package ui.field;

import ui.util.CheckedInput;

import javax.swing.*;
import java.util.Date;

public class DateTextField extends JTextField implements CheckedInput<Date> {
    

    @Override
    public boolean isInputValid() {
        return false;
    }

    @Override
    public Date getInputValue() {
        return null;
    }
}
