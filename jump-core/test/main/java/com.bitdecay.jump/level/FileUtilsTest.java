package com.bitdecay.jump.level;

import com.bitdecay.jump.geom.BitPoint;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;

/**
 * Created by Monday on 6/14/2016.
 */
public class FileUtilsTest {
    @Test
    public void testLoadFile() {
        File testFile = new File(this.getClass().getResource("/simpleTextFile").getFile());
        String contents = FileUtils.loadFile(testFile);
        assertTrue(contents.equals("simpleTextFileContents"));
    }

    @Test
    public void testLoadEmptyFile() {
        File testFile = new File(this.getClass().getResource("/emptyFile").getFile());
        String contents = FileUtils.loadFile(testFile);
        assertTrue(contents == null);
    }

    @Test
    public void testInvalidFile() {
        File testFile = new File("ThisDoesntExist");
        String contents = FileUtils.loadFile(testFile);
        assertTrue(contents == null);
    }

    @Test
    public void testSaveLoad() {
        BitPoint objIn = new BitPoint();
        String objJson = FileUtils.toJson(objIn);
        assertTrue("Object serializes to string", objJson != null);

        BitPoint objOut = FileUtils.loadFileAs(BitPoint.class, objJson);
        assertTrue(objOut.equals(objIn));
    }
}
