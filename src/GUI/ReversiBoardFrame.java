package GUI;

import GUI.MenuComponents.*;
import javax.swing.*;
import java.awt.*;

public class ReversiBoardFrame extends JFrame {
    private static final int DELAY_TIME = 1000;
    private static final int MENUBAR_HEIGHT = 30;
    private static final int INSET = 30;
    private static final int BOARD_SIZE = 8;  // 棋盘的大小
    private static final int TILE_SIZE = 80;  // 每个格子的大小

    public ReversiBoardFrame() {
        setSize(BOARD_SIZE*TILE_SIZE+10,BOARD_SIZE*TILE_SIZE+MENUBAR_HEIGHT+40);
        setTitle("黑白棋");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setPreferredSize(new Dimension(this.getWidth(),MENUBAR_HEIGHT));
        menuBar.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));

        JMenu fileMenu = new fileMenu("文件");
        WithdrawButton withdrawButton = new WithdrawButton("悔棋");

        menuBar.add(fileMenu);
        menuBar.add(withdrawButton);
        this.setJMenuBar(menuBar);
    }
}
