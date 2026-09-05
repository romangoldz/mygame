package com.mygame.utils;

import com.badlogic.gdx.graphics.Color;

public class ColorUtils {
    
    public static int rgbToInt(float r, float g, float b) {
        int ir = (int)(r * 255) & 0xFF;
        int ig = (int)(g * 255) & 0xFF;
        int ib = (int)(b * 255) & 0xFF;
        return (ir << 16) | (ig << 8) | ib;
    }
    
    public static int rgbToInt(Color color) {
        return rgbToInt(color.r, color.g, color.b);
    }
    
    public static Color intToRgb(int rgb) {
        float r = ((rgb >> 16) & 0xFF) / 255f;
        float g = ((rgb >> 8) & 0xFF) / 255f;
        float b = (rgb & 0xFF) / 255f;
        return new Color(r, g, b, 1f);
    }
    
    public static int rgbToAlpha(int rgb, int alpha) {
        return (rgb << 8) | (alpha & 0xFF);
    }
}