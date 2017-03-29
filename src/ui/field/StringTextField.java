package ui.field;

import ui.util.CheckedInput;

import javax.swing.*;

public class StringTextField extends JTextField implements CheckedInput<String> {
    @Override
    public boolean isInputValid() {
        return !this.getText().isEmpty();
    }

    @Override
    public String getInputValue() {
        return this.getText();
    }
}
