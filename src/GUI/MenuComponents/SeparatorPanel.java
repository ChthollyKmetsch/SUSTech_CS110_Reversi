package GUI.MenuComponents;

import javax.swing.*;
import java.awt.*;

public class SeparatorPanel extends JPanel {
    public SeparatorPanel(int width, Color color) {
        setPreferredSize(new Dimension(width,35));
        setBackground(color);
    }
}
