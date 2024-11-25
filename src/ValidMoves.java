public class ValidMoves {
    public int x, y, len, negDirIdx;
    public ValidMoves(int x, int y, int len, int negDirIdx) {
        this.x = x;
        this.y = y;
        this.len = len;
        this.negDirIdx = negDirIdx; // This variable refers to the index of direction to the expanding original point
    }
    public ValidMoves(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
