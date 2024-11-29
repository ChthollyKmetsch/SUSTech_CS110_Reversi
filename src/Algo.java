import java.util.*;

public class Algo {
    protected final int[] dx = {1,1,1,0,0,-1,-1,-1};
    protected final int[] dy = {0,1,-1,1,-1,1,-1,0};
    protected int[][] map = new int[10][10];
    protected ArrayList<ValidMoves> validMoves = new ArrayList<ValidMoves>();
    protected HashSet<ValidMoves> validMovesSet = new HashSet<>();
    public void initiate() {
        this.map[4][4] = 2;
        this.map[5][5] = 2;
        this.map[4][5] = 1;
        this.map[5][4] = 1;
    } // 1 is black, 2 is white
    public void findValidPlace(int currentOperator) {
        for (int i = 1; i <= 8; ++i) {
            for (int j = 1; j <= 8; ++j) { // The 2 loops find the coordinate of a chess
                if (map[i][j] != currentOperator) continue;
                ArrayList<ValidMoves> tmpExpandedMoves = expand(i,j,currentOperator,false);
//                debug(tmpExpandedMoves);
                validMovesSet.addAll(tmpExpandedMoves);
            }
        }
        validMoves.addAll(validMovesSet);
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
    public boolean placeChess(int x, int y, int currentOperator) {
        int idx = idxOfNextMove(x,y); // To check is the place valid and to find its index in the container
        if (idx == -1) {
            return false;
        }
        map[x][y] = currentOperator;
        ArrayList<ValidMoves> tmp = expand(x,y,currentOperator,true);
//        debug(tmp);
        for (ValidMoves s : tmp) {
            int dir = s.negDirIdx, len = s.len, nx = s.x, ny = s.y;

            for (int i = 0; i < len; ++i) {
                nx += dx[dir];
                ny += dy[dir];
                map[nx][ny] = currentOperator;
            }
        }
        return true;
    }
    public void feedback(int op) {
        System.out.print("  ");
        for (int i = 1; i <= 8; ++i) {
            System.out.printf("y%d ",i);
        }
        System.out.println();
        for (int i = 1; i <= 8; ++i) {
            System.out.printf("x%d ",i);
            for (int j = 1; j <= 8; ++j) {
                System.out.printf("%d  ",this.map[i][j]);
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
    public ArrayList<ValidMoves> expand(int x, int y, int currentOperator, boolean type) {
        // type = 0 : finding possible next choices when placing chess piece
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
    public void clearPlayerOptions() {
        this.validMoves.clear();
        this.validMovesSet.clear();
    }
    protected boolean isLimitOverflow(int x, int y) {
        return x > 8 || x < 1 || y > 8 || y < 1;
    }
    protected void debug(ArrayList<ValidMoves> a) {
        for (ValidMoves s : a) {
            int dir = s.negDirIdx, len = s.len, nx = s.x, ny = s.y;
            System.out.printf("x=%d, y=%d, len=%d, dir=%d\n",nx,ny,len,dir);
        }
        System.out.println("Printed.");
    }
}
