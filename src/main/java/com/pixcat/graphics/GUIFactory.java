package com.pixcat.graphics;

import com.pixcat.mesh.Mesh;

public class GUIFactory {
    private static GUIFactory instance = null;

    private Mesh modelMesh;

    private GUIFactory() {
        float[] positions = new float[]{
                0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
        };
        float[] texCoords = new float[]{
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f
        };
        int[] indices = new int[]{
                0, 1, 3, 3, 1, 2,
        };
        modelMesh = new Mesh(positions, texCoords, indices);
    }

    public static GUIFactory getInstance() {
        if (instance == null) {
            synchronized (GUIFactory.class) {
                if (instance == null)
                    instance = new GUIFactory();
            }
        }
        return instance;
    }

    public StaticImage makeImage(int width, int height, Texture texture) {
        StaticImage image = new StaticImage(modelMesh, width, height);
        image.setTexture(texture);
        return image;
    }

    public Button makeButton(int width, int height, Texture texture) {
        Button button = new Button(modelMesh, width, height);
        button.setTexture(texture);
        return button;
    }
}
