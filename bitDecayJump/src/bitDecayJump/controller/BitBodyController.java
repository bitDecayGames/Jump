package bitDecayJump.controller;

import bitDecayJump.BitBody;

public abstract class BitBodyController {

	protected BitBody body;

	public BitBodyController(BitBody body) {
		this.body = body;
	}

	public abstract void update(float delta);
}
