package Algorithms;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Algo {
    protected final int[] dx = {1,1,1,0,0,-1,-1,-1};
    protected final int[] dy = {0,1,-1,1,-1,1,-1,0};

    public static final int[][] initialBoard = {
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,2,1,0,0,0},
            {0,0,0,1,2,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
    };

    public int[][] map = new int[8][8];
    public ArrayList<ValidMoves> validMoves = new ArrayList<>();
    protected HashSet<ValidMoves> validMovesSet = new HashSet<>();

    public Stack<Saving> historicalBoards = new Stack<>();

    protected int numOfBlack = 2;
    protected int numOfWhite = 2;
    protected int totPieces = 4;

    public Algo() { // Initialize the board
        this.map[3][3] = 2;
        this.map[4][4] = 2;
        this.map[3][4] = 1;
        this.map[4][3] = 1;
    } // 1 is black, 2 is white

    public void findValidPlace(int currentOperator) {
        validMoves.clear();
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) { // The 2 loops find the coordinate of a chess
                if (map[i][j] != currentOperator) continue;
                ArrayList<ValidMoves> tmpExpandedMoves = expand(i,j,currentOperator,false);
//                debug(tmpExpandedMoves);
                validMovesSet.addAll(tmpExpandedMoves);
            }
        }
        validMoves.addAll(validMovesSet);
        validMovesSet.clear();
    }

    public boolean isNextMoveValid(int x, int y) {
        for (ValidMoves i : validMoves) {
            if (i.x == x && i.y == y) return true;
        }
        return false;
    }

    protected int idxOfNextMove(int x, int y) { // To check the player's choice of move
        boolean flag = false;
        int idxOfValidMoves = -1;
        for (int i = 0; i < validMoves.size(); ++i) {
            if (validMoves.get(i).x == x && validMoves.get(i).y == y) {
                idxOfValidMoves = i;
                break;
            }
        }
        return idxOfValidMoves;
    }

    public void placeChess(int x, int y, int currentOperator, boolean ai) {
        int idx = idxOfNextMove(x,y); // To check is the place valid and to find its index in the container

        if (!ai) {
            Saving historicalBoard = new Saving(map, currentOperator);
            historicalBoards.add(historicalBoard);
        }
        map[x][y] = currentOperator;
        ArrayList<ValidMoves> tmp = expand(x,y,currentOperator,true);
        for (ValidMoves s : tmp) {
            int dir = s.negDirIdx, len = s.len, nx = s.x, ny = s.y;
            for (int i = 0; i < len; ++i) {
                nx += dx[dir];
                ny += dy[dir];
                map[nx][ny] = currentOperator;
            }
        }
        if (currentOperator == 1) { numOfBlack++; }
        else { numOfWhite++; }
        totPieces++;
    }

    public ArrayList<ValidMoves> expand(int x, int y, int currentOperator, boolean type) {
        // type = 0 : finding possible next choices before placing chess piece
        // type = 1 : reversing chess pieces after placing chess piece
        ArrayList<ValidMoves> expandedMoves = new ArrayList<>();
        if (this.map[x][y] != currentOperator) return expandedMoves;
        for (int dir = 0; dir < 8; ++dir) { // Search along 8 directions
            boolean atLeastAOpposite = false;
            int steps = 1;
            int nx = x + dx[dir], ny = y + dy[dir];
            while (!isLimitOverflow(nx,ny)) { // Search along a direction
                if (this.map[nx][ny] == (type ? currentOperator : 0 ) && atLeastAOpposite) {
                    ValidMoves n = new ValidMoves(nx,ny,steps,7-dir);
                    expandedMoves.add(n);
                    break;
                } else if (this.map[nx][ny] == 0 && !atLeastAOpposite) {
                    break;
                } else if (this.map[nx][ny] == (currentOperator == 1 ? 2 : 1)) { // Opposite color, keeping on searching
                    atLeastAOpposite = true;
                } else break;
                nx = nx + dx[dir];
                ny = ny + dy[dir];
                ++steps;
            }
        }
        return expandedMoves;
    }

    public void updateChessStatus() { // Re-count the numbers of black and white pieces and total
        numOfBlack = 0;
        numOfWhite = 0;
        totPieces  = 0;
//        historicalBoards.clear();
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (map[i][j] == 1) { ++numOfBlack; ++totPieces; }
                else if (map[i][j] == 2) { ++numOfWhite; ++totPieces; }
            }
        }
    }

    public void saveToFile(int[][] map, int currentOperator, int idx)
            throws FileNotFoundException, FileAlreadyExistsException {
        Saving save = new Saving(map, currentOperator);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy_MMdd_HHmmss");
        String saveName = now.format(formatter);

        File directory = new File("saves");
        if (!directory.exists()) {
            directory.mkdir();
        }

        File newFile = new File(String.format("/saves/%s.sav",saveName));
        if (newFile.exists()) {
            throw new FileAlreadyExistsException(saveName);
        }

        try (
                PrintWriter output = new PrintWriter(String.format("saves/%s.sav",saveName))
        ) {
            for (int i = 0 ; i < 8; ++i) {
                for (int j = 0; j < 8; ++j) {
                    output.print(map[i][j] + " ");
                }
                output.println();
            }
            output.println(currentOperator);
            output.close();
        }
    }

    public int loadFromSaving(Saving saving) {
        clearPlayerOptions();
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                this.map[i][j] = saving.map[i][j];
            }
        }
        updateChessStatus();
        return saving.currentOperator;
    }

    public int loadFromStack() {
        if (historicalBoards.empty()) { return 0; }
        Saving tmpSaving = historicalBoards.peek();
        loadFromSaving(tmpSaving);
        historicalBoards.pop();
        return tmpSaving.currentOperator;
    }

    public void clearPlayerOptions() {
        this.validMoves.clear();
        this.validMovesSet.clear();
    }

    protected boolean isLimitOverflow(int x, int y) {
        return x >= 8 || x < 0 || y >= 8 || y < 0;
    }

    protected void debug(@NotNull ArrayList<ValidMoves> a) {
        for (ValidMoves s : a) {
            int dir = s.negDirIdx, len = s.len, nx = s.x, ny = s.y;
            System.out.printf("x=%d, y=%d, len=%d, dir=%d\n",nx,ny,len,dir);
        }
        System.out.println("Printed.");
    }

    public void feedback(int op) {
        System.out.print("  ");
        for (int i = 0; i < 8; ++i) {
            System.out.printf("y%d ",i);
        }
        System.out.println();
        for (int i = 0; i < 8; ++i) {
            System.out.printf("x%d ",i);
            for (int j = 0; j < 8; ++j) {
                char output;
                if (map[i][j] == 1) {
                    output = '□';
                } else if (map[i][j] == 2) {
                    output = '■';
                } else {
                    output = '-';
                }
                System.out.print(output + "  "); // 2 blanks
            }
            System.out.println();
        }
        System.out.println();
        for (ValidMoves validMove : validMoves) {
            System.out.printf("x=%d, y=%d\n",
                    validMove.x, validMove.y);
        }
        System.out.printf("You are now playing as %s\n", op == 1 ? "BLACK(1)" : "WHITE(2)");
    }

    public int getTotPieces() {
        return totPieces;
    }

    public int getWinner() {
        if (numOfBlack == numOfWhite) {
            return 0;
        } else if (numOfBlack > numOfWhite) {
            return 1;
        } else { return 2; }
    }
}