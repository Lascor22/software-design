package ru.itmo.sd.graph.gfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import ru.itmo.sd.graph.RunGraphLayout;

import java.awt.*;

public class JavaFXGraphRender extends Application implements GraphRender {

    private static double width = 800;
    private static double height = 600;

    public JavaFXGraphRender(double width, double height) {
        JavaFXGraphRender.height = height;
        JavaFXGraphRender.width = width;
        launch();
    }

    public JavaFXGraphRender() {
    }

    private volatile GraphicsContext context = null;
    private final Canvas CANVAS = new Canvas();

    @Override
    public void start(Stage stage) {
        StackPane pane = new StackPane();
        pane.getChildren().add(CANVAS);
        CANVAS.setHeight(height);
        CANVAS.setWidth(width);
        pane.setMinSize(100, 100);
        Scene scene = new Scene(pane);
        stage.setTitle("Graph plot");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
        context = CANVAS.getGraphicsContext2D();
        RunGraphLayout.render(this);
    }

    @Override
    public void setLineWidth(double width) {
        context.setLineWidth(width);
    }

    @Override
    public void strokeLine(double fx, double fy, double tx, double ty) {
        double cx = width / 2, cy = height / 2;
        context.strokeLine(cx + fx, cy + fy, cx + tx, cy + ty);
    }

    @Override
    public void fillCircle(double x, double y, double r) {
        double cx = width / 2, cy = height / 2;
        context.fillOval(cx + x - r / 2, cy + y - r / 2, r, r);
    }

    @Override
    public void setStroke(Color color) {
        context.setStroke(javafx.scene.paint.Color.rgb(
                color.getRed(), color.getGreen(),
                color.getBlue(), color.getAlpha() / 255.0
        ));
    }

    @Override
    public void setFill(Color color) {
        context.setFill(javafx.scene.paint.Color.rgb(
                color.getRed(), color.getGreen(),
                color.getBlue(), color.getAlpha() / 255.0
        ));
    }

    @Override
    public void clear() {
        Paint paint = context.getFill();
        context.setFill(javafx.scene.paint.Color.WHITE);
        context.fillRect(0, 0, width, height);
        context.setFill(paint);
    }
}
