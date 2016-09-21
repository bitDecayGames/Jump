package com.bitdecay.jump.level.builder;

import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.geom.PathPoint;
import com.bitdecay.jump.level.DebugSpawnObject;
import com.bitdecay.jump.level.Level;
import com.bitdecay.jump.level.LevelObject;

import java.util.List;

/**
 * Created by Monday on 9/20/2016.
 */
public interface ILevelBuilder {
    void createObject(LevelObject object);

    void selectObject(BitPointInt startPoint, boolean shift);

    void selectObject(BitPointInt point, boolean addToSelection, boolean selectGridObjects);

    void selectObjects(BitRectangle rect, boolean addToSelection);

    void deleteSelected();

    List<LevelObject> getSelection();

    void createLevelObject(BitPointInt startPoint, BitPointInt endPoint, boolean b, int material);

    int getCellSize();

    void createKineticObject(BitRectangle platform, List<PathPoint> pathPoints, boolean pendulum);

    void setDebugSpawn(DebugSpawnObject debugSpawnObject);

    Level getLevel();

    Level optimizeLevel();

    void newLevel(int cellSize);

    void setLevel(Level level);
}
