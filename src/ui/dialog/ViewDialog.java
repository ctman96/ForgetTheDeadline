package ui.dialog;

import javax.swing.*;
import java.awt.*;

public class ViewDialog extends JDialog {
    public ViewDialog(Frame owner, Component view) {
        super(owner);
        this.add(view);
    }
}
