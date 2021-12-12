package ru.itmo.sd.graph.utils;

import java.awt.*;

public class ColorUtils {
    private static final double[][] STOP_POLARS = new double[][]{
            {Math.cos(0.0D), Math.sin(0.0D)},
            {Math.cos(2.0943951023931953D), Math.sin(2.0943951023931953D)},
            {Math.cos(-2.0943951023931953D), Math.sin(-2.0943951023931953D)}
    };

    public static Color getSpectrumColor(double radius, double radians) {
        if (radius > 1.0D) {
            return Color.BLACK;
        } else {
            int[] stops = new int[STOP_POLARS.length];
            for (int i = 0; i < STOP_POLARS.length; ++i) {
                double dX = Math.cos(radians) - STOP_POLARS[i][0];
                double dY = Math.sin(radians) - STOP_POLARS[i][1];
                double polarDistance = dX * dX + dY * dY;
                stops[i] = (int) (255.0D - polarDistance / 4.0D * 255.0D);
            }
            return new Color(stops[0], stops[1], stops[2]);
        }
    }

}
