package ui.dialog;

import ui.field.DateTextField;

import java.awt.*;
import java.util.Date;

public class SalesReportDialog extends CheckedInputDialog<SalesReportDialog.DateInterval> {
    public interface DateInterval {
        Date getStartDate();
        Date getEndDate();
    }

    private DateInterval interval = new DateInterval() {
        @Override
        public Date getStartDate() {
            return startDateField.getInputValue();
        }

        @Override
        public Date getEndDate() {
            return endDateField.getInputValue();
        }
    };

    private DateTextField startDateField;
    private DateTextField endDateField;

    public SalesReportDialog(Frame owner) {
        super(owner);
        this.setTitle("Generate Sales Report...");

        this.startDateField = new DateTextField();
        this.endDateField = new DateTextField();

        CheckedInputComponent[] inputComponents = {
                makeCheckedInputComponent("From:", startDateField),
                makeCheckedInputComponent("To:", endDateField),
        };

        this.setInputComponents(inputComponents);

        this.startDateField.getDocument().addDocumentListener(defaultDocumentListener);
        this.endDateField.getDocument().addDocumentListener(defaultDocumentListener);
    }

    public DateInterval getInputValue() {
        return interval;
    }
}
