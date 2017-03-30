package ui.field;

import ui.util.CheckedInput;

import javax.swing.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTextField extends JFormattedTextField implements CheckedInput<Date> {
    private static final DateFormat defaultDateFormat = new SimpleDateFormat("yyyy/MM/dd");

    public DateTextField() {
        super(defaultDateFormat);
        this.setText(defaultDateFormat.format(Calendar.getInstance().getTime()));
    }

    @Override
    public boolean isInputValid() {
        return this.getInputValue() != null;
    }

    @Override
    public Date getInputValue() {
        try {
            return defaultDateFormat.parse(this.getText());
        } catch (ParseException e) {
            // Dropdown
        }
        return null;
    }
}
