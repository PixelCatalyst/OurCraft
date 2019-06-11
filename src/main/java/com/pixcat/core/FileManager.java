package com.pixcat.core;

import com.pixcat.gameplay.Metrics;
import com.pixcat.gameplay.WorldInfo;
import com.pixcat.graphics.Texture;
import com.pixcat.voxel.ArrayChunk;
import com.pixcat.voxel.Chunk;
import com.pixcat.voxel.Coord3Int;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBImage.*;

public class FileManager {
    private static FileManager instance = null;

    private String rootDir;
    private String saveDir;

    private FileManager() {
        rootDir = "resources" + File.separator;
        saveDir = "saved" + File.separator;
    }

    public static FileManager getInstance() {
        if (instance == null) {
            synchronized (FileManager.class) {
                if (instance == null)
                    instance = new FileManager();
            }
        }
        return instance;
    }

    public String loadText(String fileName) {
        StringBuilder builder = new StringBuilder();
        try {
            FileReader reader = new FileReader(rootDir + fileName);
            BufferedReader buffer = new BufferedReader(reader);
            String currentLine = buffer.readLine();
            while (currentLine != null) {
                builder.append(currentLine);
                builder.append("\n");
                currentLine = buffer.readLine();
            }
        } catch (IOException exp) {
            System.err.println(exp.getMessage());
        }
        return builder.toString();
    }

    public Texture loadTexture(String fileName) {
        int width;
        int height;
        ByteBuffer buffer;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            String filePath = System.getProperty("user.dir") + File.separator + rootDir + fileName;
            IntBuffer widthBuffer = stack.mallocInt(1);
            IntBuffer heightBuffer = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);
            final int channelCount = 4;
            buffer = stbi_load(filePath, widthBuffer, heightBuffer, channels, channelCount);
            if (buffer == null)
                throw new IllegalStateException("Image file " + fileName + " not loaded: " + stbi_failure_reason());
            width = widthBuffer.get();
            height = heightBuffer.get();
        }
        Texture texture = Texture.createFromBytes(buffer, width, height);
        stbi_image_free(buffer);
        return texture;
    }

    public void clearSaves() {
        File dir = new File(System.getProperty("user.dir") + File.separator + saveDir);
        File[] list = dir.listFiles();
        if (list != null) {
            for (File file : list)
                file.delete();
        }
    }

    public boolean existSavedWorld() {
        try {
            String filePath = System.getProperty("user.dir") + File.separator + saveDir + "world";
            FileInputStream inStream = new FileInputStream(filePath);
        } catch (FileNotFoundException notFoundExp) {
            return false;
        }
        return true;
    }

    public void saveWorldInfo(WorldInfo info) {
        try {
            String filePath = System.getProperty("user.dir") + File.separator + saveDir + "world";
            FileOutputStream outStream = new FileOutputStream(filePath);
            byte[] xPosBytes = floatToByteArray(info.playerPos.x);
            byte[] yPosBytes = floatToByteArray(info.playerPos.y);
            byte[] zPosBytes = floatToByteArray(info.playerPos.z);
            byte[] xRotBytes = floatToByteArray(info.playerRot.x);
            byte[] yRotBytes = floatToByteArray(info.playerRot.y);
            byte[] zRotBytes = floatToByteArray(info.playerRot.z);
            byte[] secondsBytes = floatToByteArray((float) info.playerMetrics.getSecondsInGame());
            byte[] dirtBlocksBytes = intToByteArray(info.playerMetrics.getDirtBlocksDug());
            byte[] walkedBytes = floatToByteArray(info.playerMetrics.getBlocksWalked());
            byte[] seedLengthBytes = intToByteArray(info.seed.length());
            byte[] seedBytes = info.seed.getBytes();
            outStream.write(xPosBytes);
            outStream.write(yPosBytes);
            outStream.write(zPosBytes);
            outStream.write(xRotBytes);
            outStream.write(yRotBytes);
            outStream.write(zRotBytes);
            outStream.write(secondsBytes);
            outStream.write(dirtBlocksBytes);
            outStream.write(walkedBytes);
            outStream.write(seedLengthBytes);
            outStream.write(seedBytes);
            outStream.close();
        } catch (IOException exp) {
            exp.printStackTrace();
        }
    }

    private byte[] floatToByteArray(float value) {
        int intBits = Float.floatToIntBits(value);
        return intToByteArray(intBits);
    }

    private byte[] intToByteArray(int value) {
        return new byte[]{
                (byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) (value)};
    }

    public WorldInfo loadWorldInfo() {
        WorldInfo info = new WorldInfo();
        try {
            String filePath = System.getProperty("user.dir") + File.separator + saveDir + "world";
            FileInputStream inStream = new FileInputStream(filePath);
            byte[] xPosBytes = new byte[4];
            byte[] yPosBytes = new byte[4];
            byte[] zPosBytes = new byte[4];
            byte[] xRotBytes = new byte[4];
            byte[] yRotBytes = new byte[4];
            byte[] zRotBytes = new byte[4];
            byte[] secondsBytes = new byte[4];
            byte[] dirtBlocksBytes = new byte[4];
            byte[] walkedBytes = new byte[4];
            byte[] seedLengthBytes = new byte[4];
            inStream.read(xPosBytes);
            inStream.read(yPosBytes);
            inStream.read(zPosBytes);
            inStream.read(xRotBytes);
            inStream.read(yRotBytes);
            inStream.read(zRotBytes);
            inStream.read(secondsBytes);
            inStream.read(dirtBlocksBytes);
            inStream.read(walkedBytes);
            inStream.read(seedLengthBytes);
            info.playerPos =
                    new Vector3f(floatFromBytes(xPosBytes), floatFromBytes(yPosBytes), floatFromBytes(zPosBytes));
            info.playerRot =
                    new Vector3f(floatFromBytes(xRotBytes), floatFromBytes(yRotBytes), floatFromBytes(zRotBytes));
            info.playerMetrics = new Metrics(
                    floatFromBytes(secondsBytes),
                    intFromBytes(dirtBlocksBytes),
                    floatFromBytes(walkedBytes));
            int seedLength = intFromBytes(seedLengthBytes);
            byte[] seedBytes = new byte[seedLength];
            inStream.read(seedBytes);
            info.seed = new String(seedBytes);
            inStream.close();
        } catch (IOException exp) {
            exp.printStackTrace();
        }
        return info;
    }

    private int intFromBytes(byte[] value) {
        return ByteBuffer.wrap(value).getInt();
    }

    private float floatFromBytes(byte[] value) {
        return ByteBuffer.wrap(value).getFloat();
    }

    public void serializeChunkToDisk(Chunk toSerialize) {
        if (toSerialize != null) {
            try {
                Coord3Int worldPosition = toSerialize.getWorldPosition();
                String filePath = System.getProperty("user.dir") + File.separator + saveDir + "c_" +
                        worldPosition.y + "_" + worldPosition.x + "_" + worldPosition.z;
                FileOutputStream outStream = new FileOutputStream(filePath);
                writeToFile(outStream, toSerialize);
                outStream.close();
            } catch (IOException exp) {
                exp.printStackTrace();
            }
        }
    }

    private void writeToFile(FileOutputStream outStream, Chunk chunk) throws IOException {
        for (int y = 0; y < Chunk.getSize(); ++y) {
            for (int x = 0; x < Chunk.getSize(); ++x) {
                for (int z = 0; z < Chunk.getSize(); ++z) {
                    byte blockID = chunk.getVoxelID(y, x, z);
                    outStream.write(blockID);
                }
            }
        }
    }

    public Chunk deserializeChunkFromDisk(Coord3Int worldPosition) {
        Chunk deserialized = null;
        try {
            String filePath = System.getProperty("user.dir") + File.separator + saveDir + "c_" +
                    worldPosition.y + "_" + worldPosition.x + "_" + worldPosition.z;
            FileInputStream inStream = new FileInputStream(filePath);
            deserialized = readFromFile(inStream);
            deserialized.setWorldPosition(worldPosition);
            inStream.close();
        } catch (FileNotFoundException notFoundExp) {
            return null;
        } catch (IOException exp) {
            exp.printStackTrace();
            System.exit(exp.hashCode());
        }
        return deserialized;
    }

    private Chunk readFromFile(FileInputStream inStream) throws IOException {
        final int chunkSize = Chunk.getSize();
        byte[] buffer = new byte[chunkSize * chunkSize * chunkSize];
        inStream.read(buffer);
        Chunk deserialized = new ArrayChunk();
        int i = 0;
        for (int y = 0; y < chunkSize; ++y) {
            for (int x = 0; x < chunkSize; ++x) {
                for (int z = 0; z < chunkSize; ++z) {
                    deserialized.setVoxelID(y, x, z, buffer[i]);
                    ++i;
                }
            }
        }
        return deserialized;
    }
}
