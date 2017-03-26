package ui.dialog;

import ui.util.CheckedInput;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public abstract class CheckedInputDialog<T> extends JDialog implements CheckedInput<T> {
    public interface CheckedInputComponent {
        String getLabel();
        CheckedInput getInput();
        Component getComponent();
    }

    protected static final DecimalFormat defaultPriceFormat = new DecimalFormat("#0.00");
    static {
        defaultPriceFormat.setMaximumFractionDigits(2);
        defaultPriceFormat.setParseBigDecimal(true);
    }

    protected final DocumentListener defaultDocumentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            onValueChange();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            onValueChange();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            onValueChange();
        }
    };

    protected final ActionListener defaultActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            onValueChange();
        }
    };

    CheckedInput[] inputs;

    private JButton okButton;
    private JButton cancelButton;
    private boolean ok = false;

    public CheckedInputDialog(Frame owner, CheckedInputComponent[] inputComponents) {
        super(owner);

        int inputSize = inputComponents.length;

        JLabel[] labels = new JLabel[inputSize];
        for (int i = 0; i < inputSize; i++) {
            labels[i] = new JLabel(inputComponents[i].getLabel());
        }

        Component[] components = new Component[inputSize];
        for (int i = 0; i < inputSize; i++) {
            components[i] = inputComponents[i].getComponent();
        }

        this.inputs = new CheckedInput[inputSize];
        for (int i = 0; i < inputSize; i++) {
            inputs[i] = inputComponents[i].getInput();
        }


        this.okButton = new JButton("OK");
        this.okButton.addActionListener((e) -> {
            this.ok = true;
            dispose();
        });
        this.okButton.setEnabled(false);

        this.cancelButton = new JButton("Cancel");
        this.cancelButton.addActionListener((e) -> {
            dispose();
        });

        Container contentPane = this.getContentPane();
        GroupLayout layout = new GroupLayout(contentPane);
        contentPane.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        { // Horizontal group
            GroupLayout.ParallelGroup labelGroup = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);
            for (JLabel label : labels) {
                labelGroup = labelGroup.addComponent(label);
            }

            GroupLayout.ParallelGroup componentGroup = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
            for (Component component : components) {
                componentGroup = componentGroup.addComponent(component);
            }

            layout.setHorizontalGroup(
                    layout.createParallelGroup()
                            .addGroup(layout.createSequentialGroup()
                                    .addGroup(labelGroup)
                                    .addGroup(componentGroup))
                            .addGroup(layout.createSequentialGroup()
                                    .addComponent(okButton)
                                    .addComponent(cancelButton))
            );
        }

        { // Vertical group
            GroupLayout.ParallelGroup[] inputGroups = new GroupLayout.ParallelGroup[inputSize];
            for (int i = 0; i < inputSize; i++) {
                inputGroups[i] = layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(labels[i])
                        .addComponent(components[i]);
            }

            GroupLayout.SequentialGroup verticalGroup = layout.createSequentialGroup();
            for (GroupLayout.ParallelGroup inputGroup : inputGroups) {
                verticalGroup.addGroup(inputGroup);
            }

            verticalGroup.addGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(okButton)
                        .addComponent(cancelButton)
            );
        }
    }

    protected void onValueChange() {
        for (CheckedInput input : inputs) {
            if (!input.isInputValid()) {
                this.enableOk(false);
                return;
            }
        }
        this.enableOk(true);
    }

    private void enableOk(boolean ok) {
        this.okButton.setEnabled(ok);
    }

    public boolean isInputValid() {
        return this.ok;
    }

    public static <C extends Component & CheckedInput> CheckedInputComponent makeCheckedInputComponent(String label, C inputComponent) {
        return new CheckedInputComponent() {
            @Override
            public String getLabel() {
                return label;
            }

            @Override
            public CheckedInput getInput() {
                return inputComponent;
            }

            @Override
            public Component getComponent() {
                return inputComponent;
            }
        };
    }
}
