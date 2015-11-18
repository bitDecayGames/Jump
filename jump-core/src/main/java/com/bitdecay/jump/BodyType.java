package com.bitdecay.jump;

public enum BodyType {
	STATIC(2),
	KINETIC(1),
	DYNAMIC(0);


	public final int order;

	BodyType(int order) {
		this.order = order;
	}
}
