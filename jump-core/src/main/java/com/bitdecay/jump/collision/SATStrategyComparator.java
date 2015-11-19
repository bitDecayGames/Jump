package com.bitdecay.jump.collision;

/**
 * Created by Monday on 11/18/2015.
 */
public class SATStrategyComparator implements java.util.Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        SATStrategy first = (SATStrategy) o1;
        SATStrategy second = (SATStrategy) o2;
        int firstWeight = 0;
        int secondWeight = 0;

        for (BitCollision col : first.collisions) {
            firstWeight += col.against.bodyType.order;
        }
        for (BitCollision col : second.collisions) {
            secondWeight += col.against.bodyType.order;
        }
        return -1 * Integer.compare(firstWeight, secondWeight);
    }
}
