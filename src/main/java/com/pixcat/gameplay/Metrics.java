package com.pixcat.gameplay;

public class Metrics implements SubjectStatus {
    boolean initialStateFlag;
    double secondsInGame;

    public Metrics() {
        initialStateFlag = true;
        secondsInGame = 0.0f;
        //TODO rest of params
    }

    public Metrics(Metrics other) {
        initialStateFlag = true;
        //TODO copy constructor
    }

    public boolean isInitialState() {
        return initialStateFlag;
    }

    public double getSecondsInGame() {
        return secondsInGame;
    }

    public void addSecondsInGame(double secondsToAdd) {
        this.secondsInGame += secondsToAdd;
    }
}
