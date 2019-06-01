package com.pixcat.voxel;

import com.pixcat.graphics.Texture;

import java.util.ArrayList;

public class AnimatedBlock extends SolidBlock {
    private ArrayList<Texture> frames;
    private int currentFrameIndex;
    private double secondsPerFrame;
    private double accumulatedTime;

    //added getters for testing
    public int getCurrentFrameIndex() {
        return currentFrameIndex;
    }

    public double getAccumulatedTime() {
        return accumulatedTime;
    }

    public ArrayList getFrameArray(){
        return frames;
    }

    public double getSecondsPerFrame(){
        return secondsPerFrame;
    }

    public AnimatedBlock(byte ID, String name, Texture firstFrame, double secondsPerFrame) {
        super(ID, name, firstFrame);
        frames = new ArrayList<>();
        addFrame(firstFrame);
        this.secondsPerFrame = Math.abs(secondsPerFrame);
        accumulatedTime = 0.0;
        currentFrameIndex = 0; // added
    }

    public void addFrame(Texture frameTexture) {
        frames.add(frameTexture);
    }

    public void update(double elapsedTime) { //prob need to add more textures with that addFrame()
        accumulatedTime += elapsedTime;
        if (accumulatedTime >= secondsPerFrame) {
            int frameDelta = (int) (accumulatedTime / secondsPerFrame);
            accumulatedTime -= frameDelta * secondsPerFrame;
            currentFrameIndex = (currentFrameIndex + frameDelta) % frames.size();
            texture = frames.get(currentFrameIndex);
        }
    }
}
