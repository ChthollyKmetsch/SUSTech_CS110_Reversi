package GUI;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;

public class TempCallFrame extends JFrame {
    public boolean playWithAI;

    public TempCallFrame(int firstOperator, int difficulty) {
        setSize(400,80);
        setTitle("Choose game mode");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout());

        JButton spButton = new JButton("Multi Player");
        spButton.addActionListener( e -> {
            playWithAI = false;
            SoundPlayer.playSound("start_game.wav");
            dispose();
            ReversiBoardFrame frame = new ReversiBoardFrame(firstOperator, playWithAI, difficulty);
            frame.setVisible(true);
        });

        JButton mpButton = new JButton("Single Player");
        mpButton.addActionListener( e -> {
            playWithAI = true;
            SoundPlayer.playSound("start_game.wav");
            dispose();
            ReversiBoardFrame frame = new ReversiBoardFrame(firstOperator, playWithAI, difficulty);
            frame.setVisible(true);
        });

        buttonPanel.add(spButton);
        buttonPanel.add(mpButton);

        add(buttonPanel);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
