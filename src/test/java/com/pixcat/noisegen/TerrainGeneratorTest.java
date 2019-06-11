package com.pixcat.noisegen;

import com.pixcat.voxel.ArrayChunk;
import com.pixcat.voxel.Chunk;
import org.junit.Test;

import java.util.Random;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class TerrainGeneratorTest {

    @Test
    public void testFillColumn() {
        Random randomSeed = new Random();
        for (int i = 0; i < 50; ++i) {
            //50 random seeds - generator needs to work with randomness all the time
            testColumnForSeed(randomSeed.nextInt());
        }
    }

    private void testColumnForSeed(int seed) {
        TerrainGenerator testGenerator = new TerrainGenerator(seed, 8);
        Chunk[] testColumn = new ArrayChunk[8];

        for (int x = 0; x < 56780; x += 5678) {
            for (int z = 0; z < 93720; z += 9372) {
                for (int y = 0; y < 8; ++y)
                    testColumn[y] = null;
                int height = testGenerator.fillColumn(testColumn, (x * Chunk.getSize()), (z * Chunk.getSize()));

                assertTrue((height > 50) && (height < (8 * Chunk.getSize())));
                for (int y = 0; y < 8; ++y)
                    assertNotNull(testColumn[y]);
            }
        }
    }

    @Test
    public void testFillColumnWhenColumnAlreadyFilled() {
        TerrainGenerator testGenerator = new TerrainGenerator(2123, 8);
        Chunk[] testColumn = new ArrayChunk[8];

        for (int x = 0; x < 156780; x += 15678) {
            for (int z = 0; z < 193720; z += 19372) {
                for (int y = 0; y < 8; ++y)
                    testColumn[y] = null;
                int height = testGenerator.fillColumn(testColumn, (x * Chunk.getSize()), (z * Chunk.getSize()));

                for (int y = 0; y < 8; ++y)
                    assertNotNull(testColumn[y]);

                Chunk[] columnCopy = new ArrayChunk[testColumn.length];
                System.arraycopy(testColumn, 0, columnCopy, 0, 8);
                int sameHeight = testGenerator.fillColumn(testColumn, (x * Chunk.getSize()), (z * Chunk.getSize()));

                for (int y = 0; y < 8; ++y)
                    assertEquals(columnCopy[y], testColumn[y]);

                assertEquals(height, sameHeight);
                assertTrue((height > 50) && (height < (8 * Chunk.getSize())));

            }
        }
    }
}
