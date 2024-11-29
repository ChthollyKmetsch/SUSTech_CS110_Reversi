public class Chess {
    private int[] pos = new int[2];
    private int type;
    public Chess() {}
    public Chess(int x, int y, int type) {
        this.pos[0] = x;
        this.pos[1] = y;
        this.type   = type;
    }
    public void reverse() {
        this.type = (this.type == 1 ? 0 : 1);
    }
    public int getX() {
        return this.pos[0];
    }
    public int getY() {
        return this.pos[1];
    }
    public int getType() {
        return this.type;
    }
}
