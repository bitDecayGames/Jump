package com.bitdecay.jump.level.builder;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.controller.PathedBodyController;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.geom.PathPoint;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.List;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.PROPERTY, property="objectType")
public class PathedLevelObject extends LevelObject {
	public List<PathPoint> pathPoints;
	public boolean pendulum;

	public PathedLevelObject() {
		// Here for Json
	}

	/**
	 * @param rect the rectangle to become the body
	 * @param points path points relative to where rect is
	 * @param pendulum use true for back and forth, false for loop
	 */
	public PathedLevelObject(BitRectangle rect, List<PathPoint> points, boolean pendulum) {
		super(rect);
		this.pathPoints = points;
		this.pendulum = pendulum;
	}

	@Override
	public BitBody buildBody() {
		BitBody body = new BitBody();
		body.aabb = rect.copyOf();
		body.bodyType = BodyType.KINETIC;

		List<PathPoint> path = new ArrayList<>();
		pathPoints.forEach(point -> path.add(new PathPoint(rect.xy.plus(point.destination.x, point.destination.y), point.speed, point.stayTime)));

		body.controller = new PathedBodyController(path, pendulum);
		return body;
	}
}
