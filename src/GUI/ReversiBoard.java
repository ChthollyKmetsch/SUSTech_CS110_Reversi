package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;

import Algorithms.*;

public class ReversiBoard extends JPanel {
    private static final int DELAY_TIME = 1000;
    private static final int MENUBAR_HEIGHT = 30;
    private static final int INSET = 30;
    private static final int BOARD_SIZE = 8;  // 棋盘的大小
    private static final int TILE_SIZE = 80;  // 每个格子的大小
    private int[][] board = new int[BOARD_SIZE][BOARD_SIZE];  // 棋盘状态，0为空，1为黑棋，2为白棋

    private Image blackPieceImage = new ImageIcon(getClass().getResource("/GUI/img/blackPieceImage.png")).getImage();
    private Image whitePieceImage = new ImageIcon(getClass().getResource("/GUI/img/whitePieceImage.png")).getImage();
    private Image yellowRingImage = new ImageIcon(getClass().getResource("/GUI/img/yellowRingImage.png")).getImage();
    private Image RobotImage = new ImageIcon(getClass().getResource("/GUI/img/RobotImage.png")).getImage();

    public int currentOperator = 1; // 1 is black, 2 is white
    boolean playWithAI;
    int getx, gety;

    private Algo app = new Algo();
    private java.util.Queue<Runnable> tasks = new LinkedList<>();

    public ReversiBoard(int firstOperator, boolean ai, int searchDepth) {
        playWithAI = ai;
        currentOperator = firstOperator;

        int windowWidth = BOARD_SIZE * TILE_SIZE;
        int windowHeight = BOARD_SIZE * TILE_SIZE + INSET + MENUBAR_HEIGHT;

        setSize(windowWidth, windowHeight);
        initializeBoard();
        app.findValidPlace(currentOperator);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                int col = x / TILE_SIZE;
                int row = y / TILE_SIZE;


                if (!playWithAI) { // multiplayer mode
                    if (col >= 0 && col < BOARD_SIZE &&
                            row >= 0 && row < BOARD_SIZE &&
                            board[row][col] == 0 && app.isNextMoveValid(row, col)) { // Verify blank and in-range
                        app.placeChess(row, col, currentOperator, false);
                        for (int i = 0; i < 8; ++i) { // Update the chess board
                            for (int j = 0; j < 8; ++j) {
                                board[i][j] = app.map[i][j];
                            }
                        }

                        repaint();
                        currentOperator = currentOperator == 1 ? 2 : 1;
                    }
                } else { // Play-with-AI mode
                    if (currentOperator == 1 &&
                            col >= 0 && col < BOARD_SIZE &&
                            row >= 0 && row < BOARD_SIZE &&
                            board[row][col] == 0 && app.isNextMoveValid(row, col)) { // Verify blank and in-range and player's move
                        app.placeChess(row, col, currentOperator, false);
                        updateBoardWith(app);
                        SwingUtilities.invokeLater(() -> {
                            repaint(); // 更新下黑棋之后的棋盘状态，且同时更新AI下一步的提示
                        });
                        currentOperator = currentOperator == 1 ? 2 : 1; // reverse the operator
                        // play sound
                        SoundPlayer.playRandomSound("/sound/place_chess/");

                        SwingUtilities.invokeLater(() -> {
                            // Next AI's move
                            AI ai1 = new AI(app, searchDepth);
                            Pair pos = ai1.search(currentOperator,1,-AI.INF,AI.INF);
                            getx = pos.getFt();
                            gety = pos.getSc();
                            ai1.findValidPlace(currentOperator);
                            app.validMoves = new ArrayList<>(ai1.validMoves);
                            app.placeChess(getx,gety, currentOperator, true);
//                            SwingUtilities.invokeLater(() -> repaint());
                            SwingUtilities.invokeLater(() -> { // 不要动它：我也不知道问什么这里为什么会 repaint 两次
                                Timer timer = new Timer(DELAY_TIME, e1 -> {
                                    repaint();
                                });
                                timer.setRepeats(false);
                                timer.start();
                                updateBoardWith(app);
                                currentOperator = currentOperator == 1 ? 2 : 1;
                                app.clearPlayerOptions();
                            });

                            /*
                            app.findValidPlace(currentOperator == 1 ? 2 : 1); // Predict next player's move
                            while (app.validMoves.isEmpty()) { // Player can't move
                                AI ai2 = new AI(app, searchDepth);
                                ai2.findValidPlace(currentOperator);
                                if (ai2.validMoves.isEmpty()) { // AI can't move, too. Game end.
                                    int winner = app.getWinner();
                                    if (winner == 0) {
                                        JOptionPane.showMessageDialog(ReversiBoard.this, "Draw");
                                    } else if (winner == 1) {
                                        JOptionPane.showMessageDialog(ReversiBoard.this, "Black wins!");
                                    } else {
                                        JOptionPane.showMessageDialog(ReversiBoard.this, "White wins!");
                                    }

                                    try {
                                        Saving tmp = new Saving(Algo.initialBoard, currentOperator);
                                        app.loadFromSaving(tmp);
                                        app.historicalBoards.clear();
                                        currentOperator = tmp.currentOperator;
                                        updateBoardWith(app);
                                        repaint();
                                    } catch (Exception ex) {
                                        throw new RuntimeException(ex);
                                    }

                                } else { // But AI can move, repeat AI's search
                                    AI ai3 = new AI(app, searchDepth);
                                    pos = ai1.search(currentOperator,1,-AI.INF,AI.INF);
                                    getx = pos.getFt();
                                    gety = pos.getSc();
                                    ai1.findValidPlace(currentOperator);
                                    app.validMoves = new ArrayList<>(ai1.validMoves);
                                    app.placeChess(getx,gety, currentOperator, true);
                                    SwingUtilities.invokeLater(() -> {
                                        Timer timer = new Timer(DELAY_TIME, e1 -> {
                                            repaint();
                                        });
                                        timer.setRepeats(false);
                                        timer.start();
                                        updateBoardWith(app);
                                        currentOperator = currentOperator == 1 ? 2 : 1;
                                        app.clearPlayerOptions();
                                    });
                                }
                                app.validMoves.clear();
                                app.findValidPlace(currentOperator == 1 ? 2 : 1);
                            }
                            app.validMoves.clear();
                            */

                        });
                    }
                }
            }
        });
    }

    // 初始化棋盘，设置初始的4个棋子
    private void initializeBoard() {
        // 初始时，棋盘中心放置4个棋子
        board[3][4] = 1;  // 黑棋
        board[3][3] = 2;  // 白棋
        board[4][4] = 2;  // 白棋
        board[4][3] = 1;  // 黑棋
        repaint();
    }

    // 绘制棋盘和棋子
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 获取窗口的边距（用于确保棋盘绘制不会被标题栏挡住）
        Insets insets = getInsets();
        int xOffset = 0;
        int yOffset = 0;
        // 设置棋盘背景颜色
        g.setColor(Color.LIGHT_GRAY);  // 设置背景颜色为浅灰色
        g.fillRect(xOffset, yOffset, BOARD_SIZE * TILE_SIZE, BOARD_SIZE * TILE_SIZE);  // 填充背景

        // 绘制棋盘的格子
        g.setColor(Color.BLACK);
        for (int i = 0; i <= BOARD_SIZE; i++) {
            g.drawLine(xOffset + i * TILE_SIZE, yOffset, xOffset + i * TILE_SIZE, yOffset + BOARD_SIZE * TILE_SIZE);  // 竖线
            g.drawLine(xOffset, yOffset + i * TILE_SIZE, xOffset + BOARD_SIZE * TILE_SIZE, yOffset + i * TILE_SIZE);  // 横线
        }

        // 绘制棋子
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col] == 1) {
                    // 绘制黑棋
                    g.drawImage(blackPieceImage, xOffset + col * TILE_SIZE +5, yOffset + row * TILE_SIZE + 5, TILE_SIZE - 10, TILE_SIZE - 10, this);
                } else if (board[row][col] == 2) {
                    // 绘制白棋
                    g.drawImage(whitePieceImage, xOffset + col * TILE_SIZE +5, yOffset + row * TILE_SIZE + 5, TILE_SIZE - 10, TILE_SIZE - 10, this);
                }
            }
        }

        // Paint visual hints for valid moves
        app.findValidPlace(currentOperator);
        if (!playWithAI || currentOperator == 1) {
            for (ValidMoves i : app.validMoves) {
                g.drawImage(yellowRingImage, xOffset + i.y * TILE_SIZE + 5, xOffset + i.x * TILE_SIZE + 5, TILE_SIZE - 10, TILE_SIZE - 10, this);
            }
        } else {
            g.drawImage(RobotImage, xOffset + gety * TILE_SIZE + 5, xOffset + getx * TILE_SIZE + 5, TILE_SIZE - 10, TILE_SIZE - 10, this);
        }
//        app.clearPlayerOptions();
    }
    public Algo getApp() {
        return app;
    }
    public void updateBoardWith(Algo algo) {
        for (int i = 0; i < 8; ++i) { // Update the chess board
            for (int j = 0; j < 8; ++j) {
                board[i][j] = algo.map[i][j];
            }
        }
    }

}