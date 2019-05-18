package com.pixcat.gameplay;

import com.pixcat.noisegen.TerrainGenerator;
import com.pixcat.voxel.SpatialStructure;
import com.pixcat.voxel.VirtualArray;
import org.joml.Vector3f;

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

    public void addGameTime(double elapsedTime) {
        playerMetrics.addSecondsInGame(elapsedTime);
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
}
