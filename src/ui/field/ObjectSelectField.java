package ui.field;

import ui.util.CheckedInput;

import javax.swing.*;

public class ObjectSelectField<InputT> extends JComboBox<InputT> implements CheckedInput<InputT> {
    private InputT[] options;

    public ObjectSelectField(InputT[] options) {
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
