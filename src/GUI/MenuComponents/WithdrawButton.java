package GUI.MenuComponents;

import javax.swing.*;
import java.awt.*;

public class WithdrawButton extends JButton {
    public WithdrawButton(String s) {
        super(s);
        this.setSize(20,20);
        addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "withdrawal");
        });
    }
}
