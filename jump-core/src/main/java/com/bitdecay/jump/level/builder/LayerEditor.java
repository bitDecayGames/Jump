package com.bitdecay.jump.level.builder;

import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.geom.PathPoint;

import java.util.List;

/**
 * Created by Monday on 9/19/2016.
 */
public interface LayerEditor {

    void createLevelObject(BitPointInt startPoint, BitPointInt endPoint, boolean oneway, int material);
    void createKineticObject(BitRectangle rect, List<PathPoint> path, boolean pendulum);
}
