package ru.mellvis.util;

public final class ColorUtil {
    private ColorUtil() {}

    public static int argb(int a, int r, int g, int b) {
        return ((a & 255) << 24) | ((r & 255) << 16) | ((g & 255) << 8) | (b & 255);
    }

    public static int gradient(long offsetMs) {
        float hue = ((System.currentTimeMillis() + offsetMs) % 3000L) / 3000.0f;
        return java.awt.Color.HSBtoRGB(hue, 0.75f, 1.0f) | 0xFF000000;
    }

    public static int lerp(int c1, int c2, float t) {
        t = Math.max(0, Math.min(1, t));
        int a = (int)(((c1 >>> 24) & 255) + (((c2 >>> 24) & 255) - ((c1 >>> 24) & 255)) * t);
        int r = (int)(((c1 >>> 16) & 255) + (((c2 >>> 16) & 255) - ((c1 >>> 16) & 255)) * t);
        int g = (int)(((c1 >>> 8) & 255) + (((c2 >>> 8) & 255) - ((c1 >>> 8) & 255)) * t);
        int b = (int)((c1 & 255) + ((c2 & 255) - (c1 & 255)) * t);
        return argb(a, r, g, b);
    }
}
