package pl.robertsadlowski.awardroutes.app.data.rulesModule;

public class MileageLevels {
    private final int y;
    private final int c;

    public MileageLevels(String y,String c) {
        this.y = Integer.valueOf(y);
        this.c = Integer.valueOf(c);
    }

    public int getY() {
        return y;
    }

    public int getC() {
        return c;
    }

    @Override
    public String toString() {
        return "y: " + y + ", c: " + c;
    }
}
