public class Board {
    private Chess[][] map = new Chess[10][10];
    /* 1 represents BLACK, 2 represents WHITE, 0 represents NULL
          Initial Board
     \ x 1 2 3 4 5 6 7 8
     y \
     1   0 0 0 0 0 0 0 0
     2   0 0 0 0 0 0 0 0
     3   0 0 0 0 0 0 0 0
     4   0 0 0 2 1 0 0 0
     5   0 0 0 1 2 0 0 0
     6   0 0 0 0 0 0 0 0
     7   0 0 0 0 0 0 0 0
     8   0 0 0 0 0 0 0 0
    */
    public void initialize() {
        this.map[4][4] = new Chess(4, 4, 2);
        this.map[5][5] = new Chess(5, 5, 2);
        this.map[5][4] = new Chess(5, 4, 1);
        this.map[4][5] = new Chess(4, 5, 1);
    }
    public void placeChess(Chess newChess) {
        this.map[newChess.getX()][newChess.getY()] = newChess;
    }
}