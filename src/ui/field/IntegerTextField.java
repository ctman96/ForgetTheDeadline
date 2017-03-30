package ui.field;

import ui.util.CheckedInput;

import javax.swing.*;
import java.text.DecimalFormat;

public class IntegerTextField extends JFormattedTextField implements CheckedInput<Integer> {
    static final DecimalFormat defaultFormat = new DecimalFormat();

    static {
        defaultFormat.setMaximumFractionDigits(0);
    }

    public IntegerTextField() {
        super(defaultFormat);
    }

    public IntegerTextField(int digits) {
        super(makeFormat(digits));
    }

    @Override
    public boolean isInputValid() {
        try {
            this.getInputValue();
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public Integer getInputValue() throws NumberFormatException {
        return Integer.valueOf(this.getText());
    }

    private static DecimalFormat makeFormat(int digits) {
        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(0);
        format.setMaximumIntegerDigits(digits);
        return format;
    }
}
