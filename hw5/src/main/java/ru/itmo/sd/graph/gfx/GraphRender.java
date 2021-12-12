package ru.itmo.sd.graph.gfx;

import java.awt.Color;

public interface GraphRender {

    void strokeLine(double fx, double fy, double tx, double ty);

    void fillCircle(double x, double y, double r);

    void setStroke(Color color);

    void setFill(Color color);

    void setLineWidth(double width);

    void clear();

}
