package com.bitdecay.jump.gdx.engine.utilities;

public class BitPoint {
    public int x;
    public int y;

    public BitPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Copy Constructor
     *
     * @param other
     */
    public BitPoint(BitPoint other) {
        this.x = other.x;
        this.y = other.y;
    }

    public void set(BitPoint other) {
        this.x = other.x;
        this.y = other.y;
    }

    public BitPoint add(BitPoint other) {
        return new BitPoint(this.x + other.x, this.y + other.y);
    }

    public BitPoint subtract(BitPoint other) {
        return new BitPoint(this.x - other.x, this.y - other.y);
    }

    public int manhattanDistanceTo(BitPoint other) {
        return Math.abs(other.x - this.x) + Math.abs(other.y - this.y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
