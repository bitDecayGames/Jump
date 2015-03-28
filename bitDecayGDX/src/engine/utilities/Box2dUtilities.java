//package com.bitdecay.engine.utilities;
//
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.physics.box2d.*;
//import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
//
///**
// * Allows for easy creation of common physics bodies such as Boxes and Circles.
// * 
// * @author Monday
// * @version 0.1 (March 2014)
// */
//public class Box2dUtilities {
//
//	public static Body createCircle(BodyType type, float radius, float density, com.badlogic.gdx.physics.box2d.World world, boolean isSensor) {
//		BodyDef def = new BodyDef();
//		def.type = type;
//		Body circle = world.createBody(def);
//
//		CircleShape poly = new CircleShape();
//		poly.setRadius(radius);
//		Fixture fixture = circle.createFixture(poly, density);
//		fixture.setSensor(isSensor);
//		fixture.setFriction(0);
//		poly.dispose();
//		
//		return circle;
//	}
//
//	public static Body createBox(BodyType type, float width, float height, float density, World world, boolean isSensor) {
//		BodyDef def = new BodyDef();
//		def.type = type;
//		Body box = world.createBody(def);
//
//		PolygonShape poly = new PolygonShape();
//		poly.setAsBox(width, height);
//		Fixture fixture = box.createFixture(poly, density);
//		fixture.setSensor(isSensor);
//		poly.dispose();
//		return box;
//	}
//
//	public static Fixture createBoxFixture(Body body, BodyType type, boolean isSensor, float width, float height, Vector2 center, int density,
//			World physicsWorld) {
//		PolygonShape poly = new PolygonShape();
//		poly.setAsBox(width / 2, height / 2, center, 0);
//		Fixture fixture = body.createFixture(poly, 1);
//		fixture.setSensor(isSensor);
//		return fixture;
//	}
//
//	public static Body createEdge(BodyType type, float startX, float startY, float endX, float endY, float density, World world) {
//		BodyDef def = new BodyDef();
//		def.type = type;
//		Body edge = world.createBody(def);
//
//		EdgeShape poly = new EdgeShape();
//		poly.set(new Vector2(startX, startY), new Vector2(endX, endY));
//		edge.createFixture(poly, density);
//		poly.dispose();
//
//		return edge;
//	}
//}
