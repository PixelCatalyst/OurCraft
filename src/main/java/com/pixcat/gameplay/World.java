package com.pixcat.gameplay;

import com.pixcat.noisegen.TerrainGenerator;
import com.pixcat.voxel.SpatialStructure;
import com.pixcat.voxel.VirtualArray;

import java.util.ArrayList;

public class World implements Subject {
    private Camera playerCamera;
    private Metrics playerMetrics;
    private TerrainGenerator terrainGen;
    private SpatialStructure voxels;

    private ArrayList<Observer> observers;

    public World(String generatorSeed) {
        playerCamera = new Camera();
        playerMetrics = new Metrics();
        terrainGen = new TerrainGenerator(generatorSeed.hashCode());
        voxels = new VirtualArray(12);
        observers = new ArrayList<>();
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
}
