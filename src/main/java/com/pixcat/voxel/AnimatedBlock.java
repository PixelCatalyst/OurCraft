package com.pixcat.voxel;

import com.pixcat.graphics.Texture;

import java.util.ArrayList;

public class AnimatedBlock extends SolidBlock {
    private ArrayList<Texture> frames;
    private int currentFrameIndex;
    private double secondsPerFrame;
    private double accumulatedTime;

    public AnimatedBlock(byte ID, String name, Texture firstFrame, double secondsPerFrame) {
        super(ID, name, firstFrame);
        frames = new ArrayList<>();
        addFrame(firstFrame);
        this.secondsPerFrame = Math.abs(secondsPerFrame);
        accumulatedTime = 0.0;
    }

    public void addFrame(Texture frameTexture) {
        frames.add(frameTexture);
    }

    public void update(double elapsedTime) {
        accumulatedTime += elapsedTime;
        if (accumulatedTime >= secondsPerFrame) {
            int frameDelta = (int) (accumulatedTime / secondsPerFrame);
            accumulatedTime -= frameDelta * secondsPerFrame;
            currentFrameIndex = (currentFrameIndex + frameDelta) % frames.size();
            texture = frames.get(currentFrameIndex);
        }
    }
}
