package ui.util;

public interface CheckedInput<InputT> {
    public boolean isInputValid();
    public InputT getInputValue();
}
