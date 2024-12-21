package GUI.MenuComponents;

import Algorithms.Algo;
import GUI.GeneralStatePanel;
import GUI.ReversiBoard;
import GUI.SoundPlayer;
import static Utility.Constants.*;

import javax.swing.*;

public class WithdrawalButton extends JButton {
    public WithdrawalButton(Algo app, ReversiBoard board, GeneralStatePanel displayPanel) {
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
                board.repaintWithYellowRing();
                displayPanel.setBlackCount(app.numOfBlack);
                displayPanel.setWhiteCount(app.numOfWhite);
                displayPanel.setTotCount(app.getTotPieces());
                displayPanel.paintImmediately(DISPLAY_PANEL_RECTANGLE);
            }
        });
    }
}
