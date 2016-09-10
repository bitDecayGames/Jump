package com.bitdecay.jump.collision;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds a group of manifolds and ensures all distances are positive upon being added to the bundle.
 * Created by Monday on 9/6/2015.
 */
public class ManifoldBundle {
    private List<Manifold> manifoldCandidates = new ArrayList<>();

    public void addCandidate(Manifold manifold) {
        manifoldCandidates.add(manifold);
    }

    public List<Manifold> getCandidates() {
        return manifoldCandidates;
    }
}

