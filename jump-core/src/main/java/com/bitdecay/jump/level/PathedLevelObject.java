package com.bitdecay.jump.level;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.controller.PathedBodyController;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.geom.BitRectangle;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.List;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.PROPERTY, property="objectType")
public class PathedLevelObject extends LevelObject {
	List<BitPoint> pathPoints;
	private float speed;
	private boolean pendulum;

	/**
	 * @param rect the rectangle to become the body
	 * @param points path points relative to where rect is
	 * @param speed how fast the object should move
	 * @param pendulum use true for back and forth, false for loop
	 */
	public PathedLevelObject(BitRectangle rect, List<BitPoint> points, float speed, boolean pendulum) {
		super(rect);
		this.pathPoints = points;
		this.speed = speed;
		this.pendulum = pendulum;
	}

	@Override
	public BitBody buildBody() {
		BitBody body = new BitBody();
		body.aabb = rect;
		body.bodyType = BodyType.KINETIC;

		List<BitPoint> path = new ArrayList<>();
		pathPoints.forEach(point -> path.add(rect.xy.plus(point)));

		body.controller = new PathedBodyController(path, pendulum, speed);
		return body;
	}

	//TODO needs notion of path and move speed.
}
