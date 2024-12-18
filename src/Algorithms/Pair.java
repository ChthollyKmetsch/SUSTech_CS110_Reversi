package Algorithms;

import java.util.Objects;

public class Pair implements Comparable<Pair> {
    private int ft;
    private int sc;
    private int val;
    public Pair(int ft, int sc, int val) {
        this.ft = ft;
        this.sc = sc;
        this.val = val;
    }
    public Pair(Pair a) {
        this.ft = a.getFt();//
        this.sc = a.getSc();
        this.val = a.getVal();
    }

    public int getFt() {
        return ft;
    }

    public int getSc() {
        return sc;
    }

    public int getVal() {
        return val;
    }

    @Override
    public int compareTo(Pair a) {
        if (this.ft == a.ft) {
            return this.sc - a.sc;
        }
        return this.ft - a.ft;
    }
    @Override
    public boolean equals(Object a) {
        Pair tmp = (Pair) a;
        return this.ft == tmp.ft && this.sc == tmp.sc;
    }
    @Override
    public int hashCode() {
        return Objects.hash(ft,sc);
    }
}
