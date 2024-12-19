package GUI.MenuComponents;

import Algorithms.Algo;
import GUI.ReversiBoard;
import GUI.ReversiBoardFrame;
import GUI.SoundPlayer;

import javax.swing.*;

public class WithdrawButton extends JButton {
    public WithdrawButton(Algo app, ReversiBoard board) {
        super("悔棋");
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
        this.setSize(20,20);
        addActionListener(e -> {
            int tmpOperator = app.loadFromStack();
            if (tmpOperator == 0) {
                JOptionPane.showMessageDialog(board, "无法悔棋！", "错误", JOptionPane.ERROR_MESSAGE);
            } else {
                SoundPlayer.playSound("click_default.wav");
                board.currentOperator = tmpOperator;
                board.updateBoardWith(app);
                board.repaint();
            }
        });
    }
}
