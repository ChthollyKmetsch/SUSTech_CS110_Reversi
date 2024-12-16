import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ReversiBoard extends JFrame {
    private static final int DELAY_TIME = 1000;
    private static final int BOARD_SIZE = 8;  // 棋盘的大小
    private static final int TILE_SIZE = 80;  // 每个格子的大小
    private int[][] board = new int[BOARD_SIZE][BOARD_SIZE];  // 棋盘状态，0为空，1为黑棋，2为白棋

    private Image blackPieceImage = new ImageIcon(getClass().getResource("/GUI/blackPieceImage.png")).getImage();
    private Image whitePieceImage = new ImageIcon(getClass().getResource("/GUI/whitePieceImage.png")).getImage();
    private Image yellowRingImage = new ImageIcon(getClass().getResource("/GUI/yellowRingImage.png")).getImage();
    private Image RobotImage = new ImageIcon(getClass().getResource("/GUI/RobotImage.png")).getImage();

    int currentOperator = 1; // 1 is black, 2 is white
    boolean playWithAI;
    Pair aiChoice;
    int getx, gety;

    private Algo app = new Algo();

    public ReversiBoard(int firstOperator, boolean ai, int searchDepth) {
        playWithAI = ai;
        currentOperator = firstOperator;
        setTitle("黑白棋");
        Insets insets = getInsets();
        int windowWidth = BOARD_SIZE * (TILE_SIZE+2) + insets.left + insets.right;
        int windowHeight = BOARD_SIZE * (TILE_SIZE+5) + insets.top + insets.bottom;

        setSize(windowWidth, windowHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeBoard();
        app.findValidPlace(currentOperator);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Insets insets = getInsets();
                int x = e.getX() - insets.left;  // 减去左边距
                int y = e.getY() - insets.top;   // 减去顶部边距

                int col = x / TILE_SIZE;
                int row = y / TILE_SIZE;

                if (!playWithAI) { // multiplayer mode
                    if (col >= 0 && col < BOARD_SIZE &&
                            row >= 0 && row < BOARD_SIZE &&
                            board[row][col] == 0 && app.isNextMoveValid(row, col)) { // Verify blank and in-range
                        app.placeChess(row, col, currentOperator);
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
                        app.placeChess(row, col, currentOperator);
                        for (int i = 0; i < 8; ++i) { // Update the chess board
                            for (int j = 0; j < 8; ++j) {
                                board[i][j] = app.map[i][j];
                            }
                        }
                        SwingUtilities.invokeLater(() -> {
                            repaint(); // 更新下黑棋之后的棋盘状态，但不更新AI下一步的提示
                        });
                        currentOperator = currentOperator == 1 ? 2 : 1; // reverse the operator
                        SwingUtilities.invokeLater(() -> {
                            // Next AI's move
                            AI ai1 = new AI(app, searchDepth);
                            Pair pos = ai1.search(currentOperator,1);
                            getx = pos.getFt();
                            gety = pos.getSc();
                            ai1.findValidPlace(currentOperator);
                            app.validMoves = new ArrayList<>(ai1.validMoves);
                            app.placeChess(getx,gety, currentOperator);
                            SwingUtilities.invokeLater(() -> {
                                Timer timer = new Timer(DELAY_TIME, new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e1) {
                                        System.out.println("x = " + getx);
                                        System.out.println("y = " + gety);
                                        repaint();
                                    }
                                });
                                timer.setRepeats(false);
                                timer.start();
                                for (int i = 0; i < 8; ++i) { // Update the chess board
                                    for (int j = 0; j < 8; ++j) {
                                        board[i][j] = app.map[i][j];
                                    }
                                }
                                currentOperator = currentOperator == 1 ? 2 : 1;
                                app.clearPlayerOptions();
                            });
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
    public void paint(Graphics g) {
        super.paint(g);

        // 获取窗口的边距（用于确保棋盘绘制不会被标题栏挡住）
        Insets insets = getInsets();
        int xOffset = insets.left;
        int yOffset = insets.top;

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
                g.drawImage(yellowRingImage, xOffset + i.y * TILE_SIZE + 5, xOffset + i.x * TILE_SIZE + 30, TILE_SIZE - 10, TILE_SIZE - 10, this);
            }
        } else {
            g.drawImage(RobotImage, xOffset + gety * TILE_SIZE + 5, xOffset + getx * TILE_SIZE + 30, TILE_SIZE - 10, TILE_SIZE - 10, this);
        }
//        app.clearPlayerOptions();
    }


}
