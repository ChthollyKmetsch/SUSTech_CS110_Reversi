package Algorithms;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Algo implements Serializable {
    protected final int[] dx = {1,1,1,0,0,-1,-1,-1};
    protected final int[] dy = {0,1,-1,1,-1,1,-1,0};

    public int[][] map = new int[8][8];
    public ArrayList<ValidMoves> validMoves = new ArrayList<>();
    protected HashSet<ValidMoves> validMovesSet = new HashSet<>();

    public Stack<Saving> historicalBoards = new Stack<>();
    public int currentOperatorForLoading;

    public int numOfBlack = 2;
    public int numOfWhite = 2;
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

        updateChessStatus();
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

    public void saveToFile(Algo save, int currentOperator, boolean ai)
            throws IOException {
        currentOperatorForLoading = currentOperator;
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy_MMdd_HHmmss");
        String saveName = now.format(formatter);
        String tmpName;

        File directory = new File("saves");
        if (!directory.exists()) {
            directory.mkdir();
        }
        File directory2 = new File("mp_saves");
        if (!directory.exists()) {
            directory2.mkdir();
        }
        if (ai) {
            tmpName = String.format("saves/%s.sav", saveName);
        } else {
            tmpName = String.format("mp_saves/%s.sav", saveName);
        }

        FileOutputStream fileOutput = new FileOutputStream(tmpName);
        ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
        objectOutput.writeObject(save);
        objectOutput.close();
        fileOutput.close();
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

    public void loadFromAlgo(Algo newAlgo) {
        this.historicalBoards = (Stack<Saving>) newAlgo.historicalBoards.clone();
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                this.map[i][j] = newAlgo.map[i][j];
                this.currentOperatorForLoading = newAlgo.currentOperatorForLoading;
            }
        }
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
        totPieces = 0;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (map[i][j] != 0) { totPieces++; }
            }
        }
        return totPieces;
    }

    public int getWinner() {
        numOfBlack = 0;
        numOfWhite = 0;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (map[i][j] == 1) { numOfBlack++; }
                else if (map[i][j] == 2) { numOfWhite++; }
            }
        }
        if (numOfBlack == numOfWhite) {
            return 0;
        } else if (numOfBlack > numOfWhite) {
            return 1;
        } else { return 2; }
    }
}
