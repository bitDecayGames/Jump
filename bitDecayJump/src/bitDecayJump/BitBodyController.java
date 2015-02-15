package bitDecayJump;

public abstract class BitBodyController {

	protected BitBody body;

	public BitBodyController(BitBody body) {
		this.body = body;
	}

	public abstract void update(float delta);
}
