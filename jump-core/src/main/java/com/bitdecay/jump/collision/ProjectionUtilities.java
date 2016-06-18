package com.bitdecay.jump.collision;

import com.bitdecay.jump.annotation.VisibleForTesting;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.geom.Projectable;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Monday on 9/4/2015.
 */
public class ProjectionUtilities {

    /**
     * Builds a resolution to move p1 out of p2 if necessary
     *
     * @param p1 the shape to be resolved
     * @param p2 the shape to resolve against
     * @return the resolution strategy, or null if the shapes do not intersect
     */
    public static ManifoldBundle getBundle(Projectable p1, Projectable p2) {
        BitPoint[] points1 = p1.getProjectionPoints();
        BitPoint[] points2 = p2.getProjectionPoints();

        Set<BitPoint> perpendicularAxes = new HashSet<>();

        perpendicularAxes.addAll(buildNormals(points1));
        perpendicularAxes.addAll(buildNormals(points2));

        return maybeBuildBundle(points1, points2, perpendicularAxes);
    }

    @VisibleForTesting
    static ManifoldBundle maybeBuildBundle(BitPoint[] points1, BitPoint[] points2, Set<BitPoint> perpendicularAxes) {
        ManifoldBundle res = null;
        for (BitPoint axis : perpendicularAxes) {
            BitPoint line1 = project(axis, points1);
            BitPoint line2 = project(axis, points2);
            Float overlap = getLinearOverlap(line1, line2);
            if (overlap != null) {
                if (res == null) {
                    // only instantiate the resolution if we need to.
                    res = new ManifoldBundle();
                }
                res.addCandidate(new Manifold(axis, overlap));
            } else {
                // if any axis has no overlap, then the shapes do not intersect
                return null;
            }
        }
        return res;
    }

    /**
     * Builds all perpendicular axes. Intentionally creates them all as unit vectors in the first and second cartesian quardrants.
     *
     * @param points The points to build perpendiculars for
     */
    @VisibleForTesting
    static Set<BitPoint> buildNormals(BitPoint[] points) {
        Set<BitPoint> perpendicularAxes = new HashSet<>();
        BitPoint firstPoint;
        BitPoint secondPoint;
        for (int i = 0; i < points.length; i++) {
            firstPoint = points[i];
            secondPoint = points[(i + 1) % points.length];

            float run = secondPoint.x - firstPoint.x;
            float rise = secondPoint.y - firstPoint.y;
            if (run == 0) {
                // vertical line, so perpendicular is horizontal
                perpendicularAxes.add(new BitPoint(1, 0));
            } else if (rise == 0) {
                // horizontal line, so perpendicular is vertical
                perpendicularAxes.add(new BitPoint(0, 1));
            } else {
                float perpSlope = -run / rise;
                BitPoint perpAxis = new BitPoint(1, perpSlope).normalize();
                perpendicularAxes.add(perpAxis);
            }
        }
        return perpendicularAxes;
    }

    /**
     * Projects the given points along the provided axis.
     * @param slope
     * @param points
     * @return A pair of floats of the format (min, max) values along the given axis
     */
    public static BitPoint project(BitPoint slope, BitPoint... points) {
        BitPoint axis = new BitPoint(slope.x, slope.y).normalize();

        float min = Float.POSITIVE_INFINITY;
        float max = Float.NEGATIVE_INFINITY;
        float value;
        for (BitPoint point : points) {
            value = axis.dot(point.x, point.y);
            min = Math.min(min, value);
            max = Math.max(max, value);
        }

        return new BitPoint(min, max);
    }

    /**
     * Given two pairs of (min, max), return the distance they overlap.
     * The pairs are assumed to be projections onto the same axis. The
     * sign of the distance will be such that l1 will no longer be
     * overlapping l2 if it is translated by the distance.
     * @param l1
     * @param l2
     * @return the distance of overlap, or null if the two do not overlap
     */
    public static Float getLinearOverlap(BitPoint l1, BitPoint l2) {
        // knowing which ends are closer will tell us which way the intersection came from.
        float minEnd = Math.min(l1.y, l2.y);
        float maxStart = Math.max(l1.x, l2.x);
        float overlap = minEnd - maxStart;

        if (overlap > 0) {
            float diff1 = Math.abs(l1.x - l2.y);
            float diff2 = Math.abs(l2.x - l1.y);

            if (diff2 < diff1) {
                // resolve left
                overlap *= -1;
            }
            return overlap;
        } else {
            return null;
        }
    }
}