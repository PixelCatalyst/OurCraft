package com.pixcat.core;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class FileManager {
    private static FileManager instance = null;

    private String rootDir;

    private FileManager() {
        rootDir = "resources\\";
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
}
