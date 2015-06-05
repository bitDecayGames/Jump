package bitDecayJump.test;

import static org.junit.Assert.assertTrue;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.io.*;
import java.util.Set;

import javax.swing.*;

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
		java.util.List<Failure> failures = results.getFailures();
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
		File testDirectory = getTestDirectory();
		File[] filteredFiles = testDirectory.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".test");
			}
		});
		System.out.println(filteredFiles.length + " test case files found.");
		for (File testCase : filteredFiles) {
			runTestFile(testCase);
		}
	}

	private static void runTestFile(File testFile) {
		System.out.println(testFile.getName());
		BitDecayTestCase testCase = FileUtils.loadFileAs(BitDecayTestCase.class, testFile);
		BitWorld world = new BitWorld();
		world.setLevel(testCase.level);
		for (BitBody testBody : testCase.bodies) {
			world.addBody(testBody);
		}
	}

	private static File getTestDirectory() {
		JFileChooser fileChooser = new JFileChooser() {
			@Override
			protected JDialog createDialog(Component parent) throws HeadlessException {
				JDialog dialog = super.createDialog(parent);
				dialog.setAlwaysOnTop(true);
				dialog.setModalityType(ModalityType.APPLICATION_MODAL);
				dialog.setModal(true);
				return dialog;
			}
		};
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setDialogTitle("Select Test Case Directory");
		fileChooser.setApproveButtonText("Select");
		fileChooser.setCurrentDirectory(new File("."));
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		} else {
			System.out.println("No test directory selected. Terminating.");
			System.exit(0);
		}
		return null;
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
