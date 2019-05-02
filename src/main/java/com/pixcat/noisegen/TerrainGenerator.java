package com.pixcat.noisegen;

public class TerrainGenerator {
    private Noise primaryNoise;
    private Noise secondaryNoise;

    public TerrainGenerator(int seed) {
        setSeed(seed);
    }

    public void setSeed(int seed) {
        primaryNoise = new SimplexNoise(seed);
        secondaryNoise = new SimplexNoise((seed * 2) + (seed / 1000) + 11);
    }

    public int getBlockColorAt(int x, int y) {
        double elevation = baseTerrain(x, y);
        double bumpiness = 0.40625 - elevation;
        double grass = grasslandPattern(x, y);
        elevation += bumpiness * grass;
        elevation -= oceanPattern(x, y);
        if (elevation > 0.40625)
            elevation -= (1.0 - elevation / 10.0) * riverPattern(x, y, grass);
        else
            elevation -= (elevation * 1.8) * riverPattern(x, y, 0.0);

        if (elevation <= 0.0)
            elevation = 0.0078125;
        elevation = Math.round(elevation * 128.0) / 128.0;

        double tree = treePattern(x, y, elevation);
        if (tree >= 1.0)
            return rgb(110, 80, 60);
        return rgb(elevation);
    }

    private double baseTerrain(int x, int y) {
        final double delta = 0.01;
        double nx = delta * x;
        double ny = delta * y;
        double elevation = primaryNoise.getValue(nx, ny) +
                0.6 * primaryNoise.getValue(2 * nx, 2 * ny) +
                0.25 * primaryNoise.getValue(4 * nx, 4 * ny) +
                0.125 * primaryNoise.getValue(8 * nx, 8 * ny) +
                0.0625 * primaryNoise.getValue(16 * nx, 16 * ny);
        elevation /= (1.0 + 0.6 + 0.25 + 0.125 + 0.0625);
        elevation = Math.pow(elevation, 0.65);
        elevation = terrainLowerSmooth(elevation);
        return elevation;
    }

    private double terrainLowerSmooth(double elevation) {
        if (elevation > 0.421875 && elevation <= 0.62)
            elevation = Math.round(elevation * 21) / 21.0;
        return elevation;
    }

    private double grasslandPattern(int x, int y) {
        final double delta = 0.00088;
        double nx = delta * x;
        double ny = delta * y;
        double area = secondaryNoise.getValue(nx, ny) +
                0.11 * secondaryNoise.getValue(4 * nx, 4 * ny);
        area = Math.pow(Math.sin(area), 4.0);
        return (area > 0.01 ? Math.min(1.25 * area, 1.0) : 0.0);
    }

    private double oceanPattern(int x, int y) {
        final double delta = 0.0007;
        double nx = delta * x;
        double ny = delta * y;
        double depth = primaryNoise.getValue(nx, ny) +
                0.12 * primaryNoise.getValue(4 * nx, 4 * ny) +
                0.01 * primaryNoise.getValue(32 * nx, 32 * ny);
        depth /= 1.14;
        depth = oceanSmooth(Math.pow(depth, 4.4));
        if (depth < 0.0) {
            depth = 0.0;
        }
        return depth;
    }

    private double oceanSmooth(double val) {
        return -2.0 * (val - 0.34) * (val - 1.4);
    }

    private double riverPattern(int x, int y, double grass) {
        final double delta = 0.003;
        double nx = delta * x;
        double ny = delta * y;
        double riverbed = ridgedNoise(nx, ny);
        riverbed = riverSmooth(Math.pow(riverbed, 5.5)) - (grass * grass * 0.28);
        if (riverbed < 0.0)
            riverbed = 0.0;
        return riverbed;
    }

    private double ridgedNoise(double nx, double ny) {
        double value = primaryNoise.getValue(nx, ny) +
                0.5 * primaryNoise.getValue(2 * nx, 2 * ny) +
                0.25 * primaryNoise.getValue(4 * nx, 4 * ny) +
                0.125 * primaryNoise.getValue(8 * nx, 8 * ny);
        value /= (1.0 + 0.5 + 0.25 + 0.125);
        return 1.846 * (0.5 - Math.abs(0.5 - value));
    }

    private double riverSmooth(double val) {
        return -2.0 * (val - 0.4) * (val - 1.54);
    }

    private double treePattern(int x, int y, double elevation) {
        double tree = treeAt(x, y, elevation);
        if (tree >= 1.0) {
            tree -= treeAt(x + 1, y, elevation) +
                    treeAt(x, y + 1, elevation) +
                    treeAt(x + 1, y - 1, elevation);
        }
        if (tree < 0.0)
            tree = 0.0;
        return tree;
    }

    private double treeAt(int x, int y, double elevation) {
        final double delta = 0.5;
        double nx = delta * x;
        double ny = delta * y;
        double tree = Math.pow(secondaryNoise.getValue(nx, ny), 105.0);
        double forestation = Math.pow(secondaryNoise.getValue(nx / 100.0, ny / 100.0), 3.0);
        double threshold = forestThreshold(elevation, forestation);
        tree = (tree > threshold ? 1.0 : 0.0);
        return tree;
    }

    private double forestThreshold(double elevation, double forestation) {
        if (elevation <= 0.40626 || elevation >= 0.9)
            return 1.0;
        else if (forestation > 0.1)
            forestation = Math.round(forestation * 3) / 3.0;
        else
            forestation = 0.0;

        if (forestation <= 0.2)
            return 0.7;
        else if (forestation <= 0.34)
            return 0.1;
        else if (forestation <= 0.67)
            return 0.005;
        else
            return 0.0001;
    }

    //Helper functions -- TODO temporary
    private int rgb(double normalizedGrey) {
        int grey = (int) (255 * normalizedGrey);

        if (normalizedGrey <= 0.40625) //sea level
            return rgb(0, 0, grey + 110);
        return rgb(0, grey, 0);
    }

    private int rgb(int red, int green, int blue) {
        red &= 0xFF;
        green &= 0xFF;
        blue &= 0xFF;
        int val = 0xFF000000;
        val |= ((red << 16) | (green << 8) | blue);
        return val;
    }
    //Helper functions end
}
