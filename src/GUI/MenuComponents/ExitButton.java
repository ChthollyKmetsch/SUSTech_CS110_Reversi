package GUI.MenuComponents;

import GUI.SoundPlayer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExitButton extends JButton {
    public ExitButton() {
        super("返回主菜单");
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
        addActionListener(e -> {
            SoundPlayer.playSound("click_close.wav");
            System.exit(0);
        });
    }
}
