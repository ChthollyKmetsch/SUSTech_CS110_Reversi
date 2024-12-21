package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import Algorithms.*;
import static Utility.Constants.*;

public class ReversiBoard extends JPanel {

    public int currentOperator; // 1 is black, 2 is white
    public int AIOperator;
    public int humanOperator;
    boolean playWithAI;
    int getx, gety;

    boolean isPaintYellowRing = true;
    boolean isPaintRobot = false;

    private int[][] board = new int[BOARD_SIZE][BOARD_SIZE];  // 棋盘状态，0为空，1为黑棋，2为白棋
    private Image blackPieceImage = new ImageIcon(getClass().getResource("/GUI/img/blackPieceImage.png")).getImage();
    private Image whitePieceImage = new ImageIcon(getClass().getResource("/GUI/img/whitePieceImage.png")).getImage();
    private Image yellowRingImage = new ImageIcon(getClass().getResource("/GUI/img/yellowRingImage.png")).getImage();
    private Image RobotImage = new ImageIcon(getClass().getResource("/GUI/img/RobotImage.png")).getImage();
    private Image backgroundImage = new ImageIcon(getClass().getResource("/GUI/img/war.png")).getImage();
    private Algo app = new Algo();

    public ReversiBoard(int firstOperator, boolean ai, int searchDepth) {
        playWithAI = ai;
        currentOperator = firstOperator;
        AIOperator = firstOperator == 1 ? 2 : 1;
        humanOperator = AIOperator == 1 ? 2 : 1;

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
                        SoundPlayer.playSound("click_mp.wav");

                        updateBoardWith(app);
                        currentOperator = currentOperator == 1 ? 2 : 1; // Change current player
                        repaintWithYellowRing();
                        if (app.validMoves.isEmpty()) { // Next player can't move
                            currentOperator = currentOperator == 1 ? 2 : 1; // Back to this player
                            app.findValidPlace(currentOperator);
                            if (app.validMoves.isEmpty()) { // Another player can't either move. Game end.
                                int winner = app.getWinner();
                                int tot = app.getTotPieces();
                                repaintWithYellowRing();
                                SoundPlayer.playSound("game_over.wav");
                                if (winner == 0 && tot != 64) {
                                    JOptionPane.showMessageDialog(ReversiBoard.this, "Neither Can't move. Draw!");
                                } else if (winner == 1 && tot != 64) {
                                    JOptionPane.showMessageDialog(ReversiBoard.this, "Neither Can't move. Black wins!");
                                } else if (winner == 2 && tot != 64) {
                                    JOptionPane.showMessageDialog(ReversiBoard.this, "Neither Can't move. White wins!");
                                } else if (winner == 0) {
                                    JOptionPane.showMessageDialog(ReversiBoard.this, "Draw");
                                } else if (winner == 1) {
                                    JOptionPane.showMessageDialog(ReversiBoard.this, "Black wins!");
                                } else if (winner == 2) {
                                    JOptionPane.showMessageDialog(ReversiBoard.this, "White wins!");
                                }
                                try { // Read a new game
                                    Saving tmp = new Saving(INITIALBOARD, humanOperator);
                                    app.loadFromSaving(tmp);
                                    app.historicalBoards.clear();
                                    updateBoardWith(app);
                                    repaintWithYellowRing();
                                } catch (Exception ex) {
                                    throw new RuntimeException(ex);
                                }
                            } else {
                                JOptionPane.showMessageDialog(ReversiBoard.this, "Next player can't move, back to this player!");
                                repaintWithYellowRing();
                            }
                        }
                    }
                } else { // Play-with-AI mode
                    if (currentOperator == humanOperator &&
                            col >= 0 && col < BOARD_SIZE &&
                            row >= 0 && row < BOARD_SIZE &&
                            board[row][col] == 0 && app.isNextMoveValid(row, col)) { // Verify blank and in-range and player's move
                        app.placeChess(row, col, humanOperator, false);
                        SoundPlayer.playRandomSound("/sound/place_chess/"); // play sound

                        currentOperator = humanOperator;
                        // Next AI's move
                        AI ai1 = new AI(app, searchDepth);
                        Pair pos = ai1.search(AIOperator, 1, -AI.INF, AI.INF);

                        getx = pos.getFt();
                        gety = pos.getSc();

                        updateBoardWith(app);
                        repaintWithRobot();
                        app.placeChess(getx, gety, AIOperator, true);
                        app.clearPlayerOptions();

                        app.findValidPlace(humanOperator); // Predict next player's move
                        int loopTime = 0;

                        while (app.validMoves.isEmpty()) { // Player can't move
                            AI ai2 = new AI(app, searchDepth);
                            ai2.findValidPlace(AIOperator);
                            if (ai2.validMoves.isEmpty()) { // AI can't move, too. Game end.
                                int winner = app.getWinner();
                                int tot = app.getTotPieces();
                                repaintWithYellowRing();
                                SoundPlayer.playSound("game_over.wav");
                                if (winner == 0 && tot != 64) {
                                    JOptionPane.showMessageDialog(ReversiBoard.this, "Neither Can't move. Draw!");
                                } else if (winner == 1 && tot != 64) {
                                    JOptionPane.showMessageDialog(ReversiBoard.this, "Neither Can't move. Black wins!");
                                } else if (winner == 2 && tot != 64) {
                                    JOptionPane.showMessageDialog(ReversiBoard.this, "Neither Can't move. White wins!");
                                } else if (winner == 0) {
                                    JOptionPane.showMessageDialog(ReversiBoard.this, "Draw");
                                } else if (winner == 1) {
                                    JOptionPane.showMessageDialog(ReversiBoard.this, "Black wins!");
                                } else if (winner == 2) {
                                    JOptionPane.showMessageDialog(ReversiBoard.this, "White wins!");
                                }
                                try { // Read a new game
                                    Saving tmp = new Saving(INITIALBOARD, humanOperator);
                                    app.loadFromSaving(tmp);
                                    app.historicalBoards.clear();
                                    updateBoardWith(app);
                                    repaintWithYellowRing();
                                } catch (Exception ex) {
                                    throw new RuntimeException(ex);
                                }

                            } else { // But AI can move, repeat AI's search
                                pos = ai2.search(AIOperator, 1, -AI.INF, AI.INF);
                                getx = pos.getFt();
                                gety = pos.getSc(); // 获取 AI 行动提示标志坐标，以便 repaint

                                repaintWithRobot();
                                app.placeChess(getx, gety, AIOperator, true);
                                repaintWithYellowRing(DELAY_TIME);
                            }
                            app.validMoves.clear();
                            app.findValidPlace(humanOperator);
                        }
//                        app.clearPlayerOptions();
                        repaintWithYellowRing(DELAY_TIME);
                    }
                    currentOperator = humanOperator;
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
//        g.fillRect(xOffset, yOffset, BOARD_SIZE * TILE_SIZE, BOARD_SIZE * TILE_SIZE);  // 填充背景
        g.drawImage(backgroundImage, 0, 0, WINDOWS_WIDTH, WINDOWS_HEIGHT, this);

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
                    g.drawImage(blackPieceImage, xOffset + col * TILE_SIZE + 5, yOffset + row * TILE_SIZE + 5, TILE_SIZE - 10, TILE_SIZE - 10, this);
                } else if (board[row][col] == 2) {
                    // 绘制白棋
                    g.drawImage(whitePieceImage, xOffset + col * TILE_SIZE + 5, yOffset + row * TILE_SIZE + 5, TILE_SIZE - 10, TILE_SIZE - 10, this);
                }
            }
        }

        // Paint visual hints for valid moves
        app.validMoves.clear();
        app.findValidPlace(currentOperator);
        if (isPaintYellowRing) {
            for (ValidMoves i : app.validMoves) {
                g.drawImage(yellowRingImage, xOffset + i.y * TILE_SIZE + 5, xOffset + i.x * TILE_SIZE + 5, TILE_SIZE - 10, TILE_SIZE - 10, this);
            }
        }
        if (isPaintRobot){
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

    public int antiAIOperator() {
        if (AIOperator == 1) return 2;
        else return 1;
    }

    public void repaintWithRobot() {
        isPaintRobot = true;
        isPaintYellowRing = false;
        paintImmediately(FULL_SCREEN_RECTANGLE); // Update the robot icon
        isPaintRobot = false;
        isPaintYellowRing = true;
    }

    public void repaintWithRobot(int delayTime) {
        isPaintRobot = true;
        isPaintYellowRing = false;
        Timer timer1 = new Timer(delayTime, e1 -> {
            paintImmediately(FULL_SCREEN_RECTANGLE); // Update the robot icon
            isPaintRobot = false;
            isPaintYellowRing = true;
        });
        timer1.setRepeats(false);
        timer1.start();
    }

    public void repaintWithYellowRing() {
        isPaintYellowRing = true;
        isPaintRobot = false;
        updateBoardWith(app);
        paintImmediately(FULL_SCREEN_RECTANGLE);
        isPaintYellowRing = false;
        isPaintRobot = false;
    }

    public void repaintWithYellowRing(int delayTime) {
        isPaintYellowRing = true;
        isPaintRobot = false;

        try {
            Thread.sleep(delayTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        Timer timer1 = new Timer(delayTime, e1 -> {
            updateBoardWith(app);
            paintImmediately(FULL_SCREEN_RECTANGLE);
            isPaintYellowRing = false;
            isPaintRobot = false;
//        });
//        timer1.setRepeats(false);
//        timer1.start();
    }
}
