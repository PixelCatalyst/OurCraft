package com.pixcat.gameplay;

public interface Subject {

    void addObserver(Observer toAdd);
    void removeObserver(Observer toRemove);

    void notifyAllObservers();
}
