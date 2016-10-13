package com.bitdecay.jump.collision;

import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.geom.GeomUtils;
import com.bitdecay.jump.geom.MathUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Monday on 6/18/2016.
 */
public class ProjectionUtilitiesTest {

    @Test
    public void testGetBundle() {
        BitRectangle proj1 = new BitRectangle(0, 0, 10, 10);
        BitRectangle proj2 = new BitRectangle(6, 5, 10, 10);

        ManifoldBundle bundle = ProjectionUtilities.getBundle(proj1, proj2);
        assertTrue(bundle != null);

        assertEquals(2, bundle.getCandidates().size(), 0);
    }

    @Test
    public void testGetBundleNoOverlap() {
        BitRectangle proj1 = new BitRectangle(0, 0, 10, 10);
        BitRectangle proj2 = new BitRectangle(50, 50, 10, 10);

        ManifoldBundle bundle = ProjectionUtilities.getBundle(proj1, proj2);
        assertTrue(bundle == null);
    }

    @Test
    public void testMaybeBuildBundle() {
        BitPoint[] points1 = new BitRectangle(0, 0, 5, 5).getProjectionPoints();
        BitPoint[] points2 = new BitRectangle(4, 0, 5, 5).getProjectionPoints();

        BitPoint projectionAxis = new BitPoint(1, 0);
        BitPoint solutionAxis = GeomUtils.NEG_X_AXIS;

        ManifoldBundle manifoldBundle = ProjectionUtilities.maybeBuildBundle(points1, points2, new HashSet<>(Arrays.asList(projectionAxis)));
        assertTrue(manifoldBundle != null);
        assertTrue(manifoldBundle.getCandidates().size() == 1);
        assertTrue("Axis should be correct solution axis", manifoldBundle.getCandidates().get(0).axis.equals(solutionAxis));
        assertEquals(1, manifoldBundle.getCandidates().get(0).distance, 0);
    }

    @Test
    public void testMaybeBuildBundleNoSolution() {
        BitPoint[] points1 = new BitRectangle(0, 0, 5, 5).getProjectionPoints();
        BitPoint[] points2 = new BitRectangle(5, 0, 5, 5).getProjectionPoints();

        BitPoint projectionAxis = new BitPoint(1, 0);

        ManifoldBundle manifoldBundle = ProjectionUtilities.maybeBuildBundle(points1, points2, new HashSet<>(Arrays.asList(projectionAxis)));
        assertTrue(manifoldBundle == null);
    }

    @Test
    public void testMaybeBuildBundleMultiAxis() {
        BitPoint[] points1 = new BitRectangle(0, 0, 5, 5).getProjectionPoints();
        BitPoint[] points2 = new BitRectangle(2, 3, 5, 5).getProjectionPoints();

        BitPoint xAxis = new BitPoint(1, 0);
        BitPoint yAxis = new BitPoint(0, 1);
        HashSet<BitPoint> projectionAxes = new HashSet<>(Arrays.asList(xAxis, yAxis));

        ManifoldBundle manifoldBundle = ProjectionUtilities.maybeBuildBundle(points1, points2, projectionAxes);
        assertTrue(manifoldBundle != null);
        assertTrue(manifoldBundle.getCandidates().size() == 2);

        BitPoint expectedXSolutionAxis = GeomUtils.NEG_X_AXIS;
        BitPoint expectedYSolutionAxis = GeomUtils.NEG_Y_AXIS;

        for (Manifold candidate : manifoldBundle.getCandidates()) {
            if (candidate.axis.equals(expectedXSolutionAxis)) {
                assertEquals(3, candidate.distance, 0);
            } else if (candidate.axis.equals(expectedYSolutionAxis)) {
                assertEquals(2, candidate.distance, 0);
            } else {
                fail("Unexpected axis returned in candidates");
            }
        }
    }

    @Test
    public void testBuildNormalsX() {
        BitPoint[] points = new BitPoint[] {new BitPoint(0, 0), new BitPoint(1, 0)};
        Set<BitPoint> axes = ProjectionUtilities.buildNormals(points);

        assertTrue(axes.size() == 1);

        BitPoint onlyAxis = axes.iterator().next();
        assertEquals(0, onlyAxis.x, 0);
        assertEquals(1, onlyAxis.y, 0);
    }

    @Test
    public void testBuildNormalsY() {
        BitPoint[] points = new BitPoint[] {new BitPoint(0, 0), new BitPoint(0, 1)};
        Set<BitPoint> axes = ProjectionUtilities.buildNormals(points);

        assertTrue(axes.size() == 1);

        BitPoint onlyAxis = axes.iterator().next();
        assertEquals(1, onlyAxis.x, 0);
        assertEquals(0, onlyAxis.y, 0);
    }

    @Test
    public void testBuildNonXYNormal() {
        BitPoint[] points = new BitPoint[] {new BitPoint(0, 0), new BitPoint(1, 1)};
        Set<BitPoint> axes = ProjectionUtilities.buildNormals(points);

        assertTrue(axes.size() == 1);

        BitPoint onlyAxis = axes.iterator().next();
        assertEquals((float) Math.sin(Math.PI / 4), onlyAxis.x, .0001f);
        assertEquals(-(float) Math.sin(Math.PI / 4), onlyAxis.y, .0001f);
    }

    @Test
    public void testBuildNormalsAASquare() {
        BitRectangle simpleRect = new BitRectangle(0, 0, 10, 10);
        BitPoint[] rectPoints = simpleRect.getProjectionPoints();

        Set<BitPoint> axes = ProjectionUtilities.buildNormals(rectPoints);
        assertTrue(axes.size() == 2);

        assertTrue(axes.contains(new BitPoint(1, 0)));
        assertTrue(axes.contains(new BitPoint(0, 1)));
    }

    @Test
    public void testProjectOntoX() {
        BitRectangle simpleRect = new BitRectangle(0, 0, 10, 10);
        BitPoint[] rectPoints = simpleRect.getProjectionPoints();

        BitPoint axis = new BitPoint(1, 0);

        BitPoint projection = ProjectionUtilities.project(axis, rectPoints);
        assertTrue(projection.x == 0);
        assertTrue(projection.y == 10);
    }

    @Test
    public void testProjectOntoY() {
        BitRectangle simpleRect = new BitRectangle(0, 0, 10, 10);
        BitPoint[] rectPoints = simpleRect.getProjectionPoints();

        BitPoint axis = new BitPoint(0, 1);

        BitPoint projection = ProjectionUtilities.project(axis, rectPoints);
        assertTrue(projection.x == 0);
        assertTrue(projection.y == 10);
    }

    @Test
    public void testProjectOntoArbitrary() {
        BitRectangle simpleRect = new BitRectangle(0, 0, 10, 10);
        BitPoint[] rectPoints = simpleRect.getProjectionPoints();

        BitPoint axis = new BitPoint(1, 1);

        BitPoint projection = ProjectionUtilities.project(axis, rectPoints);
        assertTrue(projection.x == 0);
        assertTrue(MathUtils.close(projection.y, (float) (10 * Math.sqrt(2))));
    }

    @Test
    public void testLinearOverlapNoOverlap() {
        BitPoint minMax1 = new BitPoint(0, 10);
        BitPoint minMax2 = new BitPoint(15, 25);

        Float overlap = ProjectionUtilities.getLinearOverlap(minMax1, minMax2);
        assertTrue(overlap == null);
    }

    @Test
    public void testLinearOverlapSimple() {
        BitPoint minMax1 = new BitPoint(0, 10);
        BitPoint minMax2 = new BitPoint(5, 20);

        Float overlap = ProjectionUtilities.getLinearOverlap(minMax1, minMax2);
        assertEquals(-5, overlap.floatValue(), 0);
    }

    @Test
    public void testLinearOverlapLeftOriented() {
        BitPoint minMax1 = new BitPoint(5, 20);
        BitPoint minMax2 = new BitPoint(0, 10);

        Float overlap = ProjectionUtilities.getLinearOverlap(minMax1, minMax2);
        assertEquals(5, overlap.floatValue(), 0);
    }
}
