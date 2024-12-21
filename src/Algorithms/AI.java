package Algorithms;

import java.util.ArrayList;

public class AI extends Algo {
    public static final int INF = 32767;
    private final int[][] estimate = {
            {500,-25, 10,  5,  5, 10,-25,500},
            {-25,-45,  1,  1,  1,  1,-45,-25},
            { 10,  1,  3,  2,  2,  3,  1, 10},
            {  5,  1,  2,  1,  1,  2,  1,  5},
            {  5,  1,  2,  1,  1,  2,  1,  5},
            { 10,  1,  3,  2,  2,  3,  1, 10},
            {-25,-45,  1,  1,  1,  1,-45,-25},
            {500,-25, 10,  5,  5, 10,-25,500},
    };
    private int targetDepth;

    public AI(Algo algo, int targetDepth) {
        this.map = algo.map.clone();
        this.targetDepth = targetDepth;
    }

    public Pair search(int currentOperator, int currentDepth, int alpha, int beta) { //
        // alpha beta
        validMoves.clear();
        findValidPlace(currentOperator);
        ArrayList<ValidMoves> currentValidMoves = new ArrayList<>(validMoves);
        if (currentDepth >= targetDepth || getCurrentBlanks() < targetDepth-currentDepth) { // 搜到头
            int finalVal = 0;
            int finalX = 0, finalY = 0;
            boolean firstMove = false;
            for (ValidMoves validMove : currentValidMoves) {
                int x = validMove.x;
                int y = validMove.y;
                if (currentOperator == 1) { // Max node
                    if (!firstMove) {
                        finalVal = -INF;
                        firstMove = true;
                    }
                    if (estimate[x][y] > finalVal) {
                        finalVal = estimate[x][y];
                        finalX = x;
                        finalY = y;
                    }
                } else { // Min node
                    if (!firstMove) {
                        finalVal = INF;
                        firstMove = true;
                    }
                    if (estimate[x][y] < finalVal) {
                        finalVal = estimate[x][y];
                        finalX = x;
                        finalY = y;
                    }
                }
            }
            return new Pair(finalX,finalY,finalVal);
        }

        Pair ans;
        int ansVal = 0;
        if (currentOperator == 1) {
            ans = new Pair(1,2,-INF);
        } else {
            ans = new Pair(1,2,INF);
        }

        int[][] originMap = new int[8][8]; // whiteboard game map
        for (int i = 0; i < 8; ++i) {// copy array manually, in case of soft copy
            for (int j = 0; j < 8; ++j)
                originMap[i][j] = map[i][j];
        }

        if (currentOperator == 1) { // Max node
            for (ValidMoves currentValidMove : currentValidMoves) {
                int nx = currentValidMove.x;
                int ny = currentValidMove.y;
                placeChess(nx, ny, currentOperator, true);
                Pair tmp = search(2, currentDepth+1, alpha, beta);
                for (int i = 0; i < 8; ++i) { // Return to the original situation manually
                    for (int j = 0; j < 8; ++j)
                        map[i][j] = originMap[i][j];
                }
                if (tmp.getVal() > alpha) {
                    alpha = tmp.getVal();
                    ans = new Pair(nx,ny,alpha);
                }
                if (beta <= alpha) { break; }
            }
        } else {
            for (ValidMoves currentValidMove : currentValidMoves) {
                int nx = currentValidMove.x;
                int ny = currentValidMove.y;
                placeChess(nx, ny, currentOperator, true);
                Pair tmp = search(1, currentDepth+1, alpha, beta);
                for (int i = 0; i < 8; ++i) { // Return to the original situation manually
                    for (int j = 0; j < 8; ++j)
                        map[i][j] = originMap[i][j];
                }
                if (tmp.getVal() < beta) {
                    beta = tmp.getVal();
                    ans = new Pair(nx,ny,beta);
                }
                if (beta <= alpha) { break; }
            }
        }
        return ans;
    }

    @Override
    public void feedback(int op) {
        System.out.print("  ");
        for (int i = 0; i < 8; ++i) {
            System.out.printf("y%d ",i);
        }
        System.out.println();
        for (int i = 0; i < 8; ++i) {
            System.out.printf("x%d ",i);
            for (int j = 0; j < 8; ++j) {
                System.out.printf("%d  ",this.map[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    @Override
    protected int idxOfNextMove(int x, int y) {
        return INF;
    }

    private int getCurrentBlanks() {
        int ans = 0;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0 ; j < 8; ++j) {
                if (this.map[i][j] == 0) ++ans;
            }
        }
        return ans;
    }
}
