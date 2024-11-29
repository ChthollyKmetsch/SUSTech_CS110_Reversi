import java.util.Objects;

public class ValidMoves implements Comparable<ValidMoves>{
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
    @Override//
    public int compareTo(ValidMoves a) {
        if (this.x == a.x) return this.y - a.y;
        return this.x - a.x;
    }
    @Override
    public boolean equals(Object a) {
        ValidMoves tmp = (ValidMoves) a;
        return this.x == tmp.x && this.y == tmp.y;
    }
    @Override
    public int hashCode() {
        return Objects.hash(x,y);
    }
}
