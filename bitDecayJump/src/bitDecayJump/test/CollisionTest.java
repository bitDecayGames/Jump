package bitDecayJump.test;

import static org.junit.Assert.*;

import org.junit.*;

import bitDecayJump.*;
import bitDecayJump.geom.*;
import bitDecayJump.level.*;

@BitDecayTest
public class CollisionTest {
	BitWorld world;
	BitBodyProps props;

	@Before
	public void setUp() throws Exception {
		world = new BitWorld();
		props = new BitBodyProps();
		props.accelX = Integer.MAX_VALUE;
		props.bodyType = BodyType.DYNAMIC;
		props.gravitational = false;
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Internal corner tests for bodies smaller than 1 tile size
	 */
	@Test
	public void testInternalCornersSmallBody() {
		testInternalCornersSmallBody(world);
	}

	public void testInternalCornersSmallBody(BitWorld world) {
		world.setLevel(loadTestLevel("internalCorners.level"));

		BitBody downLeftCorner = world.createBody(22, 25, 21, 20, props.clone());
		downLeftCorner.props.velocity.x = -1;
		BitBody downRightCorner = world.createBody(57, 25, 11, 11, props.clone());
		downRightCorner.props.velocity.x = 1;
		BitBody upRightCorner = world.createBody(57, 56, 13, 13, props.clone());
		upRightCorner.props.velocity.x = 1;
		BitBody upLeftCorner = world.createBody(20, 57, 16, 18, props.clone());
		upLeftCorner.props.velocity.x = -1;
		upLeftCorner.props.velocity.y = 1;
		world.step(.01f);
		assertTrue("Down Left Internal corner resolution", downLeftCorner.aabb.xy.looseEquals(new BitPoint(32, 32)));
		assertTrue("Down Right Internal corner resolution", downRightCorner.aabb.xy.looseEquals(new BitPoint(64 - downRightCorner.aabb.width, 32)));
		assertTrue("Up Right Internal corner resolution",
				upRightCorner.aabb.xy.looseEquals(new BitPoint(64 - upRightCorner.aabb.width, 64 - upRightCorner.aabb.height)));
		assertTrue("Up Left Internal corner resolution", upLeftCorner.aabb.xy.looseEquals(new BitPoint(32, 64 - upLeftCorner.aabb.height)));
		fail("Break");
	}

	/**
	 * Internal corner tests for bodies larger than 1 tile size
	 */
	@Test
	public void testInternalCornersLargeBody() {
		testInternalCornersLargeBody(world);
	}

	public void testInternalCornersLargeBody(BitWorld world) {
		world.setLevel(loadTestLevel("internalCornersLargeBody.level"));

		BitBody downLeftCorner = world.createBody(12, 11, 43, 44, props.clone());
		downLeftCorner.props.velocity.x = -1;
		BitBody downRightCorner = world.createBody(47, 15, 61, 54, props.clone());
		downRightCorner.props.velocity.x = 1;
		BitBody upRightCorner = world.createBody(67, 69, 51, 48, props.clone());
		upRightCorner.props.velocity.x = 1;
		BitBody upLeftCorner = world.createBody(22, 50, 54, 57, props.clone());
		upLeftCorner.props.velocity.x = -1;
		world.step(.01f);
		assertTrue("Down Left Internal corner resolution", downLeftCorner.aabb.xy.looseEquals(new BitPoint(32, 32)));
		assertTrue("Down Right Internal corner resolution", downRightCorner.aabb.xy.looseEquals(new BitPoint(96 - downRightCorner.aabb.width, 32)));
		assertTrue("Up Right Internal corner resolution",
				upRightCorner.aabb.xy.looseEquals(new BitPoint(96 - upRightCorner.aabb.width, 96 - upRightCorner.aabb.height)));
		assertTrue("Up Left Internal corner resolution", upLeftCorner.aabb.xy.looseEquals(new BitPoint(32, 96 - upLeftCorner.aabb.height)));
	}

	@Test
	public void testBasicWallCollisionsSmall() {
		testBasicWallCollisionsSmall(world);
	}

	public void testBasicWallCollisionsSmall(BitWorld world) {
		world.setLevel(loadTestLevel("singleBlock.level"));
		BitBody leftSide = world.createBody(-13, 5, 18, 20, props.clone());
		leftSide.props.velocity.x = 1;
		BitBody rightSide = world.createBody(22, 5, 17, 17, props.clone());
		rightSide.props.velocity.x = -1;
		BitBody topSide = world.createBody(8, 27, 14, 15, props.clone());
		BitBody bottomSide = world.createBody(4, -9, 16, 18, props.clone());

		world.step(.01f);

		assertTrue("Left Side resolution", leftSide.aabb.xy.x == -leftSide.aabb.width);
		assertTrue("Right Side resolution", rightSide.aabb.xy.x == 32);
		assertTrue("Top Side resolution", topSide.aabb.xy.y == 32);
		assertTrue("Bottom Side resolution", bottomSide.aabb.xy.y == -bottomSide.aabb.height);
	}

	@Test
	public void testBasicWallCollisionsSmallSplitBlock() {
		testBasicWallCollisionsSmallSplitBlock(world);
	}

	public void testBasicWallCollisionsSmallSplitBlock(BitWorld world) {
		world.setLevel(loadTestLevel("blockLarge.level"));
		BitBody leftSide = world.createBody(-8, 58, 15, 13, props.clone());
		leftSide.props.velocity.x = 1;
		BitBody rightSide = world.createBody(87, 57, 13, 14, props.clone());
		rightSide.props.velocity.x = -1;
		BitBody topSide = world.createBody(55, 89, 17, 17, props.clone());
		BitBody bottomSide = world.createBody(22, -6, 16, 14, props.clone());

		world.step(.01f);

		assertTrue("Left Side resolution", leftSide.aabb.xy.x == -leftSide.aabb.width);
		assertTrue("Right Side resolution", rightSide.aabb.xy.x == 96);
		assertTrue("Top Side resolution", topSide.aabb.xy.y == 96);
		assertTrue("Bottom Side resolution", bottomSide.aabb.xy.y == -bottomSide.aabb.height);
	}

	@Test
	public void testBasicWallCollisionsLargeSplitBlock() {
		testBasicWallCollisionsLargeSplitBlock(world);
	}

	public void testBasicWallCollisionsLargeSplitBlock(BitWorld World) {
		world.setLevel(loadTestLevel("blockLarge.level"));
		BitBody leftSide = world.createBody(-40, 15, 62, 64, props.clone());
		leftSide.props.velocity.x = 1;
		BitBody rightSide = world.createBody(72, 19, 60, 64, props.clone());
		rightSide.props.velocity.x = -1;
		BitBody topSide = world.createBody(14, 82, 72, 84, props.clone());
		BitBody bottomSide = world.createBody(18, -37, 57, 55, props.clone());

		world.step(.01f);

		assertTrue("Left Side resolution", leftSide.aabb.xy.x == -leftSide.aabb.width);
		assertTrue("Right Side resolution", rightSide.aabb.xy.x == 96);
		assertTrue("Top Side resolution", topSide.aabb.xy.y == 96);
		assertTrue("Bottom Side resolution", bottomSide.aabb.xy.y == -bottomSide.aabb.height);
	}

	@Test
	public void testTotalBodyInsideBlock() {
		testTotalBodyInsideBlock(world);
	}

	public void testTotalBodyInsideBlock(BitWorld world) {
		world.setLevel(loadTestLevel("singleBlock.level"));
		BitBody body = world.createBody(7, 8, 15, 14, props.clone());
		BitBody bodyGoUp = world.createBody(7, 8, 15, 14, props.clone());
		bodyGoUp.props.velocity.y = 1;

		BitBody bodyGoLeft = world.createBody(7, 8, 15, 14, props.clone());
		bodyGoLeft.props.velocity.x = -1;

		BitBody bodyGoRight = world.createBody(7, 8, 15, 14, props.clone());
		bodyGoRight.props.velocity.x = 1;

		world.step(.01f);

		// Body's that aren't moving are resolved up
		assertTrue("Body Inside Block no velocity", body.aabb.xy.y == 32);
		assertTrue("Body Inside Block up velocity", bodyGoUp.aabb.xy.y == -bodyGoUp.aabb.height);
		assertTrue("Body Inside Split Block left velocity", bodyGoLeft.aabb.xy.x == 32);
		assertTrue("Body Inside Split Block right velocity", bodyGoRight.aabb.xy.x == -bodyGoRight.aabb.width);
	}

	@Test
	public void testTotalBodyInsideSplitBlock() {
		testTotalBodyInsideSplitBlock(world);
	}

	public void testTotalBodyInsideSplitBlock(BitWorld world) {
		world.setLevel(loadTestLevel("singleRow.level"));
		BitBody body = world.createBody(21, 6, 17, 19, props.clone());
		BitBody bodyRising = world.createBody(21, 6, 17, 19, props.clone());
		bodyRising.props.velocity.y = 1;

		BitBody bodyGoLeft = world.createBody(131, 89, 22, 18, props.clone());
		bodyGoLeft.props.velocity.x = -1;

		BitBody bodyGoRight = world.createBody(131, 89, 22, 18, props.clone());
		bodyGoRight.props.velocity.x = 1;

		world.step(.01f);

		// Body's that aren't moving are resolved up
		assertTrue("Body Inside Split Block no velocity", body.aabb.xy.y == 32);
		assertTrue("Body Inside Split Block up velocity", bodyRising.aabb.xy.y == -bodyRising.aabb.height);
		assertTrue("Body Inside Split Block right velocity", bodyGoRight.aabb.xy.x == 128 - bodyGoRight.aabb.width);
		assertTrue("Body Inside Split Block left velocity", bodyGoLeft.aabb.xy.x == 160);
	}

	// TODO write corner test case once we figure out if we want to take velocity into consideration
	@Test
	public void testCornerCollisions() {
		testCornerCollisions(world);
	}

	public void testCornerCollisions(BitWorld world) {
		world.setLevel(loadTestLevel("singleBlock.level"));
		BitBody fallingBody = world.createBody(30, 22, 6, 41, props.clone());
		fallingBody.props.velocity.y = -5;

		BitBody ascendingBody = world.createBody(30, -37, 20, 51, props.clone());
		ascendingBody.props.velocity.y = 5;

		world.step(.01f);

		assertTrue("Body dropping onto edge of platform", fallingBody.aabb.xy.y == 32);
		assertTrue("Body ascending into edge of platform", MathUtils.close(ascendingBody.aabb.xy.y, -ascendingBody.aabb.height));
	}

	public static Level loadTestLevel(String levelName) {
		return LevelUtilities.loadLevel("./assets/test/" + levelName);
	}

}
