package bitDecayJump.geom;

public class BitSATRectangle implements Projectable {
    public BitPoint xy;
    public float halfWidth;
    public float halfHeight;

    public BitPoint topLeft;
    public BitPoint topRight;
    public BitPoint bottomLeft;
    public BitPoint bottomRight;

    public BitSATRectangle() {
        // here for JSON
    }

    public BitSATRectangle(BitSATRectangle other) {
        this(other.xy.x, other.xy.y, other.halfWidth, other.halfHeight);
    }

    public BitSATRectangle(float x, float y, float halfWidth, float halfHeight) {
        xy = new BitPoint(x, y);
        this.halfWidth = halfWidth;
        this.halfHeight = halfHeight;

        if (halfWidth < 0) {
            xy.x += halfWidth;
            this.halfWidth *= -1;
        }

        if (this.halfHeight < 0) {
            xy.y += this.halfHeight;
            this.halfHeight *= -1;
        }
    }

    public float getHalfWidth() {
        return halfWidth;
    }

    public float getHalfHeight() {
        return halfHeight;
    }

    public void translate(BitPoint point) {
        translate(point.x, point.y);
    }

    public void translate(float x, float y) {
        xy.x += x;
        xy.y += y;
    }

    public BitPoint[] getProjectionPoints() {
        return new BitPoint[] {xy.plus(-halfWidth, -halfHeight),  xy.plus(-halfWidth, halfHeight), xy.plus(halfWidth, halfHeight), xy.plus(halfWidth, -halfHeight), };
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BitRectangle other = (BitRectangle) obj;
        if (halfHeight != other.height) {
            return false;
        }
        if (halfWidth != other.width) {
            return false;
        }
        if (xy == null) {
            if (other.xy != null) {
                return false;
            }
        } else if (!xy.looseEquals(other.xy)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "(x: " + xy.x + ", y: " + xy.y + " - halfWidth: " + halfWidth + ", halfHeight: " + halfHeight + ")";
    }
}
