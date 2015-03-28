//package com.bitdecay.engine.utilities;
//
//import aurelienribon.tweenengine.TweenAccessor;
//
//import com.badlogic.gdx.scenes.scene2d.Actor;
//
//public class ActorAccessor implements TweenAccessor<Actor> {
//
//	public static final int RGB = 1;
//	public static final int ALPHA = 4;
//
//	@Override
//	public int getValues(Actor target, int tweenType, float[] returnValues) {
//		switch (tweenType) {
//		case RGB:
//			returnValues[0] = target.getColor().r;
//			returnValues[1] = target.getColor().g;
//			returnValues[2] = target.getColor().b;
//			return 3;
//		case ALPHA:
//			returnValues[0] = target.getColor().a;
//			return 1;
//		default:
//			assert false;
//			return -1;
//		}
//	}
//
//	@Override
//	public void setValues(Actor target, int tweenType, float[] newValues) {
//		switch (tweenType) {
//		case RGB:
//			target.setColor(newValues[0], newValues[1], newValues[2], target.getColor().a);
//		case ALPHA:
//			target.setColor(target.getColor().r, target.getColor().g, target.getColor().b, newValues[0]);
//			break;
//		default:
//			assert false;
//		}
//	}
//
//}
