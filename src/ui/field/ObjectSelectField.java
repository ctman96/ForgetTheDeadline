package ui.field;

import ui.util.CheckedInput;

import javax.swing.*;
import java.util.Vector;

public class ObjectSelectField<InputT> extends JComboBox<InputT> implements CheckedInput<InputT> {
    private Vector<InputT> options;

    public ObjectSelectField(Vector<InputT> options) {
        super(options);
    }

    @Override
    public boolean isInputValid() {
        return getInputValue() != null;
    }

    @Override
    public InputT getInputValue() {
        return (InputT) this.getSelectedItem();
    }
}
