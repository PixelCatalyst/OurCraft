package com.pixcat.gameplay;

import org.joml.Vector3f;

public class Metrics implements SubjectStatus {
    private boolean initialStateFlag;

    private double secondsInGame;
    private int dirtBlocksDug;
    private float blocksWalked;

    public Metrics() {
        initialStateFlag = true;
    }

    public Metrics(Metrics other) {
        this.initialStateFlag = other.initialStateFlag;
        this.secondsInGame = other.secondsInGame;
        this.dirtBlocksDug = other.dirtBlocksDug;
        this.blocksWalked = other.blocksWalked;
    }

    public Metrics(double secondsInGame, int dirtBlocksDug, float blocksWalked) {
        initialStateFlag = true;
        this.secondsInGame = secondsInGame;
        this.dirtBlocksDug = dirtBlocksDug;
        this.blocksWalked = blocksWalked;
    }

    public boolean isInitialState() {
        return initialStateFlag;
    }

    public double getSecondsInGame() {
        return secondsInGame;
    }

    public void addSecondsInGame(double secondsToAdd) {
        this.secondsInGame += secondsToAdd;
        initialStateFlag = false;
    }

    public int getDirtBlocksDug() {
        return dirtBlocksDug;
    }

    public void addDugBlock(byte blockID) {
        final byte dirtID = 1;
        if (blockID == dirtID) {
            ++dirtBlocksDug;
            initialStateFlag = false;
        }
    }

    public float getBlocksWalked() {
        return blocksWalked;
    }

    public void addPositionChange(Vector3f positionChange) {
        Vector3f localPositionChange = new Vector3f(positionChange.x, 0.0f, positionChange.z);
        float distance = localPositionChange.length();
        blocksWalked += distance;
        if (distance > 0.0f)
            initialStateFlag = false;
    }
}
