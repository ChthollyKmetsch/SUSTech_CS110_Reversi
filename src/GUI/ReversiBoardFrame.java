package GUI;

import GUI.MenuComponents.*;

import javax.swing.*;
import java.awt.*;

import static Utility.Constants.*;

public class ReversiBoardFrame extends JFrame {
    public final boolean finalPlayWithAI;

    public ReversiBoardFrame(int firstOperator, boolean playWithAI, int difficulty) {
        finalPlayWithAI = playWithAI;
        setSize(BOARD_SIZE*TILE_SIZE+10+200,BOARD_SIZE*TILE_SIZE+MENUBAR_HEIGHT+40);
        setTitle("Othello");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 数据面板
        GeneralStatePanel generalStatePanel = new GeneralStatePanel();
        generalStatePanel.paintImmediately(DISPLAY_PANEL_RECTANGLE);
        Timer totalTimer = new Timer(DELAY_TIME-10, e -> { // 数据面板秒计时器，更新一局游戏时间
            ++generalStatePanel.totSeconds;
            generalStatePanel.totMinutes = generalStatePanel.totSeconds / 60;
            generalStatePanel.paintImmediately(DISPLAY_PANEL_RECTANGLE);
        });
        totalTimer.start();

        // 下棋面板
        ReversiBoard board = new ReversiBoard(firstOperator, playWithAI, difficulty, generalStatePanel);
        board.currentOperator = firstOperator;
        board.setVisible(true);

        // 任务栏
        JMenuBar menuBar = new JMenuBar();
        menuBar.setPreferredSize(new Dimension(this.getWidth(),MENUBAR_HEIGHT+5));
        menuBar.setBackground(Color.WHITE);
        menuBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        // 四个任务栏主要元素
        JMenu fileMenu = new fileMenu(this, board.app, board, generalStatePanel);
        WithdrawalButton withdrawalButton = new WithdrawalButton(board.getApp(), board, generalStatePanel);
        ExitButton exitButton = new ExitButton();
        ResetButton resetButton = new ResetButton(board.app, board, generalStatePanel);

        // 实现左右按钮分治，由于莫名其妙的bug导致左面板不能支持 JMenu，于是只有右面板
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.add(exitButton);

        menuBar.add(fileMenu);
        menuBar.add(new SeparatorPanel(1, Color.BLACK));
        menuBar.add(withdrawalButton);
        menuBar.add(new SeparatorPanel(1, Color.BLACK));
        menuBar.add(resetButton);
        menuBar.add(new SeparatorPanel(545, Color.WHITE));
//        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(rightPanel);

        this.add(board);
        this.add(generalStatePanel);
        this.setJMenuBar(menuBar);

        this.setLocationRelativeTo(null);
        this.setVisible(true);
        SoundPlayer.playRandomMusic("/sound/music/");
    }

}
