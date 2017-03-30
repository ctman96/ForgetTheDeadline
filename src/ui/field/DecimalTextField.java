package ui.field;

import ui.util.CheckedInput;

import javax.swing.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class DecimalTextField extends JFormattedTextField implements CheckedInput<BigDecimal> {
    static final DecimalFormat defaultFormat = new DecimalFormat();

    static {
        defaultFormat.setParseBigDecimal(true);
    }

    public DecimalTextField() {
        super(defaultFormat);
    }

    public DecimalTextField(DecimalFormat format) {
        super(format);
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
    public BigDecimal getInputValue() throws NumberFormatException {
        return new BigDecimal(this.getText());
    }
}
