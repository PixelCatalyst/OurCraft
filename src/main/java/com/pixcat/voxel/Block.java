package com.pixcat.voxel;

public interface Block {

    byte getID();

    String getName();

    //TODO getTexture (...)

    default void update(double elapsedTime) {
        //Do nothing
    }
}
