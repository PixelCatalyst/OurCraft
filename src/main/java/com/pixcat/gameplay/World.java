package com.pixcat.gameplay;

import com.pixcat.core.FileManager;
import com.pixcat.core.InputBuffer;
import com.pixcat.core.MouseAction;
import com.pixcat.graphics.Renderer;
import com.pixcat.graphics.Texture;
import com.pixcat.mesh.GreedyMesher;
import com.pixcat.mesh.Mesher;
import com.pixcat.noisegen.TerrainGenerator;
import com.pixcat.voxel.*;
import org.joml.*;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Iterator;

import static org.lwjgl.glfw.GLFW.*;

public class World implements Subject {
    private Camera playerCamera;
    private final float playerHeight;
    private Metrics playerMetrics;
    private TerrainGenerator terrainGen;
    private SpatialStructure voxels;
    private BlockCursor blockCursor;
    private Mesher mesher;

    private float velocity;
    private boolean jumping;

    private Vector2i playerChunkColumn;
    private Vector4i chunkArea;

    private ArrayList<Observer> observers;

    public World() {
        playerCamera = new Camera(0, 0, 0);
        playerHeight = 1.7f;
        playerMetrics = new Metrics();
        voxels = new VirtualArray(5);
        setupBlocks();
        blockCursor = new BlockCursor();
        mesher = new GreedyMesher();
        observers = new ArrayList<>();
    }

    private void setupBlocks() {
        FileManager fm = FileManager.getInstance();
        Block dirt = new SolidBlock((byte) 1, "dirt", fm.loadTexture("dirt.png"));
        Block grass = new SolidBlock((byte) 2, "grass", fm.loadTexture("grass.png"));
        Block stone = new SolidBlock((byte) 3, "stone", fm.loadTexture("stone.png"));
        Texture texWaterFirst = fm.loadTexture("water.png");
        Texture texWaterSecond = fm.loadTexture("water1.png");
        Texture texWaterThird = fm.loadTexture("water2.png");
        AnimatedBlock water = new AnimatedBlock((byte) 4, "water", texWaterFirst, 0.6);
        water.addFrame(texWaterSecond);
        water.addFrame(texWaterThird);
        water.addFrame(texWaterSecond);
        Block tree = new SolidBlock((byte) 5, "wood", fm.loadTexture("tree.png"));
        Block leaves = new SolidBlock((byte) 6, "leaves", fm.loadTexture("leaves.png"));
        voxels.addVoxelType(dirt);
        voxels.addVoxelType(grass);
        voxels.addVoxelType(stone);
        voxels.addVoxelType(water);
        voxels.addVoxelType(tree);
        voxels.addVoxelType(leaves);
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

    public void beginGeneration(String seed) {
        final int heightInChunks = voxels.getHeight();
        terrainGen = new TerrainGenerator(seed.hashCode(), heightInChunks);
        int diameter = voxels.getDiameter();
        int planeMin = -(diameter / 2);
        int planeMax = (diameter / 2);
        if ((diameter % 2) == 0)
            --planeMax;

        Chunk[] currColumn = new Chunk[heightInChunks];
        for (int i = 0; i < currColumn.length; ++i)
            currColumn[i] = null;
        for (int x = planeMin; x <= planeMax; ++x) {
            for (int z = planeMin; z <= planeMax; ++z) {
                int heightAtFirstBlock = terrainGen.fillColumn(currColumn, (x * Chunk.getSize()), (z * Chunk.getSize()));
                if ((x == 0) && (z == 0))
                    initPlayerPosition(heightAtFirstBlock);
                for (int y = 0; y < heightInChunks; ++y)
                    voxels.putChunk(y, x, z, currColumn[y]);
            }
        }
        chunkArea = new Vector4i(planeMin, planeMax, planeMin, planeMax);
    }

    private void initPlayerPosition(int height) {
        Vector3f playerPos = new Vector3f(1.5f, (float) height + playerHeight, 1.5f);
        playerCamera.setPosition(playerPos.x, playerPos.y, playerPos.z);
        playerChunkColumn = new Vector2i(
                (int) Math.ceil(playerPos.x) / Chunk.getSize(),
                (int) Math.ceil(playerPos.z) / Chunk.getSize());
    }

    public void rotatePlayer(float deltaX, float deltaY, float timeStep) {
        final float length = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        final float maxLength = 50.0f;
        if (length > maxLength) {
            deltaX = deltaX * (maxLength / length);
            deltaY = deltaY * (maxLength / length);
        }
        final float turnSpeed = 60.0f;
        final float maxFrameTime = 1.0f / 15.0f;
        float deltaAxisX = deltaY * turnSpeed * Math.min(timeStep, maxFrameTime);
        float deltaAxisY = deltaX * turnSpeed * Math.min(timeStep, maxFrameTime);
        playerCamera.moveRotation(deltaAxisX, deltaAxisY, 0.0f);
    }

    public void movePlayer(InputBuffer input, float timeStep) {
        final float moveSpeed = 3.2f;
        final float jumpSpeed = 6.0f;
        final float maxFrameTime = 1.0f / 15.0f;
        timeStep = Math.min(timeStep, maxFrameTime);
        Vector3f originalPosition = playerCamera.getPosition();
        float gravity = -0.2f * timeStep;
        velocity += gravity;
        playerCamera.movePosition(0.0f, velocity, 0.0f);

        if (input.isKeyboardKeyDown(GLFW_KEY_W))
            playerCamera.movePosition(0, 0, -moveSpeed * timeStep);
        if (input.isKeyboardKeyDown(GLFW_KEY_S))
            playerCamera.movePosition(0, 0, moveSpeed * timeStep);

        if (input.isKeyboardKeyDown(GLFW_KEY_A))
            playerCamera.movePosition(-moveSpeed * timeStep, 0, 0);
        if (input.isKeyboardKeyDown(GLFW_KEY_D))
            playerCamera.movePosition(moveSpeed * timeStep, 0, 0);

        if (input.isKeyboardKeyDown(GLFW_KEY_SPACE) || jumping) {
            playerCamera.movePosition(0, jumpSpeed * timeStep, 0);
            jumping = true;
        }

        handleCollisions(originalPosition);
    }

    private void handleCollisions(Vector3f originalPosition) {
        Vector3f playerPosition = playerCamera.getPosition();
        boolean xCollision = isCollisionAt(new Vector3f(playerPosition.x, originalPosition.y, originalPosition.z));
        boolean yCollision = isCollisionAt(new Vector3f(originalPosition.x, playerPosition.y, originalPosition.z));
        boolean zCollision = isCollisionAt(new Vector3f(originalPosition.x, originalPosition.y, playerPosition.z));
        playerCamera.setPosition(
                (xCollision ? originalPosition.x : playerPosition.x),
                (yCollision ? originalPosition.y : playerPosition.y),
                (zCollision ? originalPosition.z : playerPosition.z));

        if (yCollision && (playerPosition.y < originalPosition.y)) {
            velocity = 0.0f;
            jumping = false;
        } else if (yCollision)
            velocity -= (playerPosition.y - originalPosition.y);

        Vector3f positionChange = playerCamera.getPosition()
                .sub(originalPosition)
                .absolute();
        //TODO positionChange -> achievements
    }

    private boolean isCollisionAt(Vector3f playerPosition) {
        Vector3i headPos = quantizeToVoxelSpace(playerPosition);
        playerPosition.y -= playerHeight;
        Vector3i legsPos = quantizeToVoxelSpace(playerPosition);
        final int chunkSize = Chunk.getSize();
        while (headPos.y >= legsPos.y) {
            Chunk currentChunk = voxels.getChunk(
                    (headPos.y >= 0 ? headPos.y / chunkSize : (headPos.y - chunkSize + 1) / chunkSize),
                    (headPos.x >= 0 ? headPos.x / chunkSize : (headPos.x - chunkSize + 1) / chunkSize),
                    (headPos.z >= 0 ? headPos.z / chunkSize : (headPos.z - chunkSize + 1) / chunkSize));
            if (currentChunk != null) {
                byte currentID = currentChunk.getVoxelID(
                        Math.floorMod(headPos.y, chunkSize),
                        Math.floorMod(headPos.x, chunkSize),
                        Math.floorMod(headPos.z, chunkSize));
                if (currentID != 0)
                    return true;
            }
            headPos.y -= 1;
        }
        return false;
    }

    private Vector3i quantizeToVoxelSpace(Vector3f position) {
        return new Vector3i(
                (position.x < 0.0f ? (int) Math.floor(position.x) : (int) (position.x)),
                (position.y < 0.0f ? (int) Math.floor(position.y) : (int) (position.y)),
                (position.z < 0.0f ? (int) Math.floor(position.z) : (int) (position.z)));
    }

    public void updateChunks() {
        Vector3f playerPos = playerCamera.getPosition();
        double roundedX = (playerPos.x > 0.0 ?
                Math.ceil(playerPos.x) :
                Math.floor(playerPos.x) - (double) Chunk.getSize());
        double roundedZ = (playerPos.z > 0.0 ?
                Math.ceil(playerPos.z) :
                Math.floor(playerPos.z) - (double) Chunk.getSize());
        Vector2i chunkPos = new Vector2i((int) roundedX / Chunk.getSize(), (int) roundedZ / Chunk.getSize());
        Vector2i chunkDiff = new Vector2i(chunkPos).sub(playerChunkColumn);
        playerChunkColumn = chunkPos;
        Vector4i movedChunkArea = new Vector4i(
                chunkArea.x + chunkDiff.x,
                chunkArea.y + chunkDiff.x,
                chunkArea.z + chunkDiff.y,
                chunkArea.w + chunkDiff.y);

        Chunk[] currColumn = new Chunk[voxels.getHeight()];
        generateChunksOnX(chunkDiff, movedChunkArea, currColumn);
        generateChunksOnZ(chunkDiff, movedChunkArea, currColumn);
    }

    private void generateChunksOnX(Vector2i chunkDiff, Vector4i movedChunkArea, Chunk[] currColumn) {
        int x = 0;
        int xMax = -1;
        if (chunkDiff.x < 0) {
            x = movedChunkArea.x;
            xMax = chunkArea.x;
        } else if (chunkDiff.x > 0) {
            x = chunkArea.y + 1;
            xMax = movedChunkArea.y + 1;
        }
        for (; x < xMax; ++x) {
            for (int z = chunkArea.z; z <= chunkArea.w; ++z)
                generateColumn(x, z, currColumn);
        }
        chunkArea.x = movedChunkArea.x;
        chunkArea.y = movedChunkArea.y;
    }

    private void generateChunksOnZ(Vector2i chunkDiff, Vector4i movedChunkArea, Chunk[] currColumn) {
        int z = 0;
        int zMax = -1;
        if (chunkDiff.y < 0) {
            z = movedChunkArea.z;
            zMax = chunkArea.z;
        } else if (chunkDiff.y > 0) {
            z = chunkArea.w + 1;
            zMax = movedChunkArea.w + 1;
        }
        for (; z < zMax; ++z) {
            for (int x = chunkArea.x; x <= chunkArea.y; ++x)
                generateColumn(x, z, currColumn);
        }
        chunkArea.z = movedChunkArea.z;
        chunkArea.w = movedChunkArea.w;
    }

    private void generateColumn(int x, int z, Chunk[] workingColumn) {
        terrainGen.fillColumn(workingColumn, (x * Chunk.getSize()), (z * Chunk.getSize()));
        for (int y = 0; y < voxels.getHeight(); ++y)
            voxels.putChunk(y, x, z, workingColumn[y]);
    }

    public void addGameTime(double elapsedTime) {
        playerMetrics.addSecondsInGame(elapsedTime);
        voxels.updateBlocks(elapsedTime);
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

    public Camera getPlayerCamera() {
        return playerCamera;
    }

    public void updateBlockCursor(MouseAction mouseAction, double scrollOffset) {
        blockCursor.scrollType((int) scrollOffset, voxels);
        Vector3f position = playerCamera.getPosition();
        Vector3f direction = playerCamera.getDirection();
        blockCursor.castRay(position, direction, voxels);
        if (mouseAction.getEvent() == MouseAction.Event.PRESS) {
            if (mouseAction.getButton() == MouseAction.Button.LEFT) {
                byte deletedBlockID = blockCursor.deleteCurrentBlock(voxels);
                //TODO ID -> achievements
            } else if (mouseAction.getButton() == MouseAction.Button.RIGHT)
                blockCursor.placeNewBlock(voxels);
        }
    }

    public void drawChunks(Renderer renderer) {
        Texture[] materials = new Texture[voxels.getTypeCount()];
        for (byte i = 1; i <= materials.length; ++i)
            materials[i - 1] = voxels.getVoxelFromID(i).getTexture();
        ArrayList<Chunk> chunks = playerCamera.getVisibleChunks(voxels);

        Iterator<Chunk> iterator = chunks.iterator();
        for (int i = 0; i < chunks.size(); ++i) {
            Chunk chunk = iterator.next();
            if (chunk.needRebuild()) {
                Coord3Int position = chunk.getWorldPosition();
                NeumannNeighborhood chunkWithNeighbors = new NeumannNeighborhood(voxels, position);
                chunk.applyBuild(mesher, chunkWithNeighbors, materials);
            }
            renderer.draw(chunk.getGraphic());
        }
        blockCursor.draw(renderer);
    }

    public void drawStatusBar(Renderer renderer) {
        blockCursor.drawCrosshair(renderer);
        blockCursor.drawMaterialBar(renderer, voxels);
    }
}
