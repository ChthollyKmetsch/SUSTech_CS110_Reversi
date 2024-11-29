import java.util.ArrayList;

public class AI extends Algo {
    private final int[][] estimate = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0,500,-25, 10,  5,  5, 10,-25,500, 0},
            {0,-25,-45,  1,  1,  1,  1,-45,-25, 0},
            {0, 10,  1,  3,  2,  2,  3,  1, 10, 0},
            {0,  5,  1,  2,  1,  1,  2,  1,  5, 0},
            {0,  5,  1,  2,  1,  1,  2,  1,  5, 0},
            {0, 10,  1,  3,  2,  2,  3,  1, 10, 0},
            {0,-25,-45,  1,  1,  1,  1,-45,-25, 0},
            {0,500,-25, 10,  5,  5, 10,-25,500, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    };
    private int targetDepth;
    private int[][] alpha = new int[10][10];
    private int[][] beta = new int[10][10];
    private int[][] score = new int[10][10];
    public AI(Algo algo, int targetDepth) {
        this.map = algo.map.clone();
        this.targetDepth = targetDepth;
    }
    public Pair search(int x, int y, int currentOperator, int currentDepth) {
        // Brute force for now
        if (currentDepth == targetDepth) {
            return new Pair(x,y,estimate[x][y]);
        }
        Pair ans = new Pair(x,y,estimate[x][y]);
        findValidPlace(currentOperator);
        int[][] originMap = map.clone();
        for (int i = 0; i < validMoves.size(); ++i) {
            map = originMap.clone();
            int nx = validMoves.get(i).x;
            int ny = validMoves.get(i).y;
            placeChess(nx,ny,currentOperator);
            if (currentDepth < targetDepth) {
                ans = search(nx,ny,currentOperator == 1 ? 2 : 1, currentDepth+1);
            }
        }
        return ans;
    }
}
