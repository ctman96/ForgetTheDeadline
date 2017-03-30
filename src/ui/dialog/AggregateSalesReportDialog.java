package ui.dialog;

import sql.GameStoreDB.Aggregate;
import ui.field.DateTextField;
import ui.field.ObjectSelectField;

import java.awt.*;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

public class AggregateSalesReportDialog extends CheckedInputDialog<AggregateSalesReportDialog.AggregatedSaleReportInput> {
    public interface AggregatedSaleReportInput {
        Date getStartDate();
        Date getEndDate();
        Aggregate getAggregate();
    }

    private AggregatedSaleReportInput input = new AggregatedSaleReportInput() {
        @Override
        public Date getStartDate() {
            return startDateField.getInputValue();
        }

        @Override
        public Date getEndDate() {
            return endDateField.getInputValue();
        }

        @Override
        public Aggregate getAggregate() {
            return aggregateField.getInputValue();
        }
    };

    private DateTextField startDateField;
    private DateTextField endDateField;
    private ObjectSelectField<Aggregate> aggregateField;

    public AggregateSalesReportDialog(Frame owner) {
        super(owner);
        this.setTitle("Generate Sales Report...");

        this.startDateField = new DateTextField();
        this.endDateField = new DateTextField();
        this.aggregateField = new ObjectSelectField<>(new Vector<>(Arrays.asList(Aggregate.values())));

        CheckedInputComponent[] inputComponents = {
                makeCheckedInputComponent("From:", startDateField),
                makeCheckedInputComponent("To:", endDateField),
                makeCheckedInputComponent("Aggregate:", aggregateField),
        };

        this.setInputComponents(inputComponents);

        this.startDateField.getDocument().addDocumentListener(defaultDocumentListener);
        this.endDateField.getDocument().addDocumentListener(defaultDocumentListener);
        this.aggregateField.addActionListener(defaultActionListener);
    }

    public AggregatedSaleReportInput getInputValue() {
        return input;
    }
}
