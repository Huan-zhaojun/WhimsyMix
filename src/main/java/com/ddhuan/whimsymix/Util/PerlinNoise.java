package com.ddhuan.whimsymix.Util;

import java.util.Random;

//柏林噪声
public final class PerlinNoise {
    private static final int[] GRAD3 = {
            1, 1, 0, -1, 1, 0, 1, -1, 0, -1, -1, 0,
            1, 0, 1, -1, 0, 1, 1, 0, 0, 0, -1, 1,
            0, 1, -1, 0, 0, 0, 1, -1, -1, 0, -1, 0
    };

    private int[] permutation;

    protected PerlinNoise() {
        Random rand = new Random();
        permutation = new int[512];
        for (int i = 0; i < 256; i++) {
            permutation[i] = i;
        }
        for (int i = 0; i < 256; i++) {
            int j = rand.nextInt(256);
            //Swap
            int temp = permutation[i];
            permutation[i] = permutation[j];
            permutation[j] = temp;
            permutation[i + 256] = permutation[i]; // Duplicate
        }
    }

    private double grad(int hash, double x, double y, double z) {
        int h = hash & 15; // Convert low 4 bits of hash code
        double u = h < 8 ? x : y; // Gradient value 1
        double v = h < 4 ? y : (h == 12 || h == 14 ? x : z); // Gradient value 2
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v); // Gradient calculation
    }

    public double noise(double x, double y, double z) {
        int X = (int) Math.floor(x) & 255;
        int Y = (int) Math.floor(y) & 255;
        int Z = (int) Math.floor(z) & 255;

        x -= Math.floor(x);
        y -= Math.floor(y);
        z -= Math.floor(z);

        double u = fade(x);
        double v = fade(y);
        double w = fade(z);

        int A = permutation[X] + Y;
        int AA = permutation[A] + Z;
        int AB = permutation[A + 1] + Z;
        int B = permutation[X + 1] + Y;
        int BA = permutation[B] + Z;
        int BB = permutation[B + 1] + Z;

        return lerp(w,
                lerp(v,
                        lerp(u, grad(permutation[AA], x, y, z), grad(permutation[BA], x - 1, y, z)),
                        lerp(u, grad(permutation[AB], x, y - 1, z), grad(permutation[BB], x - 1, y - 1, z))),
                lerp(v,
                        lerp(u, grad(permutation[AA + 1], x, y, z - 1), grad(permutation[BA + 1], x - 1, y, z - 1)),
                        lerp(u, grad(permutation[AB + 1], x, y - 1, z - 1), grad(permutation[BB + 1], x - 1, y - 1, z - 1))));
    }

    private double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }
}
