package com.pixcat.gameplay;

import com.pixcat.core.FileManager;
import com.pixcat.graphics.Texture;
import com.pixcat.noisegen.TerrainGenerator;
import com.pixcat.voxel.*;
import com.pixcat.mesh.Mesher;
import com.pixcat.mesh.MarchMesher;
import com.pixcat.graphics.Renderer;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Iterator;

public class World implements Subject {
    private Camera playerCamera;
    private Metrics playerMetrics;
    private TerrainGenerator terrainGen;
    private SpatialStructure voxels;
    private Mesher mesher;

    private ArrayList<Observer> observers;

    public World(String generatorSeed) {
        playerCamera = new Camera();
        playerMetrics = new Metrics();
        terrainGen = new TerrainGenerator(generatorSeed.hashCode());
        voxels = new VirtualArray(12);
        setupBlocks();
        mesher = new MarchMesher();
        observers = new ArrayList<>();


        //TODO TEST
        Chunk test = new ArrayChunk();
        for (int i = 0; i < test.getSize(); ++i) {
            for (int j = 0; j < test.getSize(); ++j) {
                for (int k = 0; k < test.getSize(); ++k) {
                    //double r = random();
                    //byte id = (r > 0.5 ? (byte) 1 : (byte) 0);
                    test.setVoxelID(i, j, k, (byte) 1);
                }
            }
        }
        voxels.putChunk(0, 0, 0, test);
    }

    private void setupBlocks() {
        FileManager fm = FileManager.getInstance();
        Block dirt = new SolidBlock((byte) 1, "dirt", fm.loadTexture("dirt.png"));
        Block grass = new SolidBlock((byte) 2, "grass", fm.loadTexture("grass.png"));
        Block stone = new SolidBlock((byte) 3, "stone", fm.loadTexture("stone.png"));
        Texture texWaterFirst = fm.loadTexture("water.png");
        Texture texWaterSecond = fm.loadTexture("water1.png");
        Texture texWaterThird = fm.loadTexture("water2.png");
        AnimatedBlock water = new AnimatedBlock((byte) 4, "water", texWaterFirst, 0.6);
        water.addFrame(texWaterSecond);
        water.addFrame(texWaterThird);
        water.addFrame(texWaterSecond);
        Block tree = new SolidBlock((byte) 5, "wood", fm.loadTexture("tree.png"));
        Block leaves = new SolidBlock((byte) 6, "leaves", fm.loadTexture("leaves.png"));
        voxels.addVoxelType(dirt);
        voxels.addVoxelType(grass);
        voxels.addVoxelType(stone);
        voxels.addVoxelType(water);
        voxels.addVoxelType(tree);
        voxels.addVoxelType(leaves);
    }

    public void addObserver(Observer toAdd) {
        if (observers.contains(toAdd) == false)
            observers.add(toAdd);
    }

    public void removeObserver(Observer toRemove) {
        observers.remove(toRemove);
    }

    public void notifyAllObservers() {
        Metrics playerMetricsCopy = new Metrics(playerMetrics);
        for (Observer ob : observers)
            ob.onUpdate(playerMetricsCopy);
    }

    public void addGameTime(double elapsedTime) {
        playerMetrics.addSecondsInGame(elapsedTime);
        voxels.updateBlocks(elapsedTime);
    }

    public Vector3f getSkyColor() {
        final double dayTime = 10.0;
        final double duskTime = 10.5;
        final double nightTime = 19.5;
        final double fullDayTime = 20.0;
        final Vector3f daySky = new Vector3f(0.51f, 0.79f, 1.0f);
        final Vector3f nightSky = new Vector3f(0.16f, 0.21f, 0.32f);
        double currDayMinutes = (playerMetrics.getSecondsInGame() / 60.0) % fullDayTime;
        if (currDayMinutes < dayTime)
            return daySky;
        else if (currDayMinutes <= duskTime) {
            float alpha = (float) (currDayMinutes - dayTime) / (float) (duskTime - dayTime);
            return daySky.lerp(nightSky, alpha);
        } else if (currDayMinutes < nightTime) {
            return nightSky;
        } else {
            float alpha = (float) (currDayMinutes - nightTime) / (float) (fullDayTime - nightTime);
            return nightSky.lerp(daySky, alpha);
        }
    }

    public Camera getPlayerCamera() {
        return playerCamera;
    }

    public void drawChunks(Renderer renderer) {
        Texture[] materials = new Texture[voxels.getTypeCount()];
        for (byte i = 1; i <= materials.length; ++i)
            materials[i - 1] = voxels.getVoxelFromID(i).getTexture();
        ArrayList<Chunk> chunks = playerCamera.getVisibleChunks(voxels);

        Iterator<Chunk> iterator = chunks.iterator();
        for (int i = 0; i < chunks.size(); ++i) {
            Chunk ch = iterator.next();
            ch.build(mesher, materials);
            renderer.draw(ch.getGraphic());
        }
    }

    public void drawStatusBar(Renderer renderer) {
        //TODO
    }
}
