package ru.itmo.sd.graph.gfx;

import ru.itmo.sd.graph.RunGraphLayout;

import static java.awt.BasicStroke.*;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;

public class AWTGraphRender implements GraphRender {

    private final double width;
    private final double height;
    private final BufferStrategy bufferStrategy;
    private final Graphics graphics;

    public AWTGraphRender(int width, int height) {
        this.width = width;
        this.height = height;

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenD = toolkit.getScreenSize();

        JFrame frame = new JFrame("Graph plot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLocation((screenD.width - width) / 2,
                (screenD.height - height) / 2);
        frame.setSize(width, height);
        frame.setResizable(false);
        frame.setVisible(true);

        JLayeredPane panel = new JLayeredPane();
        panel.setLayout(null);

        Canvas canvas = new Canvas();
        canvas.setSize(width, height);
        panel.add(canvas, 1);
        frame.add(panel);
        frame.setVisible(true);

        canvas.createBufferStrategy(3);
        bufferStrategy = canvas.getBufferStrategy();
        graphics = bufferStrategy.getDrawGraphics();
        bufferStrategy.show();
        RunGraphLayout.render(this);
    }

    @Override
    public void strokeLine(double fx, double fy, double tx, double ty) {
        double cx = width / 2, cy = height / 2;
        graphics.drawLine((int) (cx + fx), (int) (cy + fy),
                (int) (cx + tx), (int) (cy + ty));
        bufferStrategy.show();
    }

    @Override
    public void fillCircle(double x, double y, double r) {
        double cx = width / 2, cy = height / 2;
        int ix = (int) (cx + x - r / 2),
                iy = (int) (cy + y - r / 2);
        graphics.fillOval(ix, iy, (int) r, (int) r);
        bufferStrategy.show();
    }

    @Override
    public void setStroke(Color color) {
        graphics.setColor(color);
        bufferStrategy.show();
    }

    @Override
    public void setFill(Color color) {
        graphics.setColor(color);
        bufferStrategy.show();
    }

    @Override
    public void setLineWidth(double width) {
        float w = (float) width;
        ((Graphics2D) graphics).setStroke(new BasicStroke(w, CAP_ROUND, JOIN_ROUND));
        bufferStrategy.show();
    }

    @Override
    public void clear() {
        graphics.clearRect(0, 0, (int) width, (int) height);
    }

}
