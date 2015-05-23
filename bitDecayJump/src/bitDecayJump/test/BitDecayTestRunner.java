package bitDecayJump.test;

import static org.junit.Assert.assertTrue;

import java.util.*;

import org.junit.runner.*;
import org.junit.runner.notification.Failure;
import org.reflections.Reflections;

import bitDecayJump.*;
import bitDecayJump.geom.MathUtils;
import bitDecayJump.level.*;

public class BitDecayTestRunner {
	/*
	 * The idea: 1) use annotations 2) all tests should take a world
	 */
	BitWorld world;
	BitBodyProps props;

	public static void main(String[] args) {
		Reflections reflections = new Reflections("");
		Set<Class<?>> testClasses = reflections.getTypesAnnotatedWith(BitDecayTest.class);
		System.out.println("Running tests for " + testClasses.size() + " classes");
		Result results = JUnitCore.runClasses(testClasses.toArray(new Class[testClasses.size()]));
		List<Failure> failures = results.getFailures();
		if (failures.size() > 0) {
			System.out.println(failures.size() + " tests failed");
			for (Failure failure : failures) {
				try {
					Description description = failure.getDescription();
					System.out.println(description.getMethodName());
					System.out.println(Class.forName(description.getClassName()).getMethod(description.getMethodName(), BitWorld.class));
					// From here we can invoke it with a built / renderable world to show the problem.
				} catch (NoSuchMethodException | SecurityException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		//		Set<Method> testMethods = reflections.getMethodsAnnotatedWith(BitDecayTest.class);
		//		for (Method test : testMethods) {
		//			test.invoke()
		//		}
	}

	public void setUp() throws Exception {
		world = new BitWorld();
		props = new BitBodyProps();
		props.bodyType = BodyType.DYNAMIC;
		props.gravitational = false;
	}

	@BitDecayTest
	public void simpleCollisionTest(BitWorld world) {
		world.removeAllBodies();
		world.setLevel(loadTestLevel("singleBlock.level"));
		BitBody fallingBody = world.createBody(30, 22, 6, 41, props);
		fallingBody.props.velocity.y = -5;

		BitBody ascendingBody = world.createBody(30, -37, 20, 51, props);
		ascendingBody.props.velocity.y = 5;

		world.step(.01f);

		assertTrue("Body dropping onto edge of platform", fallingBody.aabb.xy.y == 32);
		assertTrue("Body ascending into edge of platform", MathUtils.close(ascendingBody.aabb.xy.y, -ascendingBody.aabb.height));
	}

	public static Level loadTestLevel(String levelName) {
		return LevelUtilities.loadLevel("./assets/test/" + levelName);
	}
}
