package com.pixcat.core;

import com.pixcat.gameplay.Metrics;
import com.pixcat.gameplay.WorldInfo;
import com.pixcat.graphics.Texture;
import com.pixcat.graphics.Window;
import com.pixcat.voxel.ArrayChunk;
import com.pixcat.voxel.Chunk;
import com.pixcat.voxel.Coord3Int;
import org.joml.Vector3f;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.*;
import static org.lwjgl.glfw.GLFW.glfwInit;

public class FileManagerTest {
    private FileManager testFileManager;
    private Window placeholderWindow;

    @Before
    public void setUp() {
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        placeholderWindow = new Window(200, 200, "placeholder");
        placeholderWindow.bindAsCurrent();

        testFileManager = FileManager.getInstance();
    }

    @Test
    public void testLoadText() {
        String empty = testFileManager.loadText("non existing file");

        assertEquals("", empty);

        String normalFile = testFileManager.loadText("vertex.vs");

        assertTrue(normalFile.length() > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testLoadTextureNotExist() {
        Texture empty = testFileManager.loadTexture("non existing file");
    }

    @Test
    public void testLoadTexture() {
        Texture normalFile = testFileManager.loadTexture("dirt.png");

        assertNotNull(normalFile);
        assertNotEquals(0, normalFile.getID());
        assertFalse(normalFile.getWidth() <= 0);
        assertFalse(normalFile.getHeight() <= 0);
    }

    @Test
    public void testSaveAndLoadWorldInfo() {
        WorldInfo info = new WorldInfo();
        info.playerMetrics = new Metrics(10.0, 13, 123.0f);
        info.playerPos = new Vector3f(5.0f, 4.0f, 3.0f);
        info.playerRot = new Vector3f(10.0f, 1.0f, 111.0f);
        info.seed = new String("random seed");

        testFileManager.clearSaves();
        testFileManager.saveWorldInfo(info);

        assertTrue(testFileManager.existSavedWorld());

        WorldInfo loadedInfo = testFileManager.loadWorldInfo();

        assertEquals(info.playerMetrics.getSecondsInGame(),
                loadedInfo.playerMetrics.getSecondsInGame(), Double.MIN_NORMAL);
        assertEquals(info.playerMetrics.getDirtBlocksDug(), loadedInfo.playerMetrics.getDirtBlocksDug());
        assertEquals(info.playerMetrics.getBlocksWalked(),
                loadedInfo.playerMetrics.getBlocksWalked(), Float.MIN_NORMAL);

        assertEquals(info.playerPos.x, loadedInfo.playerPos.x, Float.MIN_NORMAL);
        assertEquals(info.playerPos.y, loadedInfo.playerPos.y, Float.MIN_NORMAL);
        assertEquals(info.playerPos.z, loadedInfo.playerPos.z, Float.MIN_NORMAL);

        assertEquals(info.playerRot.x, loadedInfo.playerRot.x, Float.MIN_NORMAL);
        assertEquals(info.playerRot.y, loadedInfo.playerRot.y, Float.MIN_NORMAL);
        assertEquals(info.playerRot.z, loadedInfo.playerRot.z, Float.MIN_NORMAL);

        assertEquals(info.seed, loadedInfo.seed);

        testFileManager.clearSaves();
    }

    @Test
    public void testSerializeAndDeserializeChunk() {
        testFileManager.clearSaves();
        Coord3Int coord = new Coord3Int(3, 1, 2);
        Chunk expectedChunk = new ArrayChunk();
        expectedChunk.setWorldPosition(coord);
        for (int y = 0; y < Chunk.getSize(); ++y) {
            for (int x = 0; x < Chunk.getSize(); ++x) {
                for (int z = 0; z < Chunk.getSize(); ++z) {
                    expectedChunk.setVoxelID(y, x, z, (byte) (Math.random() * 111));
                }
            }
        }

        testFileManager.serializeChunkToDisk(expectedChunk);
        Chunk actualChunk = testFileManager.deserializeChunkFromDisk(coord);

        for (int y = 0; y < Chunk.getSize(); ++y) {
            for (int x = 0; x < Chunk.getSize(); ++x) {
                for (int z = 0; z < Chunk.getSize(); ++z) {
                    assertEquals(expectedChunk.getVoxelID(y, x, z), actualChunk.getVoxelID(y, x, z));
                }
            }
        }

        testFileManager.clearSaves();
    }

    @Test
    public void testDeserializeNonExistingChunk() {
        testFileManager.clearSaves();
        Chunk actualChunk = testFileManager.deserializeChunkFromDisk(new Coord3Int(9, 9, 7));

        assertNull(actualChunk);
    }

    @Test
    public void testGetInstance() {
        final FileManager firstInstance = FileManager.getInstance();
        final FileManager secondInstance = FileManager.getInstance();
        final FileManager thirdInstance = FileManager.getInstance();

        assertNotNull(testFileManager);
        assertNotNull(firstInstance);
        assertNotNull(secondInstance);
        assertNotNull(thirdInstance);
        assertEquals(testFileManager, firstInstance);
        assertEquals(testFileManager, secondInstance);
        assertEquals(testFileManager, thirdInstance);
        assertEquals(firstInstance, secondInstance);
        assertEquals(firstInstance, thirdInstance);
        assertEquals(secondInstance, thirdInstance);
    }

    @After
    public void tearDown() {
        placeholderWindow.destroy();
    }
}
