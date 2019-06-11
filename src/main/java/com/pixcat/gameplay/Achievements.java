package com.pixcat.gameplay;

import com.pixcat.core.FileManager;
import com.pixcat.graphics.Renderer;
import com.pixcat.graphics.gui.GUIFactory;
import com.pixcat.graphics.gui.StaticImage;

import java.util.LinkedList;
import java.util.Queue;

public class Achievements implements Observer {
    private StaticImage dirtAchImg;
    private StaticImage dayAchImg;
    private StaticImage walkAchImg;

    private static class AwardImage {
        StaticImage img;
        double TTL;

        public AwardImage(StaticImage img, double TTL) {
            this.img = img;
            this.TTL = TTL;
        }
    }

    Queue<AwardImage> awardQueue;

    private final int dirtCriterion;
    private final double dayCriterion;
    private final float walkCriterion;
    private boolean dirtAchAwarded;
    private boolean dayAchAwarded;
    private boolean walkAchAwarded;
    //Times to live for showed achievement, must be in seconds
    private final double defaultTTL;

    public Achievements() {
        GUIFactory gui = GUIFactory.getInstance();
        FileManager fm = FileManager.getInstance();
        dirtAchImg = gui.makeImage(fm.loadTexture("ach_dirt.png"), null, null);
        dayAchImg = gui.makeImage(fm.loadTexture("ach_day.png"), null, null);
        walkAchImg = gui.makeImage(fm.loadTexture("ach_walk.png"), null, null);

        awardQueue = new LinkedList<>();

        dirtCriterion = 20;
        dayCriterion = 20.0f * 60.0f; //20 minutes times 60 seconds
        walkCriterion = 50.0f;
        defaultTTL = 5.0f;
    }

    public void onUpdate(SubjectStatus status) {
        Metrics playerMetrics = (Metrics) status;
        if (playerMetrics.isInitialState()) {
            dirtAchAwarded = (playerMetrics.getDirtBlocksDug() >= dirtCriterion);
            dayAchAwarded = (playerMetrics.getSecondsInGame() >= dayCriterion);
            walkAchAwarded = (playerMetrics.getBlocksWalked() >= walkCriterion);
        } else {
            if (!dirtAchAwarded) {
                dirtAchAwarded = (playerMetrics.getDirtBlocksDug() >= dirtCriterion);
                if (dirtAchAwarded)
                    awardQueue.add(new AwardImage(dirtAchImg, defaultTTL));
            }

            if (!dayAchAwarded) {
                dayAchAwarded = (playerMetrics.getSecondsInGame() >= dayCriterion);
                if (dayAchAwarded)
                    awardQueue.add(new AwardImage(dayAchImg, defaultTTL));
            }

            if (!walkAchAwarded) {
                walkAchAwarded = (playerMetrics.getBlocksWalked() >= walkCriterion);
                if (walkAchAwarded)
                    awardQueue.add(new AwardImage(walkAchImg, defaultTTL));
            }
        }
    }

    public void updateTimeToLive(double elapsedTime) {
        if (awardQueue.isEmpty() == false) {
            AwardImage currentAward = awardQueue.peek();
            if (currentAward.TTL > 0.0)
                currentAward.TTL -= elapsedTime;
            if (currentAward.TTL <= 0.0)
                awardQueue.poll();
        }
    }

    public void draw(Renderer renderer) {
        if (awardQueue.isEmpty() == false) {
            StaticImage currentImg = awardQueue.peek().img;
            currentImg.setPosition(renderer.getWindowWidth() - currentImg.getWidth(), 0);
            renderer.draw(currentImg);
        }
    }
}
