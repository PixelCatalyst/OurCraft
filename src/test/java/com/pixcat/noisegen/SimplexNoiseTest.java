package com.pixcat.noisegen;

import com.pixcat.core.FileManager;
import com.pixcat.graphics.Texture;
import com.pixcat.graphics.Window;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import static org.lwjgl.glfw.GLFW.glfwInit;

public class SimplexNoiseTest {
    @Test
    public void testValueWithNegativeSeed(){
        SimplexNoise simplexNoise1 = new SimplexNoise(-100);
        SimplexNoise simplexNoise2 = new SimplexNoise(-100);
        assertEquals(simplexNoise1.getValue(1,1), simplexNoise2.getValue(1,1),0.0);
    }

    @Test
    public void testValueWithSameSeed(){
        SimplexNoise simplexNoise1 = new SimplexNoise(100);
        SimplexNoise simplexNoise2 = new SimplexNoise(100);
        assertEquals(simplexNoise1.getValue(1,1), simplexNoise2.getValue(1,1),0.0);
    }

    @Test
    public void testValueWithDifferentSeed(){
        SimplexNoise simplexNoise1 = new SimplexNoise(100);
        SimplexNoise simplexNoise2 = new SimplexNoise(150);
        assertNotEquals(simplexNoise1.getValue(1,1), simplexNoise2.getValue(1,1),0.0);
    }


    @Test // value in [0,1] in some random seeds
    public void testGetValue(){
        for(int i = -1000000; i < 1000000; i+=51231) {
            SimplexNoise simplexNoise1 = new SimplexNoise(i);
            assertTrue(simplexNoise1.getValue(0, 0) >= 0 && (simplexNoise1.getValue(0, 0)) <= 1);
            assertTrue(simplexNoise1.getValue(-1, -1) >= 0 && (simplexNoise1.getValue(-1, -1)) <= 1);
            assertTrue(simplexNoise1.getValue(100, 100) >= 0 && (simplexNoise1.getValue(100, 100)) <= 1);
            assertTrue(simplexNoise1.getValue(500, 0) >= 0 && (simplexNoise1.getValue(500, 0)) <= 1);
            assertTrue(simplexNoise1.getValue(0, 1000) >= 0 && (simplexNoise1.getValue(0, 1000)) <= 1);
            assertTrue(simplexNoise1.getValue(10000000, 500000) >= 0 && (simplexNoise1.getValue(10000000, 500000)) <= 1);
        }
    }
}
