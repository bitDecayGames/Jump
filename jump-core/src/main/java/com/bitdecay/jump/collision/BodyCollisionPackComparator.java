package com.bitdecay.jump.collision;

import com.bitdecay.jump.BitBody;

/**
 * Created by Monday on 9/5/2016.
 */
public class BodyCollisionPackComparator implements java.util.Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        BodyCollisionPack first = (BodyCollisionPack) o1;
        BodyCollisionPack second = (BodyCollisionPack) o2;
        int firstWeight = 0;
        int secondWeight = 0;

        for (BitBody col : first.suspects) {
            firstWeight += col.bodyType.order;
        }
        for (BitBody col : second.suspects) {
            secondWeight += col.bodyType.order;
        }
        return -1 * Integer.compare(firstWeight, secondWeight);
    }
}
