package com.pixcat.voxel;

public class AnimatedBlock extends SolidBlock {
    private double secondsPerFrame;

    public AnimatedBlock(byte id, String name, double secondsPerFrame) {
        super(id, name);
        this.secondsPerFrame = Math.abs(secondsPerFrame);
    }

    //TODO animation frames (textures...)

    public void update(double elapsedTime) {
        //TODO change frames
    }
}
