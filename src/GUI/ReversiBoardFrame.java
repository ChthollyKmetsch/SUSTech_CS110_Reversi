package GUI;

import GUI.MenuComponents.*;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static Utility.Constants.*;

public class ReversiBoardFrame extends JFrame {
    public ReversiBoardFrame(int firstOperator, boolean playWithAI, int difficulty) {
        setSize(BOARD_SIZE*TILE_SIZE+10,BOARD_SIZE*TILE_SIZE+MENUBAR_HEIGHT+40);
        setTitle("黑白棋");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ReversiBoard board = new ReversiBoard(firstOperator, playWithAI, difficulty);
        board.currentOperator = firstOperator;
        board.setVisible(true);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setPreferredSize(new Dimension(this.getWidth(),MENUBAR_HEIGHT));
        menuBar.setBackground(Color.WHITE);
//        menuBar.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));

        // 三个任务栏主要元素
        JMenu fileMenu = new fileMenu(this, board.getApp(), board);
        WithdrawButton withdrawButton = new WithdrawButton(board.getApp(), board);
        ExitButton exitButton = new ExitButton();

        // 实现左右按钮分治，由于莫名其妙的bug导致左面板不能支持 JMenu，于是只有右面板
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.add(exitButton);

        menuBar.add(fileMenu);
        menuBar.add(withdrawButton);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(rightPanel);

        this.add(board);
        this.setJMenuBar(menuBar);
        this.setVisible(true);
        SoundPlayer.playRandomMusic("/sound/music/");
    }
}
