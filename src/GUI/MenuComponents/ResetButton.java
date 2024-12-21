package GUI.MenuComponents;

import Algorithms.*;
import GUI.GeneralStatePanel;
import GUI.ReversiBoard;
import GUI.SoundPlayer;

import static Utility.Constants.*;

import javax.swing.*;

public class ResetButton extends JButton {
    public ResetButton(Algo app, ReversiBoard board, GeneralStatePanel displayPanel) {
        super("重置棋局");
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
        addActionListener(e -> {
            SoundPlayer.playSound("click_default.wav");
            Saving saving = new Saving(INITIALBOARD, 1);
            app.loadFromSaving(saving);
            app.updateChessStatus();
            app.historicalBoards.clear();

            board.updateBoardWith(app);
            board.currentOperator = 1;
            board.repaintWithYellowRing();

            displayPanel.setBlackCount(app.numOfBlack);
            displayPanel.setWhiteCount(app.numOfWhite);
            displayPanel.setTotCount(app.getTotPieces());
            displayPanel.setCurrentOperator(1);
            displayPanel.totMinutes = 0;
            displayPanel.totSeconds = 0;
            displayPanel.paintImmediately(DISPLAY_PANEL_RECTANGLE);

            JOptionPane.showMessageDialog(this, "棋盘已重置", "重置棋局", JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
