package com.bitdecay.jump.level;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.controller.PathedBodyController;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.geom.PathPoint;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import sun.security.tools.PathList;

import java.util.ArrayList;
import java.util.List;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.PROPERTY, property="objectType")
public class PathedLevelObject extends LevelObject {
	public List<PathPoint> pathPoints;
	public float speed;
	public boolean pendulum;

	public PathedLevelObject() {
		// Here for Json
	}

	/**
	 * @param rect the rectangle to become the body
	 * @param points path points relative to where rect is
	 * @param speed how fast the object should move
	 * @param pendulum use true for back and forth, false for loop
	 */
	public PathedLevelObject(BitRectangle rect, List<PathPoint> points, float speed, boolean pendulum) {
		super(rect);
		this.pathPoints = points;
		this.speed = speed;
		this.pendulum = pendulum;
	}

	@Override
	public BitBody buildBody() {
		BitBody body = new BitBody();
		body.aabb = rect.copyOf();
		body.bodyType = BodyType.KINETIC;

		List<PathPoint> path = new ArrayList<>();
		pathPoints.forEach(point -> path.add(new PathPoint(rect.xy.plus(point.destination.x, point.destination.y), point.stayTime)));

		body.controller = new PathedBodyController(path, pendulum, speed);
		return body;
	}
}
