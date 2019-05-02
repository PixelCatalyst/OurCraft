package com.pixcat.noisegen;

public interface Noise {

    void setSeed(int seed);

    double getValue(double xin, double yin);
}
