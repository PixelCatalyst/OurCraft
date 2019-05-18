package com.pixcat.voxel;

import com.pixcat.graphics.Texture;

public interface Block {

    byte getID();

    String getName();

    Texture getTexture();

    default void update(double elapsedTime) {
        //Do nothing
    }
}
